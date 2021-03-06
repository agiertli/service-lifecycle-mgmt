package org.fi.muni.diploma.thesis.utils.humantask;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Factory which produces instances of InternalHumanTask with their corresponding task outputs
 * 
 * @author osiris
 * 
 */
public class InternalHumanTaskFactory {

	public static InternalHumanTask newInternalHumanTask(HumanTaskName humanTaskIdentifier) {
		InternalHumanTask humanTask = new InternalHumanTask();
		List<HumanTaskOutput> outputs = new ArrayList<HumanTaskOutput>();

		switch (humanTaskIdentifier) {

		case DEPRECATE_SERVICE: {

			humanTask.setName(HumanTaskName.DEPRECATE_SERVICE.toString());

			// populate outputs
			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setDataType(HumanTaskOutputType.BOOLEAN);
			output1.setOutputIdentifier("outBoolconsumersInformedAboutDeprecation");
			output1.setLabel("Inform consumers about service deprecation?:");

			HumanTaskOutput output3 = new HumanTaskOutput();
			output3.setDataType(HumanTaskOutputType.STRING);
			output3.setLabel("To:");
			output3.setOutputIdentifier("outRecipient");

			HumanTaskOutput output4 = new HumanTaskOutput();
			output4.setDataType(HumanTaskOutputType.STRING);
			output4.setLabel("From:");
			output4.setOutputIdentifier("outSender");

			HumanTaskOutput output5 = new HumanTaskOutput();
			output5.setDataType(HumanTaskOutputType.STRING);
			output5.setLabel("Subject:");
			output5.setOutputIdentifier("outSubject");

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setDataType(HumanTaskOutputType.TEXT_AREA);
			output2.setOutputIdentifier("outBoolServiceConfigUpdated");
			output2.setLabel("Service deprecation announcement:");

			outputs.add(output1);
			outputs.add(output3);
			outputs.add(output4);
			outputs.add(output5);
			outputs.add(output2);

			humanTask.setOutputs(outputs);

			break;
		}

		case EVALUATE_TEST_RESULTS: {
			humanTask.setName(HumanTaskName.EVALUATE_TEST_RESULTS.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("t_SendEmail");
			output1.setDataType(HumanTaskOutputType.BOOLEAN);
			output1.setLabel("Send Email:");

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setOutputIdentifier("t_Body");
			output2.setDataType(HumanTaskOutputType.TEXT_AREA);
			output2.setLabel("Body:");

			HumanTaskOutput output3 = new HumanTaskOutput();
			output3.setOutputIdentifier("t_Subject");
			output3.setDataType(HumanTaskOutputType.STRING);
			output3.setLabel("Subject:");

			HumanTaskOutput output4 = new HumanTaskOutput();
			output4.setOutputIdentifier("t_From");
			output4.setDataType(HumanTaskOutputType.STRING);
			output4.setLabel("From:");

			HumanTaskOutput output5 = new HumanTaskOutput();
			output5.setOutputIdentifier("t_To");
			output5.setDataType(HumanTaskOutputType.STRING);
			output5.setLabel("To:");

			HumanTaskOutput output6 = new HumanTaskOutput();
			output6.setOutputIdentifier("t_Retest");
			output6.setDataType(HumanTaskOutputType.BOOLEAN);
			output6.setLabel("Re-Test:");

			outputs.add(output1);
			outputs.add(output2);
			outputs.add(output3);
			outputs.add(output4);
			outputs.add(output5);
			outputs.add(output6);

			humanTask.setOutputs(outputs);

			break;
		}

		case IDENTIFY_SERVICE: {

			humanTask.setName(HumanTaskName.IDENTIFY_SERVICE.toString());
			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("outServiceDescription");
			output1.setDataType(HumanTaskOutputType.TEXT_AREA);
			output1.setLabel("Service Description:");

			outputs.add(output1);

			humanTask.setOutputs(outputs);

			break;
		}

		case INITIALIZE: {

			humanTask.setName(HumanTaskName.INITIALIZE.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("outContractDefined");
			output1.setDataType(HumanTaskOutputType.TEXT_AREA);
			output1.setLabel("Contract defined:");

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setOutputIdentifier("outDocumentationWritten");
			output2.setDataType(HumanTaskOutputType.TEXT_AREA);
			output2.setLabel("Documentation written:");

			outputs.add(output1);
			outputs.add(output2);

			humanTask.setOutputs(outputs);

			break;
		}
		case REGISTER: {

			humanTask.setName(HumanTaskName.REGISTER.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("outBoolPolicyFullfiled");
			output1.setDataType(HumanTaskOutputType.BOOLEAN);
			output1.setLabel("Do the code quality and test coverage match the policy?");

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setOutputIdentifier("outBoolServiceCreated");
			output2.setDataType(HumanTaskOutputType.BOOLEAN);
			output2.setLabel("Has the service been created?");

			HumanTaskOutput output3 = new HumanTaskOutput();
			output3.setOutputIdentifier("outHostname");
			output3.setDataType(HumanTaskOutputType.STRING);
			output3.setLabel("Hostname:");

			HumanTaskOutput output4 = new HumanTaskOutput();
			output4.setOutputIdentifier("outUsername");
			output4.setDataType(HumanTaskOutputType.STRING);
			output4.setLabel("Username:");

			HumanTaskOutput output5 = new HumanTaskOutput();
			output5.setOutputIdentifier("outPassword");
			output5.setLabel("Password:");
			output5.setDataType(HumanTaskOutputType.PASSWORD);

			HumanTaskOutput output6 = new HumanTaskOutput();
			output6.setOutputIdentifier("outIntPort");
			output6.setDataType(HumanTaskOutputType.INTEGER);
			output6.setLabel("Port:");

			outputs.add(output1);
			outputs.add(output2);
			outputs.add(output3);
			outputs.add(output4);
			outputs.add(output5);
			outputs.add(output6);

			humanTask.setOutputs(outputs);

			break;
		}

		case REGISTER_EXISTING_SERVICE: {
			humanTask.setName(HumanTaskName.REGISTER_EXISTING_SERVICE.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("out_Status");
			output1.setDataType(HumanTaskOutputType.ENUM_STATE);
			output1.setLabel("Status:");

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setOutputIdentifier("out_Description");
			output2.setDataType(HumanTaskOutputType.TEXT_AREA);
			output2.setLabel("Description:");

			HumanTaskOutput output3 = new HumanTaskOutput();
			output3.setOutputIdentifier("out_Hostname");
			output3.setDataType(HumanTaskOutputType.STRING);
			output3.setLabel("Hostname:");

			HumanTaskOutput output4 = new HumanTaskOutput();
			output4.setOutputIdentifier("out_Username");
			output4.setDataType(HumanTaskOutputType.STRING);
			output4.setLabel("Username:");

			HumanTaskOutput output5 = new HumanTaskOutput();
			output5.setOutputIdentifier("out_Password");
			output5.setLabel("Password:");
			output5.setDataType(HumanTaskOutputType.PASSWORD);

			HumanTaskOutput output6 = new HumanTaskOutput();
			output6.setOutputIdentifier("out_Port");
			output6.setDataType(HumanTaskOutputType.INTEGER);
			output6.setLabel("Port:");

			outputs.add(output1);
			outputs.add(output2);
			outputs.add(output3);
			outputs.add(output4);
			outputs.add(output5);
			outputs.add(output6);

			humanTask.setOutputs(outputs);
			break;
		}

		case RETIRE_SERVICE: {

			humanTask.setName(HumanTaskName.RETIRE_SERVICE.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setDataType(HumanTaskOutputType.BOOLEAN);
			output1.setOutputIdentifier("outBoolconsumersInformedAboutRetirement");
			output1.setLabel("Inform consumers about service retirement:");
			
			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setLabel("From:");
			output2.setDataType(HumanTaskOutputType.STRING);
			output2.setOutputIdentifier("t_recipient");
			
			HumanTaskOutput output3 = new HumanTaskOutput();
			output3.setLabel("To:");
			output3.setDataType(HumanTaskOutputType.STRING);
			output3.setOutputIdentifier("t_sender");
			
			HumanTaskOutput output4 = new HumanTaskOutput();
			output4.setLabel("Subject:");
			output4.setDataType(HumanTaskOutputType.STRING);
			output4.setOutputIdentifier("t_subject");
			
			HumanTaskOutput output5 = new HumanTaskOutput();
			output5.setLabel("Service retirement announcement:");
			output5.setDataType(HumanTaskOutputType.TEXT_AREA);
			output5.setOutputIdentifier("t_service_retirement");
			
			

			outputs.add(output1);
			outputs.add(output2);
			outputs.add(output3);
			outputs.add(output4);
			outputs.add(output5);

			

			humanTask.setOutputs(outputs);

			break;
		}
		case SELECT_SERVICE_FROM_SRAMP: {

			humanTask.setName(HumanTaskName.SELECT_SERVICE_FROM_SRAMP.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("outServiceSrampUUID");
			output1.setDataType(HumanTaskOutputType.STRING);
			output1.setLabel("Service UUID:");

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setOutputIdentifier("t_serviceName");
			output2.setDataType(HumanTaskOutputType.STRING);
			output2.setLabel("Service Name:");

			outputs.add(output1);
			outputs.add(output2);

			humanTask.setOutputs(outputs);

			break;
		}
		case TEST:

			humanTask.setName(HumanTaskName.TEST.toString());

			HumanTaskOutput output1 = new HumanTaskOutput();
			output1.setOutputIdentifier("outBoolServiceDeployedInProduction");
			output1.setLabel("Service deployed in production:");
			output1.setDataType(HumanTaskOutputType.BOOLEAN);

			HumanTaskOutput output2 = new HumanTaskOutput();
			output2.setOutputIdentifier("outBoolpassedIntegrationTestsWithComponents");
			output2.setDataType(HumanTaskOutputType.BOOLEAN);
			output2.setLabel("Passed integration tests with components:");

			HumanTaskOutput output3 = new HumanTaskOutput();
			output3.setOutputIdentifier("outBoolpassedIntegrationTestsWithServiceConsumers");
			output3.setDataType(HumanTaskOutputType.BOOLEAN);
			output3.setLabel("Passed integration tests with service consumers:");

			outputs.add(output1);
			outputs.add(output2);
			outputs.add(output3);

			humanTask.setOutputs(outputs);
			break;

		default:

			return null;
		}

		return humanTask;

	}

}
