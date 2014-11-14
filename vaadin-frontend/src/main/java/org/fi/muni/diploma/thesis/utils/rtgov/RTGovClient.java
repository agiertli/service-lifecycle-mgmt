package org.fi.muni.diploma.thesis.utils.rtgov;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.fi.muni.diploma.thesis.utils.DatabaseUtil;
import org.fi.muni.diploma.thesis.utils.properties.RTGovProperties;
import org.overlord.rtgov.activity.model.ActivityType;
import org.overlord.rtgov.activity.model.soa.RequestReceived;

public class RTGovClient {
	private final static Logger logger = Logger.getLogger(RTGovClient.class.getName());

	private RTGovProperties properties;

	public RTGovClient(RTGovProperties properties) {
		this.properties = properties;
	}

	public Set<Notification> getRetiredInvocation(RetiredService service) throws IOException, NamingException, SQLException {

		DatabaseUtil dbUtil;

		// we are looking for invocation from the time when the service has been marked as retired until now
		String queryString = "/activity/events/?from=" + service.getRetirementTimestamp() + "&to=" + System.currentTimeMillis();

		// put it together with the RTGov server url
		java.net.URL queryUrl = new java.net.URL(properties.getUrl() + queryString);
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection) queryUrl.openConnection();
		String userPassword = properties.getUsername() + ":" + properties.getPassword();
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

		Set<Notification> result = new HashSet<Notification>();

		for (ActivityType activity : activities) {

			// we are interested only in RequestReceived type
			if (activity instanceof RequestReceived) {

				RequestReceived request = (RequestReceived) activity;
				String requestServiceName = request.getServiceType().substring(request.getServiceType().lastIndexOf("}") + 1);
				// logger.info("processed request service name:" + requestServiceName);

				// is the service invoked the one which has already been retired?
				if (requestServiceName.equals(service.getName())) {
					// put some necessary data

					dbUtil = new DatabaseUtil();
					ResultSet resultSet = null; // clear the previous result
					resultSet = dbUtil.findNotificationById(request.getTimestamp());

					// if invocation with this timestamp haven't already been processed, we can add it to the result
					if (!resultSet.isBeforeFirst()) {
						Notification n = new Notification(service, request.getTimestamp(), request.getInterface(), request.getOperation());

						result.add(n);
						logger.info("Adding a new notification:" + n.toString());
					}
					dbUtil.close();// never forget to close db connection..I guess..

				}
			}

		}
		return result;
	}
}
