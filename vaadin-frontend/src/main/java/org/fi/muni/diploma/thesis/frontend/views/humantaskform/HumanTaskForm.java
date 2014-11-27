package org.fi.muni.diploma.thesis.frontend.views.humantaskform;

import java.io.IOException;
import java.net.URL;
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
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskName;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskOutput;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskOutputType;
import org.fi.muni.diploma.thesis.utils.humantask.InternalHumanTask;
import org.fi.muni.diploma.thesis.utils.jbpm.RuntimeEngineWrapper;
import org.fi.muni.diploma.thesis.utils.jbpm.TaskServiceWrapper;
import org.kie.api.task.TaskService;
import org.tepi.filtertable.paged.PagedFilterControlConfig;
import org.tepi.filtertable.paged.PagedFilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("unused")
/**
 * Human Task form which is calculated from the number / types / names of the Human Task Inputs and Outputs
 * @author osiris
 *
 */
public class HumanTaskForm extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private InternalHumanTask humanTask;
	private PropertysetItem itemset;
	private FieldGroup binder;
	private Long taskid;
	private static final Logger logger = Logger.getLogger(HumanTaskForm.class.getName());
	private TaskService taskService;
	public Navigator navigator;
	private TextField uuid;
	private TextField serviceName;

	// for presenting found services in S-RAMP repository
	private PagedFilterTable<?> filterTable;
	private Label errorLabel;
	private Label srampErrorLabel;
	private Label portError;
	private TextField portField;
	private CheckBox emailCheckbox;
	private String taskUser;
	private final Label verticalGap = new Label("</br>", ContentMode.HTML);
	private final Label horizontalGap = new Label("&nbsp;", ContentMode.HTML);

	private CheckBox policyFulfilled;
	private CheckBox serviceCreated;
	private Button completeButton;
	private Boolean submitable;

	private Button refreshServicesButton;

	public class SelectListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			System.out.println("select listener");

			Service service = (Service) event.getButton().getData();
			// copy the selected values to the input fields below /we have to remove readonly restriction first, once
			// done, put it back
			HumanTaskForm.this.getUuid().setEnabled(true);
			HumanTaskForm.this.getUuid().setValue(service.getUUID());

			HumanTaskForm.this.getServiceName().setEnabled(true);
			HumanTaskForm.this.getServiceName().setValue(service.getName());

			HumanTaskForm.this.getUuid().setEnabled(false);
			HumanTaskForm.this.getServiceName().setEnabled(false);

		}

	}

	public class ButtonListener implements ClickListener {

		private static final long serialVersionUID = 1L;

		String menuitem;
		Boolean copy;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			if (HumanTaskForm.this.portField != null) {
				Integer portValue = null;
				try {
				portValue = Integer.valueOf(portField.getValue());
				} catch (NumberFormatException e) {
					
					HumanTaskForm.this.removeComponents(HumanTaskForm.this.errorLabel, HumanTaskForm.this.portError,
							HumanTaskForm.this.srampErrorLabel);

					HumanTaskForm.this.errorLabel = new Label("Fill in valid port value - integer number from 0 to 65535");
					addComponent(errorLabel);
					setComponentAlignment(errorLabel, Alignment.BOTTOM_LEFT);
					return; // end the processing
				}
				
			}

			String hostname = "";
			String password = "";
			String username = "";
			Integer port = 0;

			taskService = new TaskServiceWrapper();

			FieldGroup data = (FieldGroup) event.getButton().getData();
			try {
				data.commit();
			} catch (CommitException e) {
				e.printStackTrace();

				if (e.getCause().toString().contains("EmptyValueException")) {
					HumanTaskForm.this.removeComponents(HumanTaskForm.this.errorLabel, HumanTaskForm.this.portError,
							HumanTaskForm.this.srampErrorLabel);

					HumanTaskForm.this.errorLabel = new Label("Fill in all the required values");
					addComponent(errorLabel);
					setComponentAlignment(errorLabel, Alignment.BOTTOM_LEFT);
					return; // end the processing
				}

			}
			Long id = HumanTaskForm.this.getTaskid();

			Map<String, Object> result = new HashMap<String, Object>();

			Collection<Field<?>> col = data.getFields();
			//
			for (Iterator<Field<?>> iterator = col.iterator(); iterator.hasNext();) {
				Field<?> field = iterator.next();

				// look out for Integer
				if (binder.getPropertyId(field).toString().toLowerCase().contains("port")) {

					Integer portValue = null;
					try {
					portValue = Integer.valueOf(portField.getValue());
					} catch (NumberFormatException e) {
						
						HumanTaskForm.this.removeComponents(HumanTaskForm.this.errorLabel, HumanTaskForm.this.portError,
								HumanTaskForm.this.srampErrorLabel);

						HumanTaskForm.this.errorLabel = new Label("Fill in valid port value - integer number from 0 to 65535");
						addComponent(errorLabel);
						setComponentAlignment(errorLabel, Alignment.BOTTOM_LEFT);
						return; // end the processing
					}
					
					if (portValue > 65535) {
						
						HumanTaskForm.this.removeComponents(HumanTaskForm.this.errorLabel, HumanTaskForm.this.portError,
								HumanTaskForm.this.srampErrorLabel);

						HumanTaskForm.this.errorLabel = new Label("Fill in valid port value - integer number from 0 to 65535");
						addComponent(errorLabel);
						setComponentAlignment(errorLabel, Alignment.BOTTOM_LEFT);
						return; // end the processing
						
					}
					
					result.put((String) binder.getPropertyId(field), portValue);
				} else {
					result.put((String) binder.getPropertyId(field), field.getValue());
				}

				// we want to do some additional checking when the human task is register, as we need to pass
				// VALID data for http connection
				if (humanTask.getName().equals(HumanTaskName.REGISTER.toString())
						|| humanTask.getName().equals(HumanTaskName.REGISTER_EXISTING_SERVICE.toString())) {

					if (((String) binder.getPropertyId(field)).toLowerCase().contains("host")) {

						hostname = (String) field.getValue();
					} else if (((String) binder.getPropertyId(field)).toLowerCase().contains("pass")) {

						password = (String) field.getValue();
					} else if (((String) binder.getPropertyId(field)).toLowerCase().contains("user")) {

						username = (String) field.getValue();
					} else if (((String) binder.getPropertyId(field)).toLowerCase().contains("port")) {

						port = Integer.valueOf(portField.getValue());
					}

				}

			}

			logger.info("now the validation with s-ramp client will happen");
			logger.info(hostname + "+" + username + "+" + password + "+" + port);

			/**
			 * verify s-ramp connection, if not successful don't even try to complete the task!
			 */

			if (HumanTaskForm.this.humanTask.getName().equals(HumanTaskName.REGISTER.toString())
					|| humanTask.getName().equals(HumanTaskName.REGISTER_EXISTING_SERVICE.toString())) {

				try {
					HumanTaskForm.this.verifySrampConnection(username, password, hostname, port);
				} catch (Exception e) {

					e.printStackTrace();
					HumanTaskForm.this.removeComponents(HumanTaskForm.this.errorLabel, HumanTaskForm.this.portError,
							HumanTaskForm.this.srampErrorLabel);

					HumanTaskForm.this.srampErrorLabel = new Label(
							"Connection not established successfully! Please enter valid S-RAMP connection details");
					addComponent(HumanTaskForm.this.srampErrorLabel);
					setComponentAlignment(HumanTaskForm.this.srampErrorLabel, Alignment.BOTTOM_LEFT);
					return;
				}
			}

			taskService.start(id, HumanTaskForm.this.taskUser);
			taskService.complete(id, HumanTaskForm.this.taskUser, result);

			HumanTaskForm.this.navigator.navigateTo("main" + "/" + TaskListView.NAME);

		}
	}

	public HumanTaskForm() {

	}

	/**
	 * remove compoments from the view
	 * 
	 * @param components
	 */
	public void removeComponents(Component... components) {

		for (Component c : components) {

			if (c != null) {

				removeComponent(c);
			}
		}

	}

	@SuppressWarnings({ "unchecked" })
	public HumanTaskForm(Long taskId, String username, InternalHumanTask newInternalHumanTask, Navigator navigator) {
		this.humanTask = newInternalHumanTask;
		this.taskid = taskId;
		this.navigator = navigator;
		this.taskService = new TaskServiceWrapper();
		this.taskUser = username;

		// data binding of form
		this.setItemset(new PropertysetItem());
		this.setBinder(new FieldGroup(this.getItemset()));

		Label greeting = new Label("Work on \'" + this.humanTask.getName() + "\' task");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");
		addComponent(greeting);
		setComponentAlignment(greeting, Alignment.TOP_LEFT);

		// add task description
		String description = this.taskService.getTaskById(this.taskid).getDescription();

		if (!description.isEmpty()) {
			addComponent(verticalGap);
			Label descriptionContent = new Label(description);
			descriptionContent.setWidth("550px");
			addComponent(descriptionContent);
			setComponentAlignment(descriptionContent, Alignment.TOP_LEFT);
		}

		// special processing when this task is active - we need to display INPUTS to user
		if (this.humanTask.getName().equals(HumanTaskName.SELECT_SERVICE_FROM_SRAMP.toString())) {

			logger.info("Select service from s-ramp human task requires special processing as it has also inputs");
			Map<String, Object> inputs = this.taskService.getTaskContent(taskId);
			List<Service> services = (List<Service>) inputs.get("inServiceList");

			if (services == null) {

			} else {

				// load the table with data
				filterTable = buildFilterTable(services);
				filterTable.setPageLength(filterTable.getContainerDataSource().size());
				Label tableGreet = new Label("Services found in S-RAMP Repository");
				tableGreet.setSizeUndefined();
				filterTable.setHeight(String.valueOf(43 * filterTable.getContainerDataSource().size() + 80) + "px");
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
				setSpacing(true);
				addComponent(verticalGap);
				addComponent(cfg);
				setComponentAlignment(cfg, Alignment.TOP_LEFT);

			}

			this.refreshServicesButton = new Button("Refresh service list");
			this.refreshServicesButton.setData(this.taskid);
			this.refreshServicesButton.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					Long taskId = (Long) event.getButton().getData();
					Long pid = taskService.getTaskById(taskId).getTaskData().getProcessInstanceId();
					RuntimeEngineWrapper.getEngine().getKieSession().signalEvent("refreshEvent", null, pid);

					/*
					 * redirect to the latest created task instance in this process instances (refresh canceld the
					 * current HT and created new one
					 */

					int numberOfTasks = taskService.getTasksByProcessInstanceId(pid).size();

					HumanTaskForm.this.navigator.navigateTo("main" + "/" + TaskDetailView.NAME + "?id="
							+ String.valueOf(taskService.getTasksByProcessInstanceId(pid).get(numberOfTasks - 1)));

				}

			});

		}

		VerticalLayout fl = new VerticalLayout();
		fl.setSpacing(true);

		List<HumanTaskOutput> outputs = new ArrayList<HumanTaskOutput>();
		outputs = this.humanTask.getOutputs();

		// go through all outputs for this particular human task, and generate corresponding data output
		for (HumanTaskOutput output : outputs) {

			HumanTaskOutputType type = output.getDataType();

			switch (type) {
			case BOOLEAN: {

				HorizontalLayout checkboxLayout = new HorizontalLayout();
				Label caption = new Label();
				caption.setValue(output.getLabel());
				// checkboxLayout.setCaption(output.getLabel());
				// Label checkboxLabel = new Label(;
				CheckBox checkbox = new CheckBox();
				// checkbox.setCaption(output.getLabel());
				checkbox.setRequired(true);

				// for enabling/disabling fields
				if (output.getLabel().toLowerCase().contains("email") || output.getLabel().toLowerCase().contains("inform")) {

					checkbox.addValueChangeListener(new ValueChangeListener() {

						private static final long serialVersionUID = 4402356351728724065L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							// logger.info("change");

							Iterator<Component> iterate = HumanTaskForm.this.iterator();

							while (iterate.hasNext()) {

								Component c = iterate.next();
								if (c instanceof VerticalLayout) {

									Iterator<Component> formIterator = ((VerticalLayout) c).iterator();

									while (formIterator.hasNext()) {

										Component b = formIterator.next();
										if (b instanceof TextField) {

											b.setEnabled((Boolean) event.getProperty().getValue());
											((TextField) b).setRequired((Boolean) event.getProperty().getValue());
										} else if (b instanceof TextArea) {

											b.setEnabled((Boolean) event.getProperty().getValue());
											((TextArea) b).setRequired((Boolean) event.getProperty().getValue());
										}
									}
								}

							}

						}

					});
				}

				// special handling for REGISTER Task
				if (output.getLabel().toLowerCase().contains("match the policy")) {
					this.policyFulfilled = checkbox;
					this.policyFulfilled.addValueChangeListener(new ValueChangeListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {
							if (HumanTaskForm.this.policyFulfilled.getValue() && HumanTaskForm.this.serviceCreated.getValue()) {

								completeButton.setEnabled(true);
							} else
								completeButton.setEnabled(false);

						}
					});

				}

				if (output.getLabel().toLowerCase().contains("service been created")) {
					this.serviceCreated = checkbox;
					this.serviceCreated.addValueChangeListener(new ValueChangeListener() {

						private static final long serialVersionUID = 1L;

						@Override
						public void valueChange(ValueChangeEvent event) {

							if (HumanTaskForm.this.policyFulfilled.getValue() && HumanTaskForm.this.serviceCreated.getValue()) {

								completeButton.setEnabled(true);
							} else
								completeButton.setEnabled(false);

						}
					});

				}

				// checkbox.setValue(true);
				this.emailCheckbox = checkbox;
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<Boolean>(false));
				this.getBinder().bind(checkbox, output.getOutputIdentifier());

				// checkboxLayout.addComponent(checkboxLabel);
				checkboxLayout.addComponent(horizontalGap);
				checkboxLayout.setSpacing(true);
				checkboxLayout.addComponent(caption);
				checkboxLayout.addComponent(horizontalGap);
				checkboxLayout.addComponent(checkbox);
				// checkboxLayout.setComponentAlignment(checkboxLabel, Alignment.TOP_LEFT);
				checkboxLayout.setComponentAlignment(checkbox, Alignment.TOP_LEFT);
				fl.addComponent(checkboxLayout);
				fl.setComponentAlignment(checkboxLayout, Alignment.TOP_LEFT);
				fl.addComponent(verticalGap);
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
				fl.setComponentAlignment(enumSelect, Alignment.TOP_LEFT);
				fl.addComponent(verticalGap);

				break;
			}
			case INTEGER: {

				// logger.info("adding integer output:" + output.getDataType() + "label:" + output.getLabel());
				TextField integerField = new TextField(output.getLabel());
				integerField.setRequired(true);
				integerField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(integerField, output.getOutputIdentifier());
				fl.addComponent(integerField);
				fl.setComponentAlignment(integerField, Alignment.TOP_LEFT);
				fl.addComponent(verticalGap);
				this.portField = integerField;
				break;

			}
			case PASSWORD: {

				PasswordField passwordField = new PasswordField(output.getLabel());
				passwordField.setRequired(true);
				passwordField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(passwordField, output.getOutputIdentifier());
				fl.addComponent(passwordField);
				fl.setComponentAlignment(passwordField, Alignment.TOP_LEFT);
				fl.addComponent(verticalGap);
			}
				break;
			case STRING: {

				TextField stringField = new TextField(output.getLabel());

				stringField.setRequired(true);
				stringField.setEnabled(true);

				stringField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(stringField, output.getOutputIdentifier());

				// email is not mandatory
				if (this.getHumanTask().getName().equals(HumanTaskName.EVALUATE_TEST_RESULTS.toString()) ||

				this.getHumanTask().getName().equals(HumanTaskName.DEPRECATE_SERVICE.toString())
						|| this.getHumanTask().getName().equals(HumanTaskName.RETIRE_SERVICE.toString())) {

					stringField.setRequired(false);
					stringField.setEnabled(false);
				}

				fl.addComponent(stringField);
				fl.setComponentAlignment(stringField, Alignment.TOP_LEFT);
				fl.addComponent(verticalGap);

				// we need to save the outputs of select service task explicitly due to "SELECT" functionality
				if (this.humanTask.getName().equals(HumanTaskName.SELECT_SERVICE_FROM_SRAMP.toString())) {
					stringField.setEnabled(false); // user can't edit this

					if (output.getOutputIdentifier().toLowerCase().contains("uuid")) {
						this.setUuid(stringField);
					} else if (output.getOutputIdentifier().toLowerCase().contains("name")) {
						this.setServiceName(stringField);
					}
				}

				break;
			}

			case TEXT_AREA: {

				TextArea textArea = new TextArea(output.getLabel());

				textArea.setRequired(true);
				textArea.setEnabled(true);

				textArea.setRequiredError("This field is required");

				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));

				textArea.setInputPrompt("Enter some description or URL pointing to the resource");
				this.getBinder().bind(textArea, output.getOutputIdentifier());

				if (this.getHumanTask().getName().equals(HumanTaskName.EVALUATE_TEST_RESULTS.toString()) ||

				this.getHumanTask().getName().equals(HumanTaskName.DEPRECATE_SERVICE.toString())
						|| this.getHumanTask().getName().equals(HumanTaskName.RETIRE_SERVICE.toString())) {

					textArea.setRequired(false);
					textArea.setEnabled(false);
				}
				fl.addComponent(textArea);
				fl.addComponent(verticalGap);
				fl.setComponentAlignment(textArea, Alignment.TOP_LEFT);

				break;
			}

			default:
				break;

			}

		}
		fl.addComponent(verticalGap);
		HorizontalLayout buttonLayout = new HorizontalLayout();

		Button submitButton = new Button("Complete Task");
		this.completeButton = submitButton;

		// if we are in register task, disable the complete button by default
		// enable it only if both checkboxes are checked
		if (this.policyFulfilled != null) {
			this.completeButton.setEnabled(false);
		}

		submitButton.setData(this.getBinder());
		submitButton.addClickListener(new ButtonListener(TaskDetailView.NAME));
		buttonLayout.addComponent(submitButton);
		buttonLayout.setComponentAlignment(submitButton, Alignment.TOP_LEFT);

		if (this.refreshServicesButton != null) {
			buttonLayout.addComponent(this.horizontalGap);
			buttonLayout.addComponent(this.horizontalGap);
			buttonLayout.addComponent(this.refreshServicesButton);
			fl.addComponent(buttonLayout);
			fl.setComponentAlignment(buttonLayout, Alignment.TOP_LEFT);
			fl.addComponent(verticalGap);
		} else {
			fl.addComponent(submitButton);
			fl.setComponentAlignment(submitButton, Alignment.TOP_LEFT);
			fl.addComponent(verticalGap);

		}

		addComponent(this.verticalGap);
		addComponent(fl);

	}

	private PagedFilterTable<?> buildFilterTable(List<Service> services) {

		PagedFilterTable<IndexedContainer> filterTable = new PagedFilterTable<IndexedContainer>("");
		// filterTable.setSizeFull();
		filterTable.setSizeUndefined();
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
			detailsField.setData(service);
			detailsField.addClickListener(new SelectListener());
			detailsField.addStyleName(BaseTheme.BUTTON_LINK);

			cont.getContainerProperty(i, "Action").setValue(detailsField);

			i++;

		}

		return cont;

	}

	/**
	 * 'Ping' the S-RAMP server and checks whether the connection was established successfully
	 * @param username
	 * @param password
	 * @param hostname
	 * @param port
	 * @throws Exception
	 */
	private void verifySrampConnection(String username, String password, String hostname, Integer port) throws Exception {

		try {
			URL endpoint = new URL("http://" + hostname + ":" + port.toString() + "/s-ramp-server/s-ramp/servicedocument");
			java.net.HttpURLConnection connection = (java.net.HttpURLConnection) endpoint.openConnection();
			String userPassword = username + ":" + password;
			String encoding = org.apache.commons.codec.binary.Base64.encodeBase64String(userPassword.getBytes());
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.getInputStream();
		} catch (IOException e) {

			throw e; // propagating this further
		}

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

	public TextField getServiceName() {
		return serviceName;
	}

	public void setServiceName(TextField serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
