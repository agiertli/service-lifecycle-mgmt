package org.fi.muni.diploma.thesis.frontend.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.fi.muni.diploma.thesis.utils.DatabaseUtil;
import org.fi.muni.diploma.thesis.utils.RuntimeEngineWrapper;
import org.fi.muni.diploma.thesis.utils.rtgov.Notification;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class NotificationDetailView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String NAME = "notificationdetail";
	private Navigator navigator;
	private final static Logger logger = Logger.getLogger(NotificationDetailView.class.getName());
	private Notification notification;

	private PropertysetItem itemset;
	private FieldGroup binder;
	private Boolean send;

	private List<Component> formComponents;

	public NotificationDetailView(Navigator navigator, Notification notification) {

		this.navigator = navigator;
		this.notification = notification;

		logger.info("creating notification detail view");
		logger.info("notification received in detail view:" + notification.toString());

		Label greeting = new Label("Work on \'" + this.notification.getService().getName() + "\' notification");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");
		addComponent(greeting);
		setComponentAlignment(greeting, Alignment.TOP_LEFT);

		FormLayout fl = buildForm(notification);

		addComponent(fl);
		setComponentAlignment(fl, Alignment.TOP_LEFT);

	}

	@SuppressWarnings("serial")
	public FormLayout buildForm(Notification notification) {

		FormLayout fl = new FormLayout();
		this.setItemset(new PropertysetItem());
		this.setBinder(new FieldGroup(this.getItemset()));
		this.setFormComponents(new ArrayList<Component>());

		// this is all based on the corresponding process definition, hardcoding it due to small number of fields

		// Send email checkbox
		CheckBox checkbox = new CheckBox("Send Email");
		checkbox.setRequired(true);
		this.getItemset().addItemProperty(checkbox.getCaption(), new ObjectProperty<Boolean>(false));
		this.getBinder().bind(checkbox, checkbox.getCaption());
		fl.addComponent(checkbox);
		fl.setComponentAlignment(checkbox, Alignment.TOP_LEFT);

		checkbox.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				for (Component c : NotificationDetailView.this.getFormComponents()) {

					NotificationDetailView.this.send = (Boolean) event.getProperty().getValue();
					c.setEnabled((boolean) event.getProperty().getValue());
					if (c instanceof TextField) {

						((TextField) c).setRequired((boolean) event.getProperty().getValue());
					} else if (c instanceof TextArea) {
						((TextArea) c).setRequired((boolean) event.getProperty().getValue());
					}

				}

			}

		});

		// TO email address
		TextField toField = new TextField("To:");
		toField.setRequired(false);
		toField.setRequiredError("This field is required");
		this.getItemset().addItemProperty(toField.getCaption(), new ObjectProperty<String>(""));
		this.getBinder().bind(toField, toField.getCaption());
		fl.addComponent(toField);
		fl.setComponentAlignment(toField, Alignment.TOP_LEFT);
		this.getFormComponents().add(toField);
		toField.setEnabled(false);

		// FROM email address
		TextField fromField = new TextField("From:");
		fromField.setRequired(false);
		fromField.setRequiredError("This field is required");
		this.getItemset().addItemProperty(fromField.getCaption(), new ObjectProperty<String>(""));
		this.getBinder().bind(fromField, fromField.getCaption());
		fl.addComponent(fromField);
		fl.setComponentAlignment(fromField, Alignment.TOP_LEFT);
		this.getFormComponents().add(fromField);
		fromField.setEnabled(false);

		// SUBJECT field
		TextField subjectField = new TextField("Subject:");
		subjectField.setRequired(false);
		subjectField.setRequiredError("This field is required");
		this.getItemset().addItemProperty(subjectField.getCaption(), new ObjectProperty<String>(""));
		this.getBinder().bind(subjectField, subjectField.getCaption());
		fl.addComponent(subjectField);
		fl.setComponentAlignment(subjectField, Alignment.TOP_LEFT);
		this.getFormComponents().add(subjectField);
		subjectField.setEnabled(false);

		// BODY field
		TextArea bodyField = new TextArea("Body:");
		bodyField.setRequired(false);
		bodyField.setRequiredError("This field is required");
		this.getItemset().addItemProperty(bodyField.getCaption(), new ObjectProperty<String>(""));
		this.getBinder().bind(bodyField, bodyField.getCaption());
		fl.addComponent(bodyField);
		fl.setComponentAlignment(bodyField, Alignment.TOP_LEFT);
		this.getFormComponents().add(bodyField);
		bodyField.setWidth("400px");
		bodyField.setHeight("200px");

		String defaultValue = "-------------" + "\nInvocation details:" + "\nService Name:" + notification.getService().getName() + ""
				+ "\nRetirement date:" + new Date(notification.getService().getRetirementTimestamp()) + "\nInvocation date:"
				+ new Date(notification.getInvocationTimestamp()) + "\nInterface name:" + notification.getInterfaceName() + "\nOperation name:"
				+ notification.getOperation();
		bodyField.setValue(defaultValue);
		bodyField.setEnabled(false);
		this.getFormComponents().add(bodyField);

		Button submitButton = new Button("Process notification");
		submitButton.setData(notification);
		submitButton.addClickListener(new ButtonListener(NotificationView.NAME));
		fl.addComponent(submitButton);

		Label note = new Label(
				"Submitting the form without checking the \"Send Email\" box will result in notification being processed anyway i.e. the notification won't show up in the list again, and the email won't be sent");
		// note.setWidth("200px");
		note.addStyleName("h4");
		fl.addComponent(note);
		fl.setComponentAlignment(note, Alignment.TOP_LEFT);

		return fl;

	}

	public class ButtonListener implements ClickListener {

		private String menuitem;

		public ButtonListener(String menuitem) {

			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			logger.info("button clicked");
			Notification notification = (Notification) event.getButton().getData();

			Map<String, Object> params = new HashMap<String, Object>();
			if (send == null) {

				params.put("p_send", false);
			} else {
				params.put("p_send", send);
			}

			if (send != null) {
				if (send) {
					for (Component c : NotificationDetailView.this.getFormComponents()) {

						// only one text area and that's body
						if (c instanceof TextArea) {

							logger.info("body:" + ((TextArea) c).getValue());
							params.put("p_body", ((TextArea) c).getValue());
						}

						// process remaining text fields
						else if (c instanceof TextField) {

							if (c.getCaption().toLowerCase().contains("to")) {

								logger.info("to:" + ((TextField) c).getValue());
								params.put("p_to", ((TextField) c).getValue());
							} else if (c.getCaption().toLowerCase().contains("subject")) {

								logger.info("subject:" + ((TextField) c).getValue());
								params.put("p_subject", ((TextField) c).getValue());
							} else if (c.getCaption().toLowerCase().contains("from")) {

								logger.info("from:" + ((TextField) c).getValue());
								params.put("p_from", ((TextField) c).getValue());
							}
						}

					}
				}
			}

			// start the notification bpm process
			RuntimeEngineWrapper.getEngine().getKieSession().startProcess(RuntimeEngineWrapper.getProperties().getProcessIdNotification(), params);

			com.vaadin.ui.Notification notif = new com.vaadin.ui.Notification("Notification acknowledged",
					com.vaadin.ui.Notification.Type.HUMANIZED_MESSAGE);

			// Customize it
			notif.setDelayMsec(2500);
			notif.setPosition(Position.MIDDLE_CENTER);

	

			// so many things can go wrong :(
			// insert processed record into the database
			try {
				DatabaseUtil dbUtil = new DatabaseUtil();
				dbUtil.insertProcessedNotification(notification.getInvocationTimestamp());
				dbUtil.close();
			} catch (IOException | NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Show it in the page
			NotificationDetailView.this.navigator.navigateTo("main" + "/" + menuitem);
			notif.show(Page.getCurrent());
			
		}

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
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

	public List<Component> getFormComponents() {
		return formComponents;
	}

	public void setFormComponents(List<Component> formComponents) {
		this.formComponents = formComponents;
	}

}
