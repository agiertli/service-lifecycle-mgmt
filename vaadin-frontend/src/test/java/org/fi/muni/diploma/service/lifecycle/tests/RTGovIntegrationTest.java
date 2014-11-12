package org.fi.muni.diploma.service.lifecycle.tests;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.overlord.rtgov.activity.model.ActivityType;
import org.overlord.rtgov.activity.model.soa.RequestReceived;
import org.switchyard.component.test.mixins.http.HTTPMixIn;

/**
 * Prerequisites:
 * 
 * RTGov and SwitchYard deployed on the same server & bean-service quickstart deployed
 * 
 * @author osiris
 * 
 */
public class RTGovIntegrationTest {
	private static final String XML = "src/test/resources/xml/soap-request.xml";

	@Test
	public void serviceInvocationTest() throws IOException, InterruptedException {

		long beforeInvocation = System.currentTimeMillis();
		System.out.println("before" + beforeInvocation);

		HTTPMixIn soapMixIn = new HTTPMixIn();
		soapMixIn.initialize();

		soapMixIn.postFile("http://localhost:8480/quickstart-bean/OrderService", XML);
		soapMixIn.uninitialize();

		Thread.sleep(5000); // we need to let RTGov process the invocation
		long afterInvocation = System.currentTimeMillis();

		// now, we should see some request within this time period
		java.net.URL queryUrl = new java.net.URL("http://localhost:8480/overlord-rtgov/activity/events?from=" + beforeInvocation + "&to="
				+ afterInvocation);
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
		
		
		for (ActivityType activity : activities) {
			
		if (activity instanceof org.overlord.rtgov.activity.model.soa.RequestReceived) {
			
			RequestReceived request = (RequestReceived) activity;
			
			System.out.println("service type:"+request.getServiceType());
			System.out.println("operation:"+request.getOperation());
			System.out.println("interface:"+request.getInterface());
			System.out.println("timestamp"+request.getTimestamp());
			System.out.println("provider:"+request.isServiceProvider());
		//	request.
			
		}
		
		}
		is.close();
		System.out.println(activities.size());
		assertNotNull(activities);

	}
}
