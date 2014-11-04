package org.fi.muni.diploma.thesis.frontend.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.fi.muni.diploma.thesis.utils.AuditServiceWrapper;
import org.fi.muni.diploma.thesis.utils.TaskServiceWrapper;
import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.VariableInstanceLog;
import org.kie.api.task.TaskService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Form for completing the task
 * 
 * @author osiris
 * 
 */
public class TaskDetailView extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String NAME = "taskdetailview";
	private TaskService taskService;
	private AuditLogService auditLogService;
	private PropertysetItem itemset;
	private FieldGroup binder;

	private Navigator navigator;

	public TaskDetailView(Long id, Navigator navigator) {

		this.setNavigator(navigator);
		auditLogService = new AuditServiceWrapper();
		taskService = new TaskServiceWrapper();

		// data binding of form
		this.setItemset(new PropertysetItem());
		this.setBinder(new FieldGroup(this.getItemset()));

		// get process id to which this task belong
		Long processId = taskService.getTaskById(id).getTaskData().getProcessInstanceId();
		// determine the outputs of this task
		List<VariableInstanceLog> var = auditLogService.findVariableInstances(processId, "currentTaskOutput");
		String outputs = var.get(0).getValue();

		
		List<String> outputList = Arrays.asList(outputs.split(","));
		System.out.println("vaadin output list:" + outputList);

		// A FormLayout used outside the context of a Form
		FormLayout fl = new FormLayout();

		// Make the FormLayout shrink to its contents
		fl.setSizeUndefined();

		for (String output : outputList) {

			// add checkbox for boolean variables
			if (output.toLowerCase().startsWith("outbool")) {

				CheckBox check = new CheckBox(output.substring("outBool".length(), output.length()));
				fl.addComponent(check);

				check.setRequired(true);
				check.setRequiredError("You must fulfil this to true before you can continue");

				this.getItemset().addItemProperty(output, new ObjectProperty<Boolean>(true));
				this.getBinder().bind(check, output);

			}

			// add text field but enforce integer validation
			else if (output.toLowerCase().startsWith("outint")) {

				TextField integerTextField = new TextField(output.substring("outint".length(), output.length()));
				fl.addComponent(integerTextField);

				integerTextField.setConverter(new StringToIntegerConverter());
				integerTextField.addValidator(new IntegerRangeValidator("Integer is required", Integer.MIN_VALUE,
						Integer.MAX_VALUE));

				if (this.getItemset() == null) {

					System.out.println("item set je null, hm");
				}

				this.getItemset().addItemProperty(output, new ObjectProperty<Integer>(0));
				this.getBinder().bind(integerTextField, output);
			}

			// add standard Text field
			else {

				TextField stringField = new TextField(output);
				this.getItemset().addItemProperty(output, new ObjectProperty<String>(""));

				fl.addComponent(stringField);
				this.getBinder().bind(stringField, output);
			}
		}

		Button submitButton = new Button("complete task");

		Map<Long, FieldGroup> outputMap = new HashMap<Long, FieldGroup>();
		outputMap.put(id, this.getBinder());
		submitButton.setData(outputMap);

		submitButton.addClickListener(new Button.ClickListener() {

			/**
			 * Details of selected process
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				Map<String, Object> result = new HashMap<String, Object>();

				System.out.println("submitting form");
				Map<Long, FieldGroup> data = (Map<Long, FieldGroup>) event.getButton().getData();

				Iterator it = data.entrySet().iterator();
				while (it.hasNext()) {

					Map.Entry pair = (Map.Entry) it.next();

					Long taskId = (Long) pair.getKey();
					System.out.println("task id:" + pair.getKey());
					FieldGroup binder = (FieldGroup) pair.getValue();
					try {
						binder.commit();
					} catch (CommitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Collection<Field<?>> col = binder.getFields();

					for (Iterator<Field<?>> iterator = col.iterator(); iterator.hasNext();) {
						Field<?> field = iterator.next();

						System.out.println("output name:" + binder.getPropertyId(field));

						result.put((String) binder.getPropertyId(field), field.getValue());

					}

					taskService.start(taskId, "anton");
					taskService.complete(taskId, "anton", result);

					TaskDetailView.this.getNavigator().navigateTo("main/tasklist");

					it.remove();
				}

			}
		});

		fl.addComponent(submitButton);
		addComponent(fl);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

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
