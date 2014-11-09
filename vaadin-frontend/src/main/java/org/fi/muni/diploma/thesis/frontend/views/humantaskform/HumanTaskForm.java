package org.fi.muni.diploma.thesis.frontend.views.humantaskform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.frontend.views.MainView;
import org.fi.muni.diploma.thesis.frontend.views.StartView;
import org.fi.muni.diploma.thesis.frontend.views.TaskDetailView;
import org.fi.muni.diploma.thesis.frontend.views.TaskListView;
import org.fi.muni.diploma.thesis.utils.TaskServiceWrapper;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskName;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskOutput;
import org.fi.muni.diploma.thesis.utils.humantask.HumanTaskOutputType;
import org.fi.muni.diploma.thesis.utils.humantask.InternalHumanTask;
import org.kie.api.task.TaskService;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class HumanTaskForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private InternalHumanTask humanTask;
	private PropertysetItem itemset;
	private FieldGroup binder;
	private Long taskid;
	private static final Logger logger = Logger.getLogger(HumanTaskForm.class.getName());
	private TaskService taskService;
	public Navigator navigator;

	public class ButtonListener implements ClickListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		String menuitem;

		public ButtonListener(String menuitem) {
			this.menuitem = menuitem;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			taskService = new TaskServiceWrapper();
			//
			logger.info("button clicked");

			FieldGroup data = (FieldGroup) event.getButton().getData();
			Long id = HumanTaskForm.this.getTaskid();

			Map<String, Object> result = new HashMap<String, Object>();

			Collection<Field<?>> col = data.getFields();
			//
			for (Iterator<Field<?>> iterator = col.iterator(); iterator.hasNext();) {
				Field<?> field = iterator.next();

				System.out.println("output name:" + binder.getPropertyId(field));
				System.out.println("output value:" + field.getValue());

				result.put((String) binder.getPropertyId(field), field.getValue());

			}
			
			
            //	taskService.start(id, "anton");
			//	taskService.complete(id, "anton", result);
			
			HumanTaskForm.this.navigator.navigateTo("main"+"/"+TaskListView.NAME);

		

		}
	}

	public HumanTaskForm() {

	}

	public HumanTaskForm(Long taskId, InternalHumanTask newInternalHumanTask,Navigator navigator) {
		this.humanTask = newInternalHumanTask;
		this.taskid = taskId;
		this.navigator = navigator;
		

		// data binding of form
		this.setItemset(new PropertysetItem());
		this.setBinder(new FieldGroup(this.getItemset()));

		Label greeting = new Label("Work on \'" + this.humanTask.getName() + "\' task");
		greeting.setSizeUndefined();
		greeting.setStyleName("h2");
		addComponent(greeting);

		FormLayout fl = new FormLayout();

		List<HumanTaskOutput> outputs = new ArrayList<HumanTaskOutput>();
		outputs = this.humanTask.getOutputs();

		// go through all outputs for this particular human task, and generate corresponding data output
		for (HumanTaskOutput output : outputs) {

			HumanTaskOutputType type = output.getDataType();

			switch (type) {
			case BOOLEAN: {

				CheckBox checkbox = new CheckBox();
				checkbox.setCaption(output.getLabel());
				checkbox.setRequired(true);
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(checkbox, output.getOutputIdentifier());
				fl.addComponent(checkbox);
			}
				break;
			case ENUM_STATE:
				
				NativeSelect enumSelect = new NativeSelect(output.getLabel());
				enumSelect.setRequired(true);
				enumSelect.setRequiredError("This field is required");
				
				if (this.getHumanTask().getName().equals(HumanTaskName.REGISTER_EXISTING_SERVICE.toString())) {
					
					enumSelect.addItems("InTest", "Available","Deprecated");
				}
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(enumSelect, output.getOutputIdentifier());
				
				fl.addComponent(enumSelect);
				
				break;
			case INTEGER: {

				TextField integerField = new TextField(output.getLabel());
				integerField.setConverter(new StringToIntegerConverter());
				integerField.addValidator(new IntegerRangeValidator("Value has to be from range 0-65535", 0, 65535));
				integerField.setRequired(true);
				integerField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<Integer>(0));
				this.getBinder().bind(integerField, output.getOutputIdentifier());
				fl.addComponent(integerField);

			}
				break;
			case PASSWORD: {

				PasswordField passwordField = new PasswordField(output.getLabel());
				passwordField.setRequired(true);
				passwordField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(passwordField, output.getOutputIdentifier());
				fl.addComponent(passwordField);
			}
				break;
			case STRING: {

				TextField stringField = new TextField(output.getLabel());
				stringField.setRequired(true);
				stringField.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(stringField, output.getOutputIdentifier());
				fl.addComponent(stringField);
			}
				break;
			case TEXT_AREA: {

				TextArea textArea = new TextArea(output.getLabel());
				textArea.setRequired(true);
				textArea.setRequiredError("This field is required");
				this.getItemset().addItemProperty(output.getOutputIdentifier(), new ObjectProperty<String>(""));
				this.getBinder().bind(textArea, output.getOutputIdentifier());
				fl.addComponent(textArea);
			}
				break;
			default:
				break;

			}

		}
		Button submitButton = new Button("Complete Task");
		submitButton.setData(this.getBinder());
		submitButton.addClickListener(new ButtonListener(TaskDetailView.NAME));
		fl.addComponent(submitButton);
		addComponent(fl);

	}

	public InternalHumanTask getHumanTask() {
		return humanTask;
	}

	public void setHumanTask(InternalHumanTask humanTask) {
		this.humanTask = humanTask;
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

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}

}
