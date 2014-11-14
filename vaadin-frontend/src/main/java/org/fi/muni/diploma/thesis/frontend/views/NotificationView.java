package org.fi.muni.diploma.thesis.frontend.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterDecorator;
import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterGenerator;
import org.fi.muni.diploma.thesis.utils.RuntimeEngineWrapper;
import org.fi.muni.diploma.thesis.utils.properties.RTGovProperties;
import org.fi.muni.diploma.thesis.utils.rtgov.Notification;
import org.fi.muni.diploma.thesis.utils.rtgov.RTGovClient;
import org.fi.muni.diploma.thesis.utils.rtgov.RetiredService;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;
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
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class NotificationView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	private Navigator navigator;
	private final static Logger logger = Logger.getLogger(NotificationView.class.getName());
	private PagedFilterTable<?> filterTable;
	public static final String NAME = "notificationactions";

	private Notification currentNotification;

	public NotificationView(Navigator navigator) throws IOException, NamingException, SQLException {

		this.navigator = navigator;

		VerticalLayout layout = new VerticalLayout();
		Set<Notification> notifications = this.getNotifications();

		Label greeting = new Label("List of retired service invocations");
		greeting.setSizeUndefined();
		greeting.setStyleName("h1");
		layout.addComponent(greeting);
		layout.setComponentAlignment(greeting, Alignment.TOP_CENTER);

		if (!notifications.isEmpty()) {

			filterTable = buildFilterTable(notifications);
			filterTable.setPageLength(filterTable.getContainerDataSource().size());
			layout.addComponent(filterTable);
			layout.setComponentAlignment(greeting, Alignment.TOP_CENTER);
			layout.setComponentAlignment(filterTable, Alignment.TOP_CENTER);

			PagedFilterControlConfig config = new PagedFilterControlConfig();
			config.setItemsPerPage("Invocations per page:");
			List<Integer> lengths = new ArrayList<Integer>();
			lengths.add(10);
			lengths.add(20);
			lengths.add(50);
			lengths.add(100);
			lengths.add(250);
			lengths.add(500);
			config.setPageLengthsAndCaptions(lengths);

			layout.addComponent(filterTable.createControls(config));
		}

		addComponent(layout);

	}

	private PagedFilterTable<?> buildFilterTable(Set<Notification> notifications) {

		PagedFilterTable<IndexedContainer> filterTable = new PagedFilterTable<IndexedContainer>("");
		// filterTable.setSizeFull();
		filterTable.setFilterDecorator(new CustomFilterDecorator());
		filterTable.setFilterGenerator(new CustomFilterGenerator());
		filterTable.setContainerDataSource(buildContainer(notifications));
		filterTable.setFilterBarVisible(true);
		filterTable.setFilterFieldVisible("Action", false);
		// filterTable.setFilterFieldValue("Process State", ProcessStateMap.States.ACTIVE);

		return filterTable;
	}

	@SuppressWarnings("unchecked")
	private Container buildContainer(Set<Notification> notifications) {

		IndexedContainer cont = new IndexedContainer();

		cont.addContainerProperty("Service Name", String.class, null);
		cont.addContainerProperty("Retirement Date", Date.class, null);
		cont.addContainerProperty("Invocation Date", Date.class, null);
		cont.addContainerProperty("Interface", String.class, null);
		cont.addContainerProperty("Operation", String.class, null);
		cont.addContainerProperty("Action", Button.class, null);

		int i = 1;

		for (Notification notification : notifications) {
			cont.addItem(i);

			cont.getContainerProperty(i, "Service Name").setValue(notification.getService().getName());
			cont.getContainerProperty(i, "Retirement Date").setValue(new Date(notification.getService().getRetirementTimestamp()));
			cont.getContainerProperty(i, "Invocation Date").setValue(new Date(notification.getInvocationTimestamp()));
			cont.getContainerProperty(i, "Interface").setValue(notification.getInterfaceName());
			cont.getContainerProperty(i, "Operation").setValue(notification.getOperation());

			Button detailsField = new Button("send notification");
			detailsField.setData(notification);
			detailsField.addClickListener(new ButtonListener(NotificationDetailView.NAME));
			detailsField.addStyleName(BaseTheme.BUTTON_LINK);

			cont.getContainerProperty(i, "Action").setValue(detailsField);

			i++;

		}

		return cont;

	}

	@Override
	public void enter(ViewChangeEvent event) {
		return;
	}

	class ButtonListener implements ClickListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String menuitem;

		public ButtonListener(String menuitem) {

			this.menuitem = menuitem;

		}

		@Override
		public void buttonClick(ClickEvent event) {

			Notification n = (Notification) event.getButton().getData();

			getUI().getSession().setAttribute("notification", n); // pass the session via session

			NotificationView.this.navigator.navigateTo("main" + "/" + menuitem);

		}

	}

	public Set<Notification> getNotifications() throws IOException, NamingException, SQLException {

		Set<RetiredService> retiredServices = new HashSet<RetiredService>();
		Set<Notification> notifications = new HashSet<Notification>();

		@SuppressWarnings("unchecked")
		// let's look for all retired variables
		List<JaxbVariableInstanceLog> varLog = (List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstancesByNameAndValue("ServiceState_sub", "Retired", false);

		logger.info("number of retired servicestate_sub" + varLog.size());

		// look for service names which is stored in the same sub process
		for (JaxbVariableInstanceLog var : varLog) {

			logger.info("looping through following sub process instance:" + var.getProcessInstanceId());

			@SuppressWarnings("unchecked")
			List<JaxbVariableInstanceLog> serviceNames = (List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
					.findVariableInstances(var.getProcessInstanceId(), "sub_servicename");

			if (!serviceNames.isEmpty()) {
				// created retiredservice object - servicename and time of retiremend
				retiredServices.add(new RetiredService(serviceNames.get(0).getValue(), var.getDate().getTime()));

				RTGovClient client = new RTGovClient(new RTGovProperties());

				for (RetiredService service : retiredServices) {

					notifications.addAll(client.getRetiredInvocation(service));
				}

			}
		}
		return notifications;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public Notification getCurrentNotification() {
		return currentNotification;
	}

	public void setCurrentNotification(Notification currentNotification) {
		this.currentNotification = currentNotification;
	}

}
