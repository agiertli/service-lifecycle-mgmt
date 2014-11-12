package org.fi.muni.diploma.thesis.frontend.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.RuntimeEngineWrapper;
import org.fi.muni.diploma.thesis.utils.rtgov.Notification;
import org.fi.muni.diploma.thesis.utils.rtgov.RTGovClient;
import org.fi.muni.diploma.thesis.utils.rtgov.RetiredService;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class NotificationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private Navigator navigator;
	private final static Logger logger = Logger.getLogger(NotificationView.class.getName());


	@SuppressWarnings("unused")
	public NotificationView(Navigator navigator) throws IOException {

		this.navigator = navigator;

		List<RetiredService> retiredServices = new ArrayList<RetiredService>();

		@SuppressWarnings("unchecked")
		// let's look for all retired variables
		List<JaxbVariableInstanceLog> varLog = (List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstancesByNameAndValue("ServiceState_sub", "Retired", false);
		
		logger.info("number of retired servicestate_sub"+varLog.size());
		
	
		//look for service names which is stored in the same sub process
		for (JaxbVariableInstanceLog var: varLog) {
			
			logger.info("looping through following sub process instance:"+var.getProcessInstanceId());
		
			
			@SuppressWarnings("unchecked")
			List<JaxbVariableInstanceLog> serviceNames = (List<JaxbVariableInstanceLog>) 	RuntimeEngineWrapper
					.getEngine()
					.getAuditLogService()
					.findVariableInstances(var.getProcessInstanceId(), "sub_servicename");
			
			if (!serviceNames.isEmpty()) {
				//created retiredservice object - servicename and time of retiremend
				retiredServices.add(new RetiredService(serviceNames.get(0).getValue(),var.getDate().getTime()));
			
			
			List<Notification> notifications = new ArrayList<Notification>();
			RTGovClient client = new RTGovClient();
			
			for (RetiredService service: retiredServices) {
				

				notifications.addAll(client.getRetiredInvocation(service));
			}
			
			if (!notifications.isEmpty()) {
				
				System.out.println("result of RTGov client:"+notifications.toString());
			}		
			}
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

}
