package org.fi.muni.diploma.thesis.frontend.views;

import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.jbpm.RuntimeEngineWrapper;
import org.kie.api.runtime.process.ProcessInstance;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * Start Process View - includes two buttons - for starting a lifecycle for New Service and for Existing Service
 * 
 * @author osiris
 * 
 */
public class StartProcessView extends VerticalLayout implements View {

	/**
	 * 
	 */

	private final static Logger logger = Logger.getLogger(StartProcessView.class.getName());
	private static final long serialVersionUID = 7143562159977862725L;

	private VerticalLayout content;
	private Navigator navigator;
	private RuntimeEngineWrapper runtimeEngine;
	public final static String NAME = "startlifecycle";

	class ButtonListener implements Button.ClickListener {
		private static final long serialVersionUID = -4941184695301907995L;
		
		

		String menuitem;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@SuppressWarnings("static-access")
		@Override
		public void buttonClick(ClickEvent event) {
			
			String processid = "";

			if (menuitem.equals("newservice")) {
				processid = runtimeEngine.getProperties().getProcessIdNewService(); 
			}
				else if (menuitem.equals("existingservice")) {
					
					processid = runtimeEngine.getProperties().getProcessIdExistingService();
				}
				else {
					
					return; //don't do nothing if user hacks the url with the improper id
				}

				// Start the service lifecycle instance
				ProcessInstance processInstance = runtimeEngine.getEngine().getKieSession()
						.startProcess(processid);
				Notification notif = new Notification("New Lifecycle with id '" + processInstance.getId()
						+ "'  successfully started", Type.HUMANIZED_MESSAGE);

				// Customize it
				notif.setDelayMsec(2500);
				notif.setPosition(Position.MIDDLE_CENTER);

				// Show it in the page

				notif.show(Page.getCurrent());

				return;
			} 
		}
	

	public StartProcessView(Navigator navigator) {

		this.navigator = navigator;

		content = new VerticalLayout();
		Label startGreeting = new Label("Start a new Lifecycle");
		startGreeting.setStyleName("h2");
		startGreeting.setSizeUndefined();
		content.addComponent(startGreeting);
		content.setComponentAlignment(startGreeting, Alignment.TOP_CENTER);

		Button newServiceButton = new Button("Start Service Lifecycle - for a new service", new ButtonListener(
				"newservice"));
		Button existingServiceButton = new Button("Start Service Lifecycle - for existing service", new ButtonListener(
				"existingservice"));

		Label gap = new Label("");
		gap.setHeight("200px");

		Label biggerGap = new Label("</br></br>", ContentMode.HTML);

		content.addComponent(biggerGap);
		gap.setHeight("1em");
		content.addComponent(newServiceButton);
		content.addComponent(gap);
		content.addComponent(existingServiceButton);
		content.setComponentAlignment(newServiceButton, Alignment.MIDDLE_CENTER);
		content.setComponentAlignment(existingServiceButton, Alignment.MIDDLE_CENTER);

		addComponent(content);

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

}
