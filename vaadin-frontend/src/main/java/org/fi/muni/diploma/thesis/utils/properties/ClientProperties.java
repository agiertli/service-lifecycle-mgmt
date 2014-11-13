package org.fi.muni.diploma.thesis.utils.properties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class ClientProperties {
	
	private final static Logger logger = Logger.getLogger(ClientProperties.class.getName());


	private String dsJndi; //datasource jndi
	
	public ClientProperties() throws IOException {

		// load the properties file
		String resourceName = "client.properties";
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		InputStream resourceStream = loader.getResourceAsStream(resourceName);
		props.load(resourceStream);
		
		this.dsJndi = props.getProperty("database.jndi");
	}

	public String getDsJndi() {
		return dsJndi;
	}

	public void setDsJndi(String dsJndi) {
		this.dsJndi = dsJndi;
	}

}
