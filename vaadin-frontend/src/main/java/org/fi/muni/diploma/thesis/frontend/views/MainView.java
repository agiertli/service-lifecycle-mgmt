package org.fi.muni.diploma.thesis.frontend.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.properties.ApplicationUserRoleProperties;
import org.fi.muni.diploma.thesis.utils.rtgov.Notification;
import org.vaadin.alump.fancylayouts.FancyImage;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Main navigation menu - includes left navigation button and right panel with actual content
 * 
 * @author osiris
 * 
 */
@SuppressWarnings("unused")
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
	private FancyImage fancyImage;

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

			MainView.this.navigator.navigateTo(MainView.NAME + "/" + menuitem);
		}
	}

	public MainView(Navigator navigator) {

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
		menuContent.setSpacing(true);
		menuContent.addStyleName("left-allign-button-caption");

		startButton = new Button("Start New Lifecycle Instance", new ButtonListener(StartProcessView.NAME));
		startButton.setWidth(Constants.MENU_BUTTON_WIDTH_MAIN);

		instances = new Button("Lifecycle Instances", new ButtonListener(ProcessListView.NAME));
		instances.setWidth(Constants.MENU_BUTTON_WIDTH_MAIN);

		tasks = new Button("Lifecycle Tasks", new ButtonListener(TaskListView.NAME));
		tasks.setWidth(Constants.MENU_BUTTON_WIDTH_MAIN);

		notifications = new Button("Retired Services", new ButtonListener(NotificationView.NAME));
		notifications.setWidth(Constants.MENU_BUTTON_WIDTH_MAIN);

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

		HorizontalLayout buttonLayout = new HorizontalLayout();
		// Allow going back to the start
		Button logout = new Button("Logout", new Button.ClickListener() {
			private static final long serialVersionUID = -1809072471885383781L;

			@Override
			public void buttonClick(ClickEvent event) {
				getSession().setAttribute("username", null);
				getSession().setAttribute("role", null);
				getUI().getNavigator().navigateTo("");
			}
		});

		// Allow going back to the start
		Button home = new Button("Home", new Button.ClickListener() {
			private static final long serialVersionUID = -1809072471885383781L;

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(MainView.NAME);
			}
		});
		buttonLayout.setSpacing(true);
		buttonLayout.addComponent(logout);
		buttonLayout.addComponent(Constants.HORIZONTAL_GAP);
		buttonLayout.addComponent(home);

		addComponent(buttonLayout);
	}

	@Override
	public void enter(ViewChangeEvent event) {

		ApplicationUserRoleProperties props = null;
		try {
			props = new ApplicationUserRoleProperties();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// only SOAGovernanceSpecialist can access this
		if (!props.hasRole((String) getUI().getSession().getAttribute("username"), "SOAGovernanceSpecialist")) {

			if (event.getParameters().equalsIgnoreCase(TaskListView.NAME)) {

				this.getNavigator().navigateTo(LimitedView.NAME + "/" + TaskListView.NAME);
			} else if (event.getParameters().contains((TaskDetailView.NAME.toLowerCase()))) {

				logger.info("params:" + event.getParameters());

				this.getNavigator().navigateTo(LimitedView.NAME + "/" + event.getParameters());
			} else {
				this.getNavigator().navigateTo(LimitedView.NAME);
			}

		}

		VerticalLayout panelContent = new VerticalLayout();
		panelContent.setSpacing(true);
		panelContent.setSizeFull();
		panelContent.setMargin(true);
		panelContent.setHeightUndefined();

		panel.setContent(panelContent); // Also clear

		if (event.getParameters() == null || event.getParameters().isEmpty()) {
			fancyImage = new FancyImage();
			fancyImage.setSizeFull();
			fancyImage.setHeight("800px");

			greeting = new Label("Service Lifecycle Manager");
			greeting.setSizeUndefined();
			greeting.setStyleName("h1");

			HorizontalLayout buttons = new HorizontalLayout();
			buttons.setSpacing(true);
			buttons.setMargin(true);
			buttons.setSizeUndefined();

			Button create = new Button("start");
			create.setData(getImageResource(Constants.CREATE_BUTTON_IMAGE));

			create.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					com.vaadin.server.Resource resource = (com.vaadin.server.Resource) event.getButton().getData();
					fancyImage.showResource(resource);
					fancyImage.setVisible(true);

				}

			});

			Label delimiter = new Label();
			delimiter.setValue("|");

			Button execute = new Button("execute");
			execute.setData(Constants.EXECUTE_BUTTON_IMAGE);
			execute.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					com.vaadin.server.Resource resource = (com.vaadin.server.Resource) event.getButton().getData();
					fancyImage.showResource(resource);
					fancyImage.setVisible(true);

				}

			});

			Button monitor = new Button("monitor");
			monitor.setData(Constants.MONITOR_BUTTON_IMAGE);
			monitor.addClickListener(new ClickListener() {

				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					com.vaadin.server.Resource resource = (com.vaadin.server.Resource) event.getButton().getData();
					fancyImage.showResource(resource);
					fancyImage.setVisible(true);

				}

			});

			panelContent.addComponent(greeting);
			panelContent.setComponentAlignment(greeting, Alignment.MIDDLE_CENTER);

			buttons.addComponent(create);
			buttons.addComponent(execute);
			buttons.addComponent(monitor);
			panelContent.addComponent(buttons);
			panelContent.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

			// //// fancy image

			// Setting images used
			final List<Resource> resources = getAllImageResources();

			for (Resource resource : resources) {

				fancyImage.addResource(resource);
			}

			fancyImage.setFadeTransition(true);
			fancyImage.setRotateTransition(true, false);
			fancyImage.setVisible(false);

			panelContent.addComponent(fancyImage);
			panelContent.setComponentAlignment(fancyImage, Alignment.MIDDLE_CENTER);

			// //// fancy image

			return;
		}

		// Redirect to Start Process View
		if (event.getParameters().equalsIgnoreCase(StartProcessView.NAME)) {

			panelContent.addComponent(new StartProcessView(this.getNavigator(), (String) getUI().getSession().getAttribute("username")));

		}

		// Redirect to Process List View
		else if (event.getParameters().equalsIgnoreCase(ProcessListView.NAME)) {

			panelContent.addComponent(new ProcessListView(this.getNavigator()));
			return;

		}

		// Redirect to Task List View
		else if (event.getParameters().equalsIgnoreCase(TaskListView.NAME)) {

			panelContent.addComponent(new TaskListView(this.getNavigator(), (String) getUI().getSession().getAttribute("username")));
			return;

			// Redirect to Notification Action view
		} else if (event.getParameters().equalsIgnoreCase(NotificationView.NAME)) {

			try {
				panelContent.addComponent(new NotificationView(this.getNavigator()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;

		}

		// Redirect to Lifecycle Detail view
		else if (event.getParameters().contains((ProcessDetailView.NAME.toLowerCase()))) {

			// parse the id=X parameter
			Long processInstanceId = Long.valueOf(event.getParameters().substring(event.getParameters().indexOf("id=") + 3));

			try {
				panelContent.addComponent(new ProcessDetailView(processInstanceId, this.getNavigator()));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Redirect to Task Detail View
		else if (event.getParameters().contains(TaskDetailView.NAME.toLowerCase())) {

			Long taskId = Long.valueOf(event.getParameters().substring(event.getParameters().indexOf("id=") + 3));

			panelContent.addComponent(new TaskDetailView(taskId, (String) getUI().getSession().getAttribute("username"), this.getNavigator()));

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

	/**
	 * Loads the image from the WEB-INF/images folder
	 * @param resourceName full file name including extension
	 * @return
	 */
	private Resource getImageResource(String resourceName) {

		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/" + resourceName));
		return resource;
	}

	/**
	 * Gets all images from WEB-INF/images folder
	 * @return List of images
	 */
	private List<Resource> getAllImageResources() {

		List<Resource> list = new ArrayList<Resource>();

		// Find the application directory
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/" + Constants.EXECUTE_BUTTON_IMAGE));
		FileResource resource1 = new FileResource(new File(basepath + "/WEB-INF/images/" + Constants.MONITOR_BUTTON_IMAGE));
		FileResource resource2 = new FileResource(new File(basepath + "/WEB-INF/images/" + Constants.CREATE_BUTTON_IMAGE));

		list.add(resource);
		list.add(resource1);
		list.add(resource2);

		return list;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
}