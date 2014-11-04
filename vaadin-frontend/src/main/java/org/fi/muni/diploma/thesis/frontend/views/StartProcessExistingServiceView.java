package org.fi.muni.diploma.thesis.frontend.views;

import java.util.logging.Logger;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class StartProcessExistingServiceView extends VerticalLayout implements View {

	private static final long serialVersionUID = -5662834179069381965L;
	private Navigator navigator;
	private final static Logger logger = Logger.getLogger(StartProcessExistingServiceView.class.getName());

	private PropertysetItem itemset;
	private FieldGroup binder;
	private Label exceptionLabel;
	public static final String NAME = "existingservice";

	public StartProcessExistingServiceView(Navigator navigator) {

		this.navigator = navigator;

		Label greeting = new Label("Start a new Lifecycle -  for existing Service");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");
		addComponent(greeting);
		StartProcessExistingServiceView.this.setComponentAlignment(greeting, Alignment.TOP_CENTER);

		// data binding of form
		this.setItemset(new PropertysetItem());
		this.setBinder(new FieldGroup(this.getItemset()));

		// A FormLayout used outside the context of a Form
		FormLayout fl = new FormLayout();
		fl.setCaption("Enter required data, such as where the service is deployed and its description");

		// Form has to include hostname, port, username, password, service state and service description
		TextField hostname = new TextField("Hostname");
		hostname.setRequired(true);
		hostname.setRequiredError("Hostname is required");

		TextField port = new TextField("Port");
		port.setConverter(new StringToIntegerConverter());
		port.addValidator(new IntegerRangeValidator("Port has to be from internal 0-65535", 0, 65535));
		port.setRequired(true);
		port.setRequiredError("Port is required");

		TextField username = new TextField("Username");
		username.setRequired(true);
		username.setRequiredError("Username is required");

		PasswordField password = new PasswordField("Password");
		password.setRequired(true);
		password.setRequiredError("Password is required");

		TextArea serviceDescription = new TextArea("Service Description");
		serviceDescription.setRequired(true);
		serviceDescription.setRequiredError("Service description is required");

		NativeSelect serviceState = new NativeSelect("Service State");
		serviceState.setRequired(true);
		serviceState.setRequiredError("Service State is required");
		serviceState.addItems("ServiceIdentification", "InitialService", "Registered", "InTest", "Available",
				"Deprecated", "Retired");

		// bind all components

		this.getItemset().addItemProperty("hostname", new ObjectProperty<String>(""));
		this.getItemset().addItemProperty("port", new ObjectProperty<Integer>(0));
		this.getItemset().addItemProperty("username", new ObjectProperty<String>(""));
		this.getItemset().addItemProperty("password", new ObjectProperty<String>(""));
		this.getItemset().addItemProperty("serviceDescription", new ObjectProperty<String>(""));
		this.getItemset().addItemProperty("serviceState", new ObjectProperty<String>(""));

		this.getBinder().bind(hostname, "hostname");
		this.getBinder().bind(port, "port");
		this.getBinder().bind(username, "username");
		this.getBinder().bind(password, "password");
		this.getBinder().bind(serviceDescription, "serviceDescription");
		this.getBinder().bind(serviceState, "serviceState");

		// submit button, includes all the inputted values
		Button submitButton = new Button("Start Lifecycle");
		submitButton.setData(this.getBinder());
		submitButton.addClickListener(new ButtonListener());

		// add all inputs to the Form Layout

		fl.addComponent(hostname);
		fl.addComponent(port);
		fl.addComponent(username);
		fl.addComponent(password);
		fl.addComponent(serviceDescription);
		fl.addComponent(serviceState);
		fl.addComponent(submitButton);

		// add Form to the output
		addComponent(fl);

	}

	class ButtonListener implements Button.ClickListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7039085687422184176L;

		@Override
		public void buttonClick(ClickEvent event) {

			Boolean exception = false;
			logger.info("form submitted, the inputted data are:");
			
			if (StartProcessExistingServiceView.this.getExceptionLabel() != null) {
				
				removeComponent(StartProcessExistingServiceView.this.getExceptionLabel());
				
			}
			
			

			try {
				StartProcessExistingServiceView.this.getBinder().commit();
			} catch (CommitException e) {

				logger.info(e.toString());
				logger.info(e.getCause().toString());
				if (e.getCause().toString().contains("EmptyValueException")) {
					StartProcessExistingServiceView.this.setExceptionLabel(new Label("Fill in all required inputs!"));
					addComponent(StartProcessExistingServiceView.this.getExceptionLabel());
					

				}
				
				if (e.getCause().toString().contains("InvalidValueException")) {
					
					StartProcessExistingServiceView.this.setExceptionLabel(new Label("Port must be number from 0-65535!"));
					addComponent(StartProcessExistingServiceView.this.getExceptionLabel());
				}
				
				exception = true;
				return;

			}

			FieldGroup fieldGroup = (FieldGroup) event.getButton().getData();
			com.vaadin.ui.Field<String> hostname = (com.vaadin.ui.Field<String>) fieldGroup.getField("hostname");
			logger.info("hostname:" + hostname.getValue());

			Field<String> serviceState = (Field<String>) fieldGroup.getField("serviceState");
			logger.info("service state:" + serviceState.getValue());

			if (serviceState.getValue().isEmpty() && !exception) {
				logger.info("empty service state");
				StartProcessExistingServiceView.this.setExceptionLabel(new Label("Fill in all required inputs!"));
				addComponent(StartProcessExistingServiceView.this.getExceptionLabel());
				exception = false;
				return;
			}

		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// no need

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
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

	public Label getExceptionLabel() {
		return exceptionLabel;
	}

	public void setExceptionLabel(Label exceptionLabel) {
		this.exceptionLabel = exceptionLabel;
	}

}
