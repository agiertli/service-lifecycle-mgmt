package org.fi.muni.diploma.service.lifecycle.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.fi.muni.diploma.thesis.humantask.HumanTaskName;
import org.fi.muni.diploma.thesis.humantask.HumanTaskOutput;
import org.fi.muni.diploma.thesis.humantask.HumanTaskOutputType;
import org.fi.muni.diploma.thesis.humantask.InternalHumanTask;
import org.fi.muni.diploma.thesis.humantask.InternalHumanTaskFactory;
import org.junit.Test;

public class InternalHumanTaskFactoryTest {

	@Test
	public void factoryTest() {

		// create sample Internal Human Task, based on a real process definition
		String taskName = "Test";
		InternalHumanTask ht = new InternalHumanTask();
		List<HumanTaskOutput> outputs = new ArrayList<HumanTaskOutput>();

		HumanTaskOutput output1 = new HumanTaskOutput();
		output1.setOutputIdentifier("outBoolServiceDeployedInProduction");
		output1.setDataType(HumanTaskOutputType.BOOLEAN);
		output1.setLabel("Service deployed in production:");

		HumanTaskOutput output2 = new HumanTaskOutput();
		output2.setOutputIdentifier("outBoolpassedIntegrationTestsWithComponents");
		output2.setLabel("Passed integration tests with components:");
		output2.setDataType(HumanTaskOutputType.BOOLEAN);

		HumanTaskOutput output3 = new HumanTaskOutput();
		output3.setOutputIdentifier("outBoolpassedIntegrationTestsWithServiceConsumers");
		output3.setLabel("Passed integration tests with service consumers:");
		output3.setDataType(HumanTaskOutputType.BOOLEAN);

		outputs.add(output1);
		outputs.add(output2);
		outputs.add(output3);

		ht.setOutputs(outputs);
		ht.setName(taskName);

		InternalHumanTask resultTask = InternalHumanTaskFactory.newInternalHumanTask(HumanTaskName.TEST);

		assertEquals(ht, resultTask); // factory has to return the sama data!

	}

}
