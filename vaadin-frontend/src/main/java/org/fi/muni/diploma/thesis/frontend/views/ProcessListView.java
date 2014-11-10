package org.fi.muni.diploma.thesis.frontend.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterDecorator;
import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterGenerator;
import org.fi.muni.diploma.thesis.utils.ProcessStateMap;
import org.fi.muni.diploma.thesis.utils.RuntimeEngineWrapper;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbProcessInstanceLog;
import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.paged.PagedFilterControlConfig;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * View which displays list of all processes
 * 
 * @author osiris
 * 
 */

public class ProcessListView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;

	private Navigator navigator;

	// /private AuditLogService auditService;

	private PagedFilterTable<?> filterTable;
	private final static Logger logger = Logger.getLogger(ProcessListView.class.getName());
	public static final String NAME = "lifecycleinstances";

	class ButtonListener implements Button.ClickListener {

		private static final long serialVersionUID = 2728100067980270439L;

		String menuitem;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			logger.info("button click, redirecting to Process Details page");
			ProcessListView.this.navigator.navigateTo("main" + "/" + menuitem + "?id="
					+ String.valueOf((Long) event.getButton().getData()));

		}

	}

	@SuppressWarnings("unchecked")
	public ProcessListView(Navigator navigator) {
		this.setNavigator(navigator);
		setSizeFull();

		// auditService = new AuditServiceWrapper();

		// find all lifecycles for 'new service'
		List<JaxbProcessInstanceLog> processLogNewService = (List<JaxbProcessInstanceLog>) RuntimeEngineWrapper
				.getEngine().getAuditLogService().findProcessInstances("service-lifecycle-new-service");

		// find all lifecycles for 'existing service'
		List<JaxbProcessInstanceLog> processLogExistingService = (List<JaxbProcessInstanceLog>) RuntimeEngineWrapper
				.getEngine().getAuditLogService().findProcessInstances("service-lifecycle-existing-service");

		// all lifecycle instances
		List<JaxbProcessInstanceLog> allProcesses = new ArrayList<JaxbProcessInstanceLog>();
		allProcesses.addAll(processLogExistingService);
		allProcesses.addAll(processLogNewService);

		VerticalLayout layout = new VerticalLayout();

		filterTable = buildFilterTable(allProcesses);
		filterTable.setPageLength(filterTable.getContainerDataSource().size());

		Label greeting = new Label("List of lifecycle instances");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");

		layout.addComponent(greeting);
		layout.addComponent(filterTable);
		layout.setComponentAlignment(greeting, Alignment.TOP_CENTER);
		layout.setComponentAlignment(filterTable, Alignment.TOP_CENTER);

		PagedFilterControlConfig config = new PagedFilterControlConfig();
		config.setItemsPerPage("Lifecycles per page:");
		List<Integer> lengths = new ArrayList<Integer>();
		lengths.add(10);
		lengths.add(20);
		lengths.add(50);
		lengths.add(100);
		lengths.add(250);
		lengths.add(500);
		config.setPageLengthsAndCaptions(lengths);

		layout.addComponent(filterTable.createControls(config));

		addComponent(layout);

	}

	private PagedFilterTable<IndexedContainer> buildFilterTable(List<JaxbProcessInstanceLog> allProcesses) {

		PagedFilterTable<IndexedContainer> filterTable = new PagedFilterTable<IndexedContainer>("");
		// filterTable.setSizeFull();
		filterTable.setFilterDecorator(new CustomFilterDecorator());
		filterTable.setFilterGenerator(new CustomFilterGenerator());
		filterTable.setContainerDataSource(buildContainer(allProcesses));
		filterTable.setFilterBarVisible(true);
		filterTable.setFilterFieldVisible("Details", false);
		filterTable.setFilterFieldValue("Process State", ProcessStateMap.States.ACTIVE);

		return filterTable;
	}

	@SuppressWarnings("unchecked")
	private Container buildContainer(List<JaxbProcessInstanceLog> allProcesses) {

		IndexedContainer cont = new IndexedContainer();

		cont.addContainerProperty("Process Name", String.class, null);
		cont.addContainerProperty("Process Instance ID", Long.class, null);
		cont.addContainerProperty("Process State", ProcessStateMap.States.class, null);
		cont.addContainerProperty("Start Date", Date.class, null);
		cont.addContainerProperty("End Date", Date.class, null);
		cont.addContainerProperty("Details", Button.class, null);

		int i = 1;

		for (JaxbProcessInstanceLog process : allProcesses) {
			cont.addItem(i);

			cont.getContainerProperty(i, "Process Name").setValue(process.getProcessName());
			cont.getContainerProperty(i, "Process Instance ID").setValue(process.getProcessInstanceId());
			cont.getContainerProperty(i, "Process State").setValue(
					ProcessStateMap.getProcessStatusAsEnum(process.getStatus()));
			cont.getContainerProperty(i, "Start Date").setValue(process.getStart());
			cont.getContainerProperty(i, "End Date").setValue(process.getEnd());

			Button detailsField = new Button("show details");
			detailsField.setData(process.getProcessInstanceId());
			detailsField.addClickListener(new ButtonListener(ProcessDetailView.NAME));
			detailsField.addStyleName(BaseTheme.BUTTON_LINK);

			cont.getContainerProperty(i, "Details").setValue(detailsField);

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

	public FilterTable getFilterTable() {
		return filterTable;
	}

	public void setFilterTable(PagedFilterTable<?> filterTable) {
		this.filterTable = filterTable;
	}

}
