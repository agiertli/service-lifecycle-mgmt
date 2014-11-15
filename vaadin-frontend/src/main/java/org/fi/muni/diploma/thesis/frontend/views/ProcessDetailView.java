package org.fi.muni.diploma.thesis.frontend.views;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.file.FileUtil;
import org.fi.muni.diploma.thesis.utils.jbpm.ProcessStateMap;
import org.fi.muni.diploma.thesis.utils.jbpm.RuntimeEngineWrapper;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbProcessInstanceLog;
import org.kie.services.client.serialization.jaxb.impl.audit.JaxbVariableInstanceLog;
import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
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
	private String selectedProcessName;
	private Embedded currentImage;
	
	private static final Logger logger = Logger.getLogger(ProcessDetailView.class.getName());

	@SuppressWarnings("unchecked")
	public ProcessDetailView(Long processId, Navigator navigator) {
		this.navigator = navigator;
		this.processId = processId;

		VerticalLayout layout = new VerticalLayout();

		// Get process metadata
		JaxbProcessInstanceLog selectedProcess = (JaxbProcessInstanceLog) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findProcessInstance(processId);

		if (selectedProcess == null) {

			Label notfound = new Label("Selected process not found, please don't try to hack me anymore!");
			addComponent(notfound);
			setComponentAlignment(notfound, Alignment.TOP_LEFT);
			return;
		}

		this.selectedProcessName = selectedProcess.getProcessName();

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

		// process diagram
		layout.addComponent(gap);
		Label processDiagramHeader = new Label("Process diagram");
		processDiagramHeader.setSizeUndefined();
		processDiagramHeader.setStyleName("h2");
		layout.addComponent(processDiagramHeader);
		layout.setComponentAlignment(processDiagramHeader, Alignment.MIDDLE_LEFT);
		
		if (currentImage!=null) {
			
			layout.removeComponent(currentImage);
		}

		// load the embedded image
		Embedded image = new Embedded("", new StreamResource(new StreamSource() {

			private static final long serialVersionUID = 1;

			public InputStream getStream() {

				FileUtil util = new FileUtil();

				// select corresponding svg source file
				logger.info("selected process name:"+ProcessDetailView.this.selectedProcessName);
				InputStream is = util.getFileFromClasspath(ProcessDetailView.this.selectedProcessName);

				// new ByteArrayInputStream("".getBytes());
				return is;
			}
		}, ProcessDetailView.this.selectedProcessName+".svg"));

		image.setMimeType("image/svg+xml");
		layout.addComponent(image);
		layout.setComponentAlignment(image, Alignment.MIDDLE_LEFT);
		this.currentImage = image;

		addComponent(layout);

	}

	// build the Process Variables table
	private FilterTable buildProcessVariablesTable(List<JaxbVariableInstanceLog> varLog) {

		FilterTable filterTable = new FilterTable("");
		// filterTable.setSizeUndefined();
		filterTable.setContainerDataSource(buildContainerForVariablesTable(varLog));
		filterTable.setFilterBarVisible(true);

		return filterTable;
	}

	// Populate the data for Process Variables table
	@SuppressWarnings("unchecked")
	private Container buildContainerForVariablesTable(List<JaxbVariableInstanceLog> varLog) {

		List<JaxbVariableInstanceLog> filterSubProcessStates = new ArrayList<JaxbVariableInstanceLog>();

		// find all sub processes
		List<JaxbProcessInstanceLog> subProcesses = (List<JaxbProcessInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findSubProcessInstances(ProcessDetailView.this.processId);

		// search for ServiceState_sub variable
		for (JaxbProcessInstanceLog sub : subProcesses) {

			filterSubProcessStates.addAll((List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
					.findVariableInstances(sub.getProcessInstanceId(), "ServiceState_sub"));

		}

		JaxbVariableInstanceLog finalStateHolder = null;
		if (!filterSubProcessStates.isEmpty()) {
			finalStateHolder = filterSubProcessStates.get(0);

			// only save the LAST instance
			for (JaxbVariableInstanceLog finalState : filterSubProcessStates) {

				if (finalState.getDate().after(finalStateHolder.getDate())) {

					finalStateHolder = finalState;
				}

			}
		}

		List<JaxbVariableInstanceLog> mainProcessStates = (List<JaxbVariableInstanceLog>) RuntimeEngineWrapper.getEngine().getAuditLogService()
				.findVariableInstances(ProcessDetailView.this.processId, "ServiceState");

		IndexedContainer cont = new IndexedContainer();
		int counter = 1;

		cont.addContainerProperty("Variable Name", String.class, null);
		cont.addContainerProperty("Variable Value", String.class, null);

		for (JaxbVariableInstanceLog var : varLog) {

			if (var.getVariableId().equals("ServiceState") || var.getVariableId().equals("taskGroup")) {

				continue;
			}

			cont.addItem(counter);

			cont.getContainerProperty(counter, "Variable Name").setValue(var.getVariableId());
			cont.getContainerProperty(counter, "Variable Value").setValue(var.getValue());

			counter++;
		}

		if (filterSubProcessStates.size() > 0 && finalStateHolder != null) {
			cont.addItem(counter);
			cont.getContainerProperty(counter, "Variable Name").setValue("Service state");
			cont.getContainerProperty(counter, "Variable Value").setValue(finalStateHolder.getValue());

		} else {
			if (mainProcessStates.size() > 0) {
				cont.addItem(counter);
				cont.getContainerProperty(counter, "Variable Name").setValue("Service state");
				cont.getContainerProperty(counter, "Variable Value").setValue(mainProcessStates.get(mainProcessStates.size() - 1).getValue());
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

	public String getSelectedProcessName() {
		return selectedProcessName;
	}

	public void setSelectedProcessName(String selectedProcessName) {
		this.selectedProcessName = selectedProcessName;
	}

	public Embedded getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(Embedded currentImage) {
		this.currentImage = currentImage;
	}

}
