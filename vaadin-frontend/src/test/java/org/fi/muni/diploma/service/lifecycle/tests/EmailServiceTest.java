package org.fi.muni.diploma.service.lifecycle.tests;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

/**
 * Email Service tests, which test that the out-of-the-box EmailWorkItemHandler can really deliver emails and also that
 * jbpm server can really benefit from the mail session configuration
 * 
 * There are numerous conditions which needs to be fulfilled before this test can be run:
 * 
 * 1) Mail session has to be configured in the jbpm server, in standalone-full.xml in a mail subsystem, like this:
 * 
 * <mail-session jndi-name="java:/mail/jbpmMailSession" from="anton@business.com"> <smtp-server
 * outbound-socket-binding-ref="jbpm-email"/> </mail-session>
 * 
 * with corresponding host/port config
 * 
 * <outbound-socket-binding name="jbpm-email"> <remote-destination host="localhost" port="1025"/>
 * </outbound-socket-binding>
 * 
 * 2) Following entries has to be added in
 * $JBPM_SERVER_HOME/standalone/deployments/kie-wb-distribution-wars-6.2.0.CR1-eap6_3
 * .war/WEB-INF/classes/userinfo.properties
 * 
 * Administrators=administrator@domain.com:en-UK:Administrators:[Administrator] anton=anton@business.com:en-UK:anton
 * 
 * 3)support/service-lifecycle-tests-1.jar has to be deployed in the jbpm server
 * 
 * 4) Some SMTP server has to run on localhost:1025 and needs to be able to forward email to the file, I have used
 * support/fakeSMTP-1.11.jar, like this:
 * 
 * java -jar fakeSMTP-1.11.jar -p 1025 -s -o
 * /home/osiris/Documents/Diplomka/gantt/5-Implementacia-a-testovanie-Nov-4-Nov-25/emails
 * 
 * -o value needs to match emailInbox attribute of this test
 * 
 * 
 * @author osiris
 * 
 */
public class EmailServiceTest {

	private static RemoteRuntimeEngine engine;
	private static String url = "http://localhost:8080/kie-wb-distribution-wars-6.2.0.CR1-eap6_3";
	private static final String username = "anton";
	private static final String password = "password1!";
	private static final String deploymentId = "org.fi.muni:service-lifecycle-tests:1";
	private static final String processId = "ServiceLifecycle-TestScenarios.Simpleemailtest";
	private static final String emailInbox = "/home/osiris/Documents/Diplomka/gantt/5-Implementacia-a-testovanie-Nov-4-Nov-25/emails";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		RemoteRestRuntimeEngineFactory factory = RemoteRestRuntimeEngineFactory.newBuilder().addDeploymentId(deploymentId).addUrl(new URL(url))
				.addUserName(username).addPassword(password).buildFactory();
		engine = factory.newRuntimeEngine();

	}

	@Test
	public void testEmailService() throws InterruptedException {

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("pSubject", "QA Tests Failed");
		inputParams.put("pBody", "Dear QA, I have noticed that latest tests for OrderService failed, can we schedule a meeting for a follow up?");
		inputParams.put("pTo", "qadepartment@business.com");
		inputParams.put("pFrom", "soateam@business.com");

		int numberOfEmailsBeforeProcessStarted = new File(emailInbox).listFiles().length;

		engine.getKieSession().startProcess(processId, inputParams);

		Thread.sleep(2000); // let the smtp server process the email

		int numberOfEmailsAfterProcessStarted = new File(emailInbox).listFiles().length;

		assertTrue(numberOfEmailsBeforeProcessStarted < numberOfEmailsAfterProcessStarted); // we should have +1 file
																							// which represents the
																							// email sent

	}

}
