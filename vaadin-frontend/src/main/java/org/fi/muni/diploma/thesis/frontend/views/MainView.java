package org.fi.muni.diploma.thesis.frontend.views;

import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.rtgov.Notification;
import org.fi.muni.diploma.thesis.utils.rtgov.RetiredService;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/** Main view with a menu */
public class MainView extends VerticalLayout implements View {

	private static final long serialVersionUID = -3398565663865641952L;
	private final static Logger logger = Logger.getLogger(MainView.class.getName());

	public static String NAME = "main";

	// list of components on the page
	private Panel panel;
	private Navigator navigator;
	private Button startButton;
	private Button instances;
	private Button tasks;
	private Button notifications;

	private Label greeting;

	// Menu navigation button listener
	class ButtonListener implements Button.ClickListener {
		private static final long serialVersionUID = -4941184695301907995L;

		String menuitem;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			MainView.this.navigator.navigateTo("main" + "/" + menuitem);
			logger.info("main view button click");
		}
	}

	public MainView(Navigator navigator) {

		logger.info("constructor of MainView Called");

		this.setNavigator(navigator);

		setSizeFull();

		// Layout with menu on left and view area on right
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setSizeFull();

		// Have a menu on the left side of the screen
		Panel menu = new Panel("Menu");
		menu.setHeight("100%");
		menu.setWidth(null);
		VerticalLayout menuContent = new VerticalLayout();

		startButton = new Button("Start New Lifecycle Instance", new ButtonListener(StartProcessView.NAME));
		startButton.setWidth("179px");

		instances = new Button("Lifecycle Instances", new ButtonListener(ProcessListView.NAME));
		instances.setWidth("179px");

		tasks = new Button("Lifecycle Tasks", new ButtonListener(TaskListView.NAME));
		tasks.setWidth("179px");

		notifications = new Button("Notification Actions", new ButtonListener("notificationactions"));
		notifications.setWidth("179px");

		menuContent.addComponent(startButton);
		menuContent.addComponent(instances);
		menuContent.addComponent(tasks);
		menuContent.addComponent(notifications);
		menuContent.setWidth(null);
		menuContent.setMargin(true);
		menu.setContent(menuContent);
		hLayout.addComponent(menu);

		// A panel that contains a content area on right
		panel = new Panel("");
		VerticalLayout labelHolder = new VerticalLayout();

		panel.setSizeFull();
		panel.setContent(labelHolder);

		hLayout.addComponent(panel);
		hLayout.setExpandRatio(panel, 1.0f);

		addComponent(hLayout);
		setExpandRatio(hLayout, 1.0f);

		// Allow going back to the start
		Button logout = new Button("Logout", new Button.ClickListener() {
			private static final long serialVersionUID = -1809072471885383781L;

			@Override
			public void buttonClick(ClickEvent event) {
				MainView.this.getNavigator().navigateTo("");
			}
		});
		addComponent(logout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
	//	logger.info("enter method called");
		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setSizeFull();
		panelContent.setMargin(true);

		panel.setContent(panelContent); // Also clear

		if (event.getParameters() == null || event.getParameters().isEmpty()) {
			greeting = new Label("Welcome to Service Lifecycle Management Application");
			greeting.setSizeUndefined();
			greeting.setStyleName("h2");

			panelContent.addComponent(greeting);
			panelContent.setComponentAlignment(greeting, Alignment.MIDDLE_CENTER);

			return;
		}

		// Redirect to Start Process View
		if (event.getParameters().equalsIgnoreCase(StartProcessView.NAME)) {

			panelContent.addComponent(new StartProcessView(this.getNavigator()));

		}

		// Redirect to Process List View
		else if (event.getParameters().equalsIgnoreCase(ProcessListView.NAME)) {

			panelContent.addComponent(new ProcessListView(this.getNavigator()));
			return;

		}

		// Redirect to Task List View
		else if (event.getParameters().equalsIgnoreCase(TaskListView.NAME)) {

			panelContent.addComponent(new TaskListView(this.getNavigator()));
			return;

			// Redirect to Notification Action view
		} else if (event.getParameters().equalsIgnoreCase(NotificationView.NAME)) {

			try {
				panelContent.addComponent(new NotificationView(this.getNavigator()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// todo - add Notification Actions view
			return;

		}

		// Redirect to Lifecycle Detail view
		else if (event.getParameters().contains((ProcessDetailView.NAME.toLowerCase()))) {

			// parse the id=X parameter
			Long processInstanceId = Long.valueOf(event.getParameters().substring(event.getParameters().indexOf("id=") + 3));

			panelContent.addComponent(new ProcessDetailView(processInstanceId, this.getNavigator()));

		}

		// Redirect to Task Detail View
		else if (event.getParameters().contains(TaskDetailView.NAME.toLowerCase())) {

			Long taskId = Long.valueOf(event.getParameters().substring(event.getParameters().indexOf("id=") + 3));

			panelContent.addComponent(new TaskDetailView(taskId, this.getNavigator()));

		}

		// Redirect to Notification Detail View
		else if (event.getParameters().contains(NotificationDetailView.NAME.toLowerCase())) {

			Notification notification = (Notification) getUI().getSession().getAttribute("notification");
			
			if (notification == null) {
				
				Label notFound = new Label("Unknown notification. Please don't try to hack me anymore");
				addComponent(notFound);
				panelContent.addComponent(notFound);
				panelContent.setComponentAlignment(notFound, Alignment.TOP_LEFT);
				return;
			}
			
			getUI().getSession().setAttribute("notification", null); // clear the value once we don't need it

			panelContent.addComponent(new NotificationDetailView(this.getNavigator(), notification));

		}

		return;

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
}