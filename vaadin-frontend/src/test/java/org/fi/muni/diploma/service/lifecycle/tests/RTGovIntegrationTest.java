package org.fi.muni.diploma.service.lifecycle.tests;

import java.io.IOException;

import javax.validation.constraints.AssertTrue;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.overlord.rtgov.activity.model.ActivityType;
import org.switchyard.component.test.mixins.http.HTTPMixIn;

import static org.junit.Assert.*;

/**
 * Prerequisites:
 * 
 * RTGov and SwitchYard deployed on the same server
 * bean-service quickstart deployed
 * @author osiris
 *
 */
public class RTGovIntegrationTest {
	private static final String XML = "src/test/resources/xml/soap-request.xml";

	@Test
	public void serviceInvocationTest() throws IOException, InterruptedException {

		
		long beforeInvocation = System.currentTimeMillis();
		System.out.println("before"+beforeInvocation);
		
		boolean success = false;
		
		HTTPMixIn soapMixIn = new HTTPMixIn();
		soapMixIn.initialize();

		 soapMixIn.postFile("http://localhost:8480/quickstart-bean/OrderService", XML);
		soapMixIn.uninitialize();
		
		Thread.sleep(5000); // we need to let RTGov process the invocation
		long afterInvocation = System.currentTimeMillis();

		//now, we should see some request within this time period
		java.net.URL queryUrl = new java.net.URL("http://localhost:8480/overlord-rtgov/activity/events?from="+beforeInvocation+"&to="+afterInvocation);
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection) queryUrl.openConnection();
		String userPassword = "admin" + ":" + "password1!";
		String encoding = org.apache.commons.codec.binary.Base64.encodeBase64String(userPassword.getBytes());
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setAllowUserInteraction(false);
		connection.setRequestProperty("Content-Type", "application/json");
		java.io.InputStream is = connection.getInputStream();

		ObjectMapper mapper = new ObjectMapper();

		java.util.List<ActivityType> activities = mapper.readValue(is, new TypeReference<java.util.List<ActivityType>>() {
		});
		is.close();
		System.out.println(activities.size());
        assertNotNull(activities);

	}
}
