package org.fi.muni.diploma.thesis.frontend.views;

import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.frontend.views.humantaskform.HumanTaskForm;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskName;
import org.fi.muni.diploma.thesis.utils.humantask.InternalHumanTaskFactory;
import org.fi.muni.diploma.thesis.utils.jbpm.TaskServiceWrapper;
import org.jbpm.process.audit.AuditLogService;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Form for completing the task
 * 
 * @author osiris
 * 
 */
public class TaskDetailView extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	public static final String NAME = "taskdetailview";
	private TaskService taskService;
	private AuditLogService auditLogService;
	private PropertysetItem itemset;
	private FieldGroup binder;

	private static final Logger logger = Logger.getLogger(TaskDetailView.class.toString());

	private Navigator navigator;

	public TaskDetailView(Long id, String username,Navigator navigator) {

		this.setNavigator(navigator);
		taskService = new TaskServiceWrapper();

		Task task = taskService.getTaskById(id);
		
		
		if (task == null) {
			
			Label notfound = new Label("Requested task was was not found, plesae don't try to hack me anymore!");
			addComponent(notfound);
			setComponentAlignment(notfound, Alignment.TOP_LEFT);
			return;
		}
		

		// convert task name to enum, should be successfull unless there is a typo or new human task name which didn't
		// get an update
		HumanTaskName name = HumanTaskName.convertStringToEnum(task.getName());

		logger.info("human task name after conversion:" + name.toString());

		//add Human Task Form
		// Task  'Select Service From S-RAMP' is not working due to unsolve issues with jbpm+custom objects
		addComponent(new HumanTaskForm(id,username,  InternalHumanTaskFactory.newInternalHumanTask(name),this.getNavigator()));

	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public AuditLogService getAuditLogService() {
		return auditLogService;
	}

	public void setAuditLogService(AuditLogService auditLogService) {
		this.auditLogService = auditLogService;
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

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

}
