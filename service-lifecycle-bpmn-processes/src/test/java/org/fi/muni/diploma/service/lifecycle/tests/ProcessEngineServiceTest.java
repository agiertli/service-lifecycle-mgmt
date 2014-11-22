package org.fi.muni.diploma.service.lifecycle.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.fi.muni.diploma.thesis.service_lifecycle_model.Service;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

public class ProcessEngineServiceTest extends JbpmJUnitBaseTestCase {

	private final static String actor = new String("anton");
	private final static String lang = new String("en-UK");
	private WorkItem workItem;

	private final static String mainProcess = "Sevice Lifecycle Process - Final.bpmn2";
	private final static String AvailableSubProces = "Available Sub Process.bpmn2";
	private final static String DeprecatedSubProcess = "Deprecated Sub Process.bpmn2";
	private final static String InitSubProcess = "Initialize Sub Process.bpmn2";
	private final static String QuerySRAMPSubProcess = "Query S-RAMP Wrapper Sub Process.bpmn2";
	private final static String RegisterSubProcess = "Register Sub Process.bpmn2";
	private final static String TestSubProces = "Test Subprocess.bpmn2";

	public ProcessEngineServiceTest() {
		super(true, true);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProcess() throws NamingException, NotSupportedException, SystemException, SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException, HeuristicRollbackException {

		// create runtime manager all the processes
		createRuntimeManager(mainProcess, AvailableSubProces, DeprecatedSubProcess, InitSubProcess, InitSubProcess, QuerySRAMPSubProcess,
				RegisterSubProcess, TestSubProces);

		// take RuntimeManager to work with process engine
		RuntimeEngine runtimeEngine = getRuntimeEngine();

		// get access to KieSession instance
		KieSession ksession = runtimeEngine.getKieSession();

		// Test Handler provided by the jBPM test suite
		TestWorkItemHandler querySrampHandler = getTestWorkItemHandler();
		TestWorkItemHandler emailHandler = getTestWorkItemHandler();
		TestWorkItemHandler ontologyhandler = getTestWorkItemHandler();

		// register dummy handler
		ksession.getWorkItemManager().registerWorkItemHandler("QuerySRAMP", querySrampHandler);
		ksession.getWorkItemManager().registerWorkItemHandler("ApplyOntology", ontologyhandler);
		ksession.getWorkItemManager().registerWorkItemHandler("Email", emailHandler);

		// is KieSession created successfully with the resources above?
		assertNotNull("Ksession is NULL", ksession);

		// get task service
		TaskService taskService = runtimeEngine.getTaskService();

		UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
		// everything must run within transaction

		ut.begin();

		// start process
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) ksession.startProcess("service-lifecycle-new-service");

		// has process been started successfully?
		assertNotNull("processInstance is null", processInstance);

		// parma mapping
		assertEquals("Wrong param mapping", "New", processInstance.getVariable("ServiceState"));

		// Human task Identify Service
		assertNodeActive(processInstance.getId(), ksession, "Identify Service");

		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertNotNull("Task list is null", list);
		assertEquals("Task name Mismatch", "Identify Service", list.get(0).getName());
		Map<String, Object> output = new HashMap<String, Object>();
		output.put("outServiceDescription", "service description");
		taskService.start(list.get(0).getId(), actor);
		taskService.complete(list.get(0).getId(), actor, output);

		// param mapping
		assertEquals("Wrong param mapping", "service description", ((WorkflowProcessInstance) processInstance).getVariable("serviceDescription"));

		// Human Task Initial
		assertNodeActive(processInstance.getId(), ksession, "Initialize  Service ");
		list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertEquals("Task name Mismatch", "Initialize", list.get(0).getName());
		output = new HashMap<String, Object>();
		output.put("outContractDefined", "contract defined");
		output.put("outDocumentationWritten", "doc written");
		taskService.start(list.get(0).getId(), actor);
		taskService.complete(list.get(0).getId(), actor, output);

		assertEquals("Wrong param mapping", "Initial", processInstance.getVariable("ServiceState"));

		// Sub process Register
		assertNodeActive(processInstance.getId(), ksession, "Register Service ");
		list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertEquals("Task list is not empty", false, list.isEmpty());
		assertEquals("Task name Mismatch", "Register", list.get(0).getName());
		output = new HashMap<String, Object>();
		output.put("outBoolPolicyFullfiled", true);
		output.put("outBoolServiceCreated", true);
		taskService.start(list.get(0).getId(), actor);

		// param mapping
		processInstance.setVariable("hostname", "localhost");
		processInstance.setVariable("username", "admin");
		processInstance.setVariable("password", "password1!");
		processInstance.setVariable("port", new Integer(8080));

		taskService.complete(list.get(0).getId(), actor, output);

		assertEquals("Wrong param mapping", "registered", processInstance.getVariable("ServiceState"));

		// Domain specific task - Query S-RAMP Repository
		workItem = querySrampHandler.getWorkItem();

		// this is just a mock of real logic
		Service service1 = new Service("service1", "1");
		Service service2 = new Service("service2", "2");
		Service service3 = new Service("service3", "3");
		Service service4 = new Service("service4", "4");

		List<Service> serviceList = new ArrayList<Service>();
		serviceList.add(service1);
		serviceList.add(service2);
		serviceList.add(service3);
		serviceList.add(service4);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("OutServiceList", serviceList);
		ksession.getWorkItemManager().completeWorkItem(workItem.getId(), result);

		List<Service> varList = (List<Service>) processInstance.getVariable("srampServiceList");
		assertEquals("Wrong param mapping", serviceList.size(), varList.size());

		// Human Task Select service from S-RAMP
		assertNodeActive(processInstance.getId(), ksession, "Select service from S-RAMP");
		list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertEquals("Task list is empty", false, list.isEmpty());
		assertEquals("Task name Mismatch", "Select service from S-RAMP", list.get(0).getName());
		taskService.start(list.get(0).getId(), actor);

		Map<String, Object> taskContent = taskService.getTaskContent(list.get(0).getId());

		output = new HashMap<String, Object>();
		output.put("outServiceSrampUUID", (((List<Service>) taskContent.get("inServiceList")).get(0)).getUUID()); // 1
		output.put("t_serviceName", (((List<Service>) taskContent.get("inServiceList")).get(0)).getName()); // service1

		taskService.complete(list.get(0).getId(), actor, output);

		// Domain Specific Task - Apply ontology
		workItem = ontologyhandler.getWorkItem();
		assertEquals(processInstance.getVariable("serviceSRAMPUUID"), workItem.getParameter("inUuid"));
		assertEquals("InTest", workItem.getParameter("inValue"));
		ksession.getWorkItemManager().completeWorkItem(workItem.getId(), null);

		// Sub process in test
		assertNodeActive(processInstance.getId(), ksession, "Test Service ");
		list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertEquals("Task list is empty", false, list.isEmpty());
		assertEquals("Task name Mismatch", "Test", list.get(0).getName());
		taskService.start(list.get(0).getId(), actor);

		output = new HashMap<String, Object>();
		output.put("outBoolServiceDeployedInProduction", true);
		output.put("outBoolpassedIntegrationTestsWithComponents", true);
		output.put("outBoolpassedIntegrationTestsWithServiceConsumers", true);

		taskService.complete(list.get(0).getId(), actor, output);

		// param mapping
		assertEquals("Wrong param mapping", true, processInstance.getVariable("isServiceDeployedInProduction"));
		assertEquals("Wrong param mapping", true, processInstance.getVariable("passedIntegrationTestsWithComponents"));
		assertEquals("Wrong param mapping", true, processInstance.getVariable("passedIntegrationTestsWithServiceConsumers"));
		assertEquals("Wrong param mapping", "InTest", processInstance.getVariable("ServiceState"));

		// Domain Specific Task - Apply ontology

		workItem = ontologyhandler.getWorkItem();
		ksession.getWorkItemManager().completeWorkItem(workItem.getId(), null);

		// Deprecate sub process
		assertNodeActive(processInstance.getId(), ksession, "Deprecate service");
		list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertEquals("Task list is empty", false, list.isEmpty());
		assertEquals("Task name Mismatch", "Deprecate service", list.get(0).getName());
		taskService.start(list.get(0).getId(), actor);

		output = new HashMap<String, Object>();
		output.put("outBoolconsumersInformedAboutDeprecation", true);
		output.put("outBoolServiceConfigUpdated", true);

		taskService.complete(list.get(0).getId(), actor, output);

		// param mapping

		assertEquals("Wrong param mapping", "Available", processInstance.getVariable("ServiceState"));

		// Domain Specific Task - Apply ontology
		workItem = ontologyhandler.getWorkItem();
		ksession.getWorkItemManager().completeWorkItem(workItem.getId(), null);

		// Sub Process - Retired
		assertNodeActive(processInstance.getId(), ksession, "Retire service");
		list = taskService.getTasksAssignedAsPotentialOwner(actor, lang);
		assertEquals("Task list is empty", false, list.isEmpty());
		assertEquals("Task name Mismatch", "Retire Service", list.get(0).getName());
		taskService.start(list.get(0).getId(), actor);

		output = new HashMap<String, Object>();
		output.put("outBoolconsumersInformedAboutRetirement", true);
		taskService.complete(list.get(0).getId(), actor, output);

		// Domain Specific Task - Apply ontology
		workItem = ontologyhandler.getWorkItem();

		ksession.getWorkItemManager().completeWorkItem(workItem.getId(), null);

		assertProcessInstanceCompleted(processInstance.getId(), ksession);

		ut.commit();

	}

}
