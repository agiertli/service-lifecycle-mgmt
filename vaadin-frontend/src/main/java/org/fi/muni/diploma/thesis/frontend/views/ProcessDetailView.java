package org.fi.muni.diploma.thesis.frontend.views;

import java.util.Date;
import java.util.List;

import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterDecorator;
import org.fi.muni.diploma.thesis.frontend.filterutils.CustomFilterGenerator;
import org.fi.muni.diploma.thesis.frontend.views.ProcessListView.ButtonListener;
import org.fi.muni.diploma.thesis.utils.ProcessStateMap;
import org.fi.muni.diploma.thesis.utils.RuntimeEngineWrapper;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbProcessInstanceLog;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;
import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ProcessDetailView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String NAME = "processdetails";
	private Navigator navigator;

	private FilterTable processDetailsTable;
	private FilterTable processVariablesTable;
	private Long processId;

	@SuppressWarnings("unchecked")
	public ProcessDetailView(Long processId, Navigator navigator) {
		this.navigator = navigator;
		this.processId = processId;

		VerticalLayout layout = new VerticalLayout();

		// Get process metadata
		JaxbProcessInstanceLog selectedProcess = (JaxbProcessInstanceLog) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findProcessInstance(processId);

		// Get Process Variables
		List<JaxbVariableInstanceLog> varLog = (List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstances(processId);

		// Build process details table
		processDetailsTable = buildProcessDetailsTable(selectedProcess);
		processDetailsTable.setPageLength(processDetailsTable.getContainerDataSource().size());

		// Build process variables table
		processVariablesTable = buildProcessVariablesTable(varLog);
		processVariablesTable.setPageLength(processVariablesTable.getContainerDataSource().size());

		Label greeting = new Label("Details of Lifecycle Instance");
		greeting.setSizeUndefined();
		greeting.setStyleName("h1");

		Label processDetailsTableHeader = new Label("Lifecycle Metadata");
		processDetailsTableHeader.setSizeUndefined();
		processDetailsTableHeader.setStyleName("h2");

		Label processVariablesHeader = new Label("Lifecycle's variables");
		processVariablesHeader.setSizeUndefined();
		processVariablesHeader.setStyleName("h2");

		Label gap = new Label("</br></br>", ContentMode.HTML);

		layout.addComponent(greeting);
		layout.addComponent(processDetailsTableHeader);
		layout.addComponent(processDetailsTable);
		layout.addComponent(gap);
		layout.addComponent(processVariablesHeader);
		layout.addComponent(processVariablesTable);

		layout.setComponentAlignment(greeting, Alignment.TOP_CENTER);
		layout.setComponentAlignment(processDetailsTableHeader, Alignment.MIDDLE_LEFT);
		layout.setComponentAlignment(processVariablesHeader, Alignment.MIDDLE_LEFT);

		addComponent(layout);

	}

	// build the Process Variables table
	private FilterTable buildProcessVariablesTable(List<JaxbVariableInstanceLog> varLog) {

		FilterTable filterTable = new FilterTable("");
		// filterTable.setSizeUndefined();
		filterTable.setContainerDataSource(buildContainerForVariablesTable(varLog));
		filterTable.setFilterBarVisible(false);

		return filterTable;
	}

	// Populate the data for Process Variables table
	@SuppressWarnings("unchecked")
	private Container buildContainerForVariablesTable(List<JaxbVariableInstanceLog> varLog) {

		// get specificaly process states from the sub process
		List<JaxbVariableInstanceLog> processStates = (List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstancesByName("ServiceState_sub", false);

		IndexedContainer cont = new IndexedContainer();
		int counter = 1;

		int stateindex = 0;

		cont.addContainerProperty("Variable Name", String.class, null);
		cont.addContainerProperty("Variable Value", String.class, null);

		for (JaxbVariableInstanceLog var : varLog) {

			// we want to ignore empty values and taskGroup
			if (var.getValue().isEmpty() || var.getVariableId().equals("taskGroup")) {

				continue;

			} else {

				// we only want to preserve the latest ServiceState value
				if (var.getVariableId().equals("ServiceState") && stateindex != 0) {

					for (JaxbVariableInstanceLog subState : processStates) {
						if (subState.getExternalId().equals(var.getExternalId())) {

							cont.getContainerProperty(stateindex, "Variable Value").setValue(subState.getValue());
						}

						else {

							cont.getContainerProperty(stateindex, "Variable Value").setValue(var.getValue());
						}
						counter++;
						continue;

					}
				}

				if (var.getVariableId().equals("ServiceState") && stateindex == 0) {

					stateindex = counter;

				}

				cont.addItem(counter);

				cont.getContainerProperty(counter, "Variable Name").setValue(var.getVariableId());
				cont.getContainerProperty(counter, "Variable Value").setValue(var.getValue());

				counter++;
			}
		}

		return cont;
	}

	// build the Process Detail Table
	private FilterTable buildProcessDetailsTable(JaxbProcessInstanceLog selectedProcess) {

		FilterTable filterTable = new FilterTable("");
		// filterTable.setSizeUndefined();
		filterTable.setContainerDataSource(buildContainerForDetailsTable(selectedProcess));
		filterTable.setFilterBarVisible(false);

		return filterTable;
	}

	// Populate the data Process Detail Table
	@SuppressWarnings("unchecked")
	private Container buildContainerForDetailsTable(JaxbProcessInstanceLog selectedProcess) {

		IndexedContainer cont = new IndexedContainer();

		cont.addContainerProperty("Process Name", String.class, null);
		cont.addContainerProperty("Process ID", Long.class, null);
		cont.addContainerProperty("Process State", ProcessStateMap.States.class, null);
		cont.addContainerProperty("Start Date", Date.class, null);
		cont.addContainerProperty("End Date", Date.class, null);

		cont.addItem(1);

		cont.getContainerProperty(1, "Process Name").setValue(selectedProcess.getProcessName());
		cont.getContainerProperty(1, "Process ID").setValue(selectedProcess.getProcessInstanceId());
		cont.getContainerProperty(1, "Process State").setValue(ProcessStateMap.getProcessStatusAsEnum(selectedProcess.getStatus()));
		cont.getContainerProperty(1, "Start Date").setValue(selectedProcess.getStart());
		cont.getContainerProperty(1, "End Date").setValue(selectedProcess.getEnd());

		return cont;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

	public FilterTable getProcessDetailsTable() {
		return processDetailsTable;
	}

	public void setProcessDetailsTable(FilterTable processDetailsTable) {
		this.processDetailsTable = processDetailsTable;
	}

	public FilterTable getProcessVariablesTable() {
		return processVariablesTable;
	}

	public void setProcessVariablesTable(FilterTable processVariablesTable) {
		this.processVariablesTable = processVariablesTable;
	}

	public Long getProcessId() {
		return processId;
	}

	public void setProcessId(Long processId) {
		this.processId = processId;
	}

}
