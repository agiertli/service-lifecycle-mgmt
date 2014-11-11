package org.fi.muni.diploma.thesis.frontend.views.humantaskform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterDecorator;
import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterGenerator;
import org.fi.muni.diploma.thesis.frontend.views.TaskDetailView;
import org.fi.muni.diploma.thesis.frontend.views.TaskListView;
import org.fi.muni.diploma.thesis.service_lifecycle_model.Service;
import org.fi.muni.diploma.thesis.utils.TaskServiceWrapper;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskName;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskOutput;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskOutputType;
import org.fi.muni.diploma.thesis.utils.humantask.InternalHumanTask;
import org.kie.api.task.TaskService;
import org.tepi.filtertable.paged.PagedFilterControlConfig;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class HumanTaskForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private InternalHumanTask humanTask;
	private PropertysetItem itemset;
	private FieldGroup binder;
	private Long taskid;
	private static final Logger logger = Logger.getLogger(HumanTaskForm.class.getName());
	private TaskService taskService;
	public Navigator navigator;
	private TextField uuid;

	// for storing integer
	final MyBean myBean = new MyBean();
	BeanItem<MyBean> beanItem = new BeanItem<MyBean>(myBean);
	@SuppressWarnings("unchecked")
	final Property<Integer> integerProperty = (Property<Integer>) beanItem.getItemProperty("value");

	// for presenting found services in S-RAMP repository
	private PagedFilterTable<?> filterTable;

	public class SelectListener implements ClickListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO Auto-generated method stub

			String uuid = (String) event.getButton().getData();
			HumanTaskForm.this.getUuid().setValue(uuid);

		}

	}

	public class ButtonListener implements ClickListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		String menuitem;
		Boolean copy;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			taskService = new TaskServiceWrapper();
			//
			logger.info("button clicked");

			FieldGroup data = (FieldGroup) event.getButton().getData();
			try {
				data.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Long id = HumanTaskForm.this.getTaskid();

			Map<String, Object> result = new HashMap<String, Object>();

			Collection<Field<?>> col = data.getFields();
			//
			for (Iterator<Field<?>> iterator = col.iterator(); iterator.hasNext();) {
				Field<?> field = iterator.next();

				logger.info("output name:" + binder.getPropertyId(field));

				logger.info("output value:" + field.getValue());
				logger.info("output type:" + field.getType() + "property:" + field.getPropertyDataSource().getType());

				// look out for Integer
				if (field.getPropertyDataSource().getType().equals(Integer.class)) {
					logger.info("we have found integer output");

					result.put((String) binder.getPropertyId(field), integerProperty.getValue());
				} else {
					result.put((String) binder.getPropertyId(field), field.getValue());
				}

			}

			taskService.start(id, "anton");
			taskService.complete(id, "anton", result);

			HumanTaskForm.this.navigator.navigateTo("main" + "/" + TaskListView.NAME);

		}
	}

	public HumanTaskForm() {

	}

	@SuppressWarnings({ "unchecked" })
	public HumanTaskForm(Long taskId, InternalHumanTask newInternalHumanTask, Navigator navigator) {
		this.humanTask = newInternalHumanTask;
		this.taskid = taskId;
		this.navigator = navigator;
		this.taskService = new TaskServiceWrapper();

		// data binding of form
		this.setItemset(new PropertysetItem());
		this.setBinder(new FieldGroup(this.getItemset()));

		Label greeting = new Label("Work on \'" + this.humanTask.getName() + "\' task");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");
		addComponent(greeting);
		setComponentAlignment(greeting, Alignment.TOP_LEFT);

		// special processing when this task is active - we need to display INPUTS to user
		if (this.humanTask.getName().equals(HumanTaskName.SELECT_SERVICE_FROM_SRAMP.toString())) {

			logger.info("Select service from s-ramp human task requires special processing as it has also inputs");
			Map<String, Object> inputs = this.taskService.getTaskContent(taskId);
			List<Service> services = (List<Service>) inputs.get("inServiceList");

			if (services == null) {

				logger.info("services are null");
			} else {

				logger.info(String.valueOf(services.size()));
				logger.info(services.toString());

				logger.info("processing inputs");
				for (Service service : services) {

					logger.info("service uuid:" + service.getUUID());
					logger.info("service name:" + service.getName());
				}

				// load the table with data
				filterTable = buildFilterTable(services);
				filterTable.setPageLength(filterTable.getContainerDataSource().size());
				Label tableGreet = new Label("Services found in S-RAMP Repository");
				tableGreet.setSizeUndefined();
				tableGreet.setStyleName("h3");
				addComponent(tableGreet);
				addComponent(filterTable);
				setComponentAlignment(greeting, Alignment.TOP_LEFT);
				setComponentAlignment(filterTable, Alignment.TOP_LEFT);
				// add table pagination
				PagedFilterControlConfig config = new PagedFilterControlConfig();
				config.setItemsPerPage("Services per page:");
				List<Integer> lengths = new ArrayList<Integer>();
				lengths.add(10);
				lengths.add(20);
				lengths.add(50);
				lengths.add(100);
				lengths.add(250);
				lengths.add(500);
				config.setPageLengthsAndCaptions(lengths);
				HorizontalLayout cfg = filterTable.createControls(config);
				addComponent(cfg);
				setComponentAlignment(cfg, Alignment.TOP_LEFT);

			}

		}

		FormLayout fl = new FormLayout();

		List<HumanTaskOutput> outputs = new ArrayList<HumanTaskOutput>();
		outputs = this.humanTask.getOutputs();

		// go through all outputs for this particular human task, and generate corresponding data output
		for (HumanTaskOutput output : outputs) {

			HumanTaskOutputType type = output.getDataType();

			switch (type) {
			case BOOLEAN: {

				CheckBox checkbox = new CheckBox();
				checkbox.setCaption(output.getLabel());
				checkbox.setRequired(true);
			//	checkbox.setValue(true);
		
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<Boolean>(false));
				this.getBinder().bind(checkbox, output.getOutputIdentifier());
				fl.addComponent(checkbox);
				break;
			}

			case ENUM_STATE: {

				NativeSelect enumSelect = new NativeSelect(output.getLabel());
				enumSelect.setRequired(true);
				enumSelect.setRequiredError("This field is required");

				if (this.getHumanTask().getName().equals(HumanTaskName.REGISTER_EXISTING_SERVICE.toString())) {

					enumSelect.addItems("InTest", "Available", "Deprecated");
				}
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(enumSelect, output.getOutputIdentifier());

				fl.addComponent(enumSelect);

				break;
			}
			case INTEGER: {

				logger.info("adding integer output:" + output.getDataType() + "label:" + output.getLabel());
				TextField integerField = new TextField(output.getLabel());
				integerField.setConverter(Integer.class);
				// integerField.setConverter(new StringToIntegerConverter());
				integerField.addValidator(new IntegerRangeValidator("Value has to be from range 0-65535", 0, 65535));
				integerField.setRequired(true);
				integerField.setRequiredError("This field is required");

				// this.getItemset().addItemProperty(output.getOutputIdentifier(), new
				// ObjectProperty<Integer>(((Integer)8480)));
				this.getItemset().addItemProperty(output.getOutputIdentifier(), integerProperty);
				this.getBinder().bind(integerField, output.getOutputIdentifier());
				fl.addComponent(integerField);
				break;

			}
			case PASSWORD: {

				PasswordField passwordField = new PasswordField(output.getLabel());
				passwordField.setRequired(true);
				passwordField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(passwordField, output.getOutputIdentifier());
				fl.addComponent(passwordField);
			}
				break;
			case STRING: {

				TextField stringField = new TextField(output.getLabel());
				stringField.setRequired(true);
				stringField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(stringField, output.getOutputIdentifier());
				fl.addComponent(stringField);

				if (this.humanTask.getName().equals(HumanTaskName.SELECT_SERVICE_FROM_SRAMP.toString())) {
					this.setUuid(stringField);
				}

				break;
			}

			case TEXT_AREA: {

				TextArea textArea = new TextArea(output.getLabel());
				textArea.setRequired(true);
				textArea.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(textArea, output.getOutputIdentifier());
				fl.addComponent(textArea);
				break;
			}

			default:
				break;

			}

		}
		Button submitButton = new Button("Complete Task");
		submitButton.setData(this.getBinder());
		submitButton.addClickListener(new ButtonListener(TaskDetailView.NAME));
		fl.addComponent(submitButton);

		addComponent(fl);

	}

	private PagedFilterTable<?> buildFilterTable(List<Service> services) {

		PagedFilterTable<IndexedContainer> filterTable = new PagedFilterTable<IndexedContainer>("");
		// filterTable.setSizeFull();
		filterTable.setFilterDecorator(new CustomFilterDecorator());
		filterTable.setFilterGenerator(new CustomFilterGenerator());
		filterTable.setContainerDataSource(buildContainer(services));
		filterTable.setFilterBarVisible(true);
		filterTable.setFilterFieldVisible("Action", false);

		return filterTable;

	}

	@SuppressWarnings("unchecked")
	private Container buildContainer(List<Service> services) {

		IndexedContainer cont = new IndexedContainer();

		cont.addContainerProperty("Service Name", String.class, null);
		cont.addContainerProperty("Service UUID", String.class, null);
		cont.addContainerProperty("Action", Button.class, null);

		int i = 1;

		for (Service service : services) {
			cont.addItem(i);

			cont.getContainerProperty(i, "Service Name").setValue(service.getName());
			cont.getContainerProperty(i, "Service UUID").setValue(service.getUUID());

			Button detailsField = new Button("select");
			detailsField.setData(service.getUUID());
			detailsField.addClickListener(new SelectListener());
			detailsField.addStyleName(BaseTheme.BUTTON_LINK);

			cont.getContainerProperty(i, "Action").setValue(detailsField);

			i++;

		}

		return cont;

	}

	public InternalHumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(InternalHumanTask humanTask) {
		this.humanTask = humanTask;
	}

	public PropertysetItem getItemset() {
		return itemset;
	}

	public void setItemset(PropertysetItem itemset) {
		this.itemset = itemset;
	}

	public FieldGroup getBinder() {
		return binder;
	}

	public void setBinder(FieldGroup binder) {
		this.binder = binder;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public class MyBean {
		private int value;

		public int getValue() {
			return value;
		}

		public void setValue(int integer) {
			value = integer;
		}
	}

	public PagedFilterTable<?> getFilterTable() {
		return filterTable;
	}

	public void setFilterTable(PagedFilterTable<?> filterTable) {
		this.filterTable = filterTable;
	}

	public TextField getUuid() {
		return uuid;
	}

	public void setUuid(TextField uuid) {
		this.uuid = uuid;
	}

}
