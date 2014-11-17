package org.fi.muni.diploma.thesis.frontend.views;

import java.util.logging.Logger;

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

@SuppressWarnings("unused")
public class LimitedView extends VerticalLayout implements View {

	private static final long serialVersionUID = -3398565663865641952L;

	private final static Logger logger = Logger.getLogger(MainView.class.getName());

	public static String NAME = "limited-main";

	// list of components on the page
	private Panel panel;
	private Navigator navigator;
	private Button tasks;

	private Label greeting;

	public LimitedView(Navigator navigator) {

		this.navigator = navigator;

		setSizeFull();

		// Layout with menu on left and view area on right
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setSizeFull();

		// Have a menu on the left side of the screen
		Panel menu = new Panel("Menu");
		menu.setHeight("100%");
		menu.setWidth(null);
		VerticalLayout menuContent = new VerticalLayout();

		tasks = new Button("Lifecycle Tasks", new ButtonListener(TaskListView.NAME));
		tasks.setWidth("179px");

		menuContent.addComponent(tasks);

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
				getSession().setAttribute("username", null);
				getUI().getNavigator().navigateTo("");
			}
		});
		addComponent(logout);
	}

	// Menu navigation button listener
	class ButtonListener implements Button.ClickListener {
		private static final long serialVersionUID = -4941184695301907995L;

		String menuitem;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {

			LimitedView.this.navigator.navigateTo(LimitedView.NAME + "/" + menuitem);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
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

		// Redirect to Task List View
		else if (event.getParameters().toLowerCase().contains((TaskListView.NAME.toLowerCase()))) {

			panelContent.addComponent(new TaskListView(this.getNavigator(), (String) getUI().getSession().getAttribute("username")));
			return;

			// Redirect to Notification Action view
		}

		// Redirect to Task Detail View
		else if (event.getParameters().toLowerCase().contains(TaskDetailView.NAME.toLowerCase())) {

			Long taskId = Long.valueOf(event.getParameters().substring(event.getParameters().indexOf("id=") + 3));

			panelContent.addComponent(new TaskDetailView(taskId, (String) getUI().getSession().getAttribute("username"), this.getNavigator()));

		}

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

}
