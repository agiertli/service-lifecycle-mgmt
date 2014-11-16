package org.fi.muni.diploma.thesis.utils.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class ApplicationUserProperties {

	private static final Logger logger = Logger.getLogger(ApplicationUserProperties.class.getName());

	private Properties properties;

	public ApplicationUserProperties() throws IOException {

		String dir = System.getProperty("jboss.server.config.dir");
		logger.info("dir:" + dir);

		FileInputStream is = new FileInputStream(dir + "/application-users.properties");

		Properties props = new Properties();
		props.load(is);

		this.properties = props;
	}

	public String getPassword(String username) {
		return this.properties.getProperty(username);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}
