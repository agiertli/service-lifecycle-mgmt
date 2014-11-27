package org.fi.muni.diploma.thesis.frontend.views;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterDecorator;
import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterGenerator;
import org.fi.muni.diploma.thesis.utils.jbpm.RuntimeEngineWrapper;
import org.fi.muni.diploma.thesis.utils.jbpm.TaskServiceWrapper;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbProcessInstanceLog;
import org.tepi.filtertable.paged.PagedFilterControlConfig;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomTable.RowHeaderMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class TaskListView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private TaskService taskService;
	private Navigator navigator;
	public final static String NAME = "lifecycletasks";
	private final int ROW_HEIGHT = 43;
	private final int TABLE_OFFSET = 65;

	private PagedFilterTable<?> taskListTable;
	private Long taskId;

	private final static Logger logger = Logger.getLogger(TaskListView.class.getName());

	class ButtonListener implements Button.ClickListener {

		private static final long serialVersionUID = 2728100067980270439L;

		String menuitem;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			TaskListView.this.navigator.navigateTo("main" + "/" + menuitem + "?id=" + String.valueOf((Long) event.getButton().getData()));

		}

	}

	public TaskListView(Navigator navigator, String username) {

		logger.info("task list username:" + username);

		this.setNavigator(navigator);
		taskService = new TaskServiceWrapper();

		// there seems to be a bug in the jBPM 6.2.0.CR1 release - only 10 results are returned..
		// this is completely out of my hands..
		List<TaskSummary> taskList = taskService.getTasksAssignedAsPotentialOwner(username, "en-UK");

		logger.info("tsak list size:" + taskList.size());

		VerticalLayout layout = new VerticalLayout();
		taskListTable = buildTaskListTable(taskList);

		taskListTable = buildTaskListTable(taskList);
		int rowcount = taskListTable.getItemIds().size();
		if (rowcount > taskListTable.getPageLength()) {

			taskListTable.setHeight(String.valueOf(ROW_HEIGHT * taskListTable.getPageLength() + TABLE_OFFSET) + "px");

		} else {

			taskListTable.setHeight(String.valueOf(ROW_HEIGHT * rowcount + TABLE_OFFSET) + "px");
		}

		taskListTable.addItemSetChangeListener(new ItemSetChangeListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {

				int rowcount = taskListTable.getItemIds().size();

				if (rowcount > taskListTable.getPageLength()) {

					taskListTable.setHeight(String.valueOf(ROW_HEIGHT * taskListTable.getPageLength() + TABLE_OFFSET) + "px");

				} else {

					taskListTable.setHeight(String.valueOf(ROW_HEIGHT * rowcount + TABLE_OFFSET) + "px");
				}
			}
		});

		taskListTable.setColumnWidth("Task ID", 80);

		Label greeting = new Label("Lifecycle's Task");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");

		PagedFilterControlConfig config = new PagedFilterControlConfig();
		config.setItemsPerPage("Tasks per page:");
		List<Integer> lengths = new ArrayList<Integer>();
		lengths.add(10);
		lengths.add(20);
		lengths.add(50);
		lengths.add(100);
		lengths.add(250);
		lengths.add(500);
		config.setPageLengthsAndCaptions(lengths);

		layout.addComponent(greeting);
		layout.addComponent(taskListTable);

		HorizontalLayout h = taskListTable.createControls(config);

		layout.addComponent(h);
		layout.setComponentAlignment(h, Alignment.TOP_CENTER);
		layout.setComponentAlignment(greeting, Alignment.TOP_CENTER);
		layout.setComponentAlignment(taskListTable, Alignment.TOP_CENTER);

		addComponent(layout);

	}

	private PagedFilterTable<?> buildTaskListTable(List<TaskSummary> taskList) {

		PagedFilterTable<IndexedContainer> filterTable = new PagedFilterTable<IndexedContainer>("");
		// filterTable.setSizeFull();
		filterTable.setFilterDecorator(new CustomFilterDecorator());
		filterTable.setFilterGenerator(new CustomFilterGenerator());

		filterTable.setHeightUndefined();

		filterTable.setSelectable(false);
		filterTable.setImmediate(true);
		filterTable.setMultiSelect(true);
		// filterTable.setPageLength(10);

		filterTable.setColumnCollapsingAllowed(true);
		filterTable.setRowHeaderMode(RowHeaderMode.INDEX);

		filterTable.setColumnReorderingAllowed(true);

		filterTable.setContainerDataSource(buildContainer(taskList));
		filterTable.setFilterBarVisible(true);
		filterTable.setFilterFieldVisible("Action", false);

		return filterTable;
	}

	@SuppressWarnings("unchecked")
	private Container buildContainer(List<TaskSummary> taskList) {

		IndexedContainer cont = new IndexedContainer();

		cont.addContainerProperty("Task Name", String.class, null);
		cont.addContainerProperty("Task ID", Long.class, null);
		cont.addContainerProperty("Process Instance ID", Long.class, null);
		cont.addContainerProperty("Process Name", String.class, null);
		cont.addContainerProperty("Role Assigned", String.class, null);
		cont.addContainerProperty("Action", Button.class, null);

		int i = 1;

		for (TaskSummary taskSummary : taskList) {

			// dirty hack, bug in kie-wb creates "dead" process instance with id 1, and starts the task, with id 1..
			// it's not possible to work on this task, so we are excluding it
			if (taskSummary.getId() == 1 && taskSummary.getStatus().name().equals("InProgress")) {
				continue;
			}

			cont.addItem(i);

			cont.getContainerProperty(i, "Task Name").setValue(taskSummary.getName());
			cont.getContainerProperty(i, "Task ID").setValue(taskSummary.getId());

			JaxbProcessInstanceLog p = (JaxbProcessInstanceLog) RuntimeEngineWrapper.getEngine().getAuditLogService()
					.findProcessInstance(taskSummary.getProcessInstanceId());

			if (p.getParentProcessInstanceId() == null) {

				cont.getContainerProperty(i, "Process Instance ID").setValue(taskSummary.getProcessInstanceId());
				cont.getContainerProperty(i, "Process Name").setValue(p.getProcessName());
			} else {

				cont.getContainerProperty(i, "Process Instance ID").setValue(p.getParentProcessInstanceId());
				cont.getContainerProperty(i, "Process Name").setValue(
						RuntimeEngineWrapper.getEngine().getAuditLogService().findProcessInstance(p.getParentProcessInstanceId()).getProcessName());
			}

			cont.getContainerProperty(i, "Role Assigned").setValue((String) taskService.getTaskContent(taskSummary.getId()).get("group"));

			Button detailsField = new Button("Work on Task");
			detailsField.setData(taskSummary.getId());
			detailsField.addClickListener(new ButtonListener(TaskDetailView.NAME));
			detailsField.addStyleName(BaseTheme.BUTTON_LINK);

			cont.getContainerProperty(i, "Action").setValue(detailsField);

			i++;
		}

		return cont;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public PagedFilterTable<?> getTaskListTable() {
		return taskListTable;
	}

	public void setTaskListTable(PagedFilterTable<?> taskListTable) {
		this.taskListTable = taskListTable;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

}
