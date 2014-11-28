package org.fi.muni.diploma.thesis.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Configuration of the remote jBPM server
 * 
 * @author osiris
 * 
 */
public class JBPMProperties {
	private final static Logger logger = Logger.getLogger(JBPMProperties.class.getName());

	private URL url;
	private String deploymentId;
	private String processIdNewService;
	private String processIdExistingService;
	private String processIdNotification;

	public JBPMProperties() throws IOException {

		// load the properties file
		String resourceName = "jbpm.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		InputStream resourceStream = loader.getResourceAsStream(resourceName);
		props.load(resourceStream);

		// load the properties
		this.url = new URL(props.getProperty("jbpm.url"));
		this.deploymentId = props.getProperty("jbpm.deploymentid");
		this.processIdNewService = props.getProperty("jbpm.processid.newservice");
		this.processIdExistingService = props.getProperty("jbpm.processid.existingservice");
		this.processIdNotification = props.getProperty("jbpm.processid.notification");

		resourceStream.close();

	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getProcessIdNewService() {
		return processIdNewService;
	}

	public void setProcessIdNewService(String processIdNewService) {
		this.processIdNewService = processIdNewService;
	}

	public String getProcessIdExistingService() {
		return processIdExistingService;
	}

	public void setProcessIdExistingService(String processIdExistingService) {
		this.processIdExistingService = processIdExistingService;
	}

	public String getProcessIdNotification() {
		return processIdNotification;
	}

	public void setProcessIdNotification(String processIdNotification) {
		this.processIdNotification = processIdNotification;
	}

}
