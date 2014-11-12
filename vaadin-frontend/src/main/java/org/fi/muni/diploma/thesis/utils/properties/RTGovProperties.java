package org.fi.muni.diploma.thesis.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class RTGovProperties {
	
	private final static Logger logger = Logger.getLogger(RTGovProperties.class.getName());

	
	private URL url;
	private String username; // user which will be logging into business-central
	private String password;
	
	public RTGovProperties() throws IOException {
		
		// load the properties file
		String resourceName = "rtgov.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		InputStream resourceStream = loader.getResourceAsStream(resourceName);
		props.load(resourceStream);
		
		// load the properties
		this.url = new URL(props.getProperty("rtgov.url"));
		this.username = props.getProperty("rtgov.username");
		this.password = props.getProperty("rtgov.password");
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
