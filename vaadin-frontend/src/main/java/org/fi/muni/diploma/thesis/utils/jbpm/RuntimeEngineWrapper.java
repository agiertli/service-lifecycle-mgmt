package org.fi.muni.diploma.thesis.utils.jbpm;

import java.io.IOException;
import java.util.logging.Logger;

import org.fi.muni.diploma.thesis.utils.properties.JBPMProperties;
import org.kie.api.runtime.KieSession;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

/**
 * Wrapper of RemoteRuntimeEngine which gives us access to all important services such as
 * 
 * - TaskService - AuditLogService - KieSession
 * 
 * Implemented as simple Singleton, we only need one instance for one deployment
 * 
 * @author Anton Giertli
 * 
 */
public class RuntimeEngineWrapper {

	private static RuntimeEngineWrapper instance = null;
	private static RemoteRuntimeEngine engine; // provides access to KieSession / TaskService / AuditService
	private static JBPMProperties properties;
	private static String username;
	@SuppressWarnings("unused")
	private static String password;

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(RuntimeEngineWrapper.class.getName());

	protected RuntimeEngineWrapper() {
		// Exists only to defeat instantiation.
	}

	public static RuntimeEngineWrapper getInstance() {

		return instance;
	}

	public static RuntimeEngineWrapper getInstance(String username, String password) throws IOException {

		// if another user is logged, create new runtimeengine for him
		if (instance == null || RuntimeEngineWrapper.username != username) {
			instance = new RuntimeEngineWrapper();
			properties = new JBPMProperties();

			RemoteRestRuntimeEngineFactory factory = RemoteRestRuntimeEngineFactory.newBuilder().addDeploymentId(properties.getDeploymentId())
					.addUrl(properties.getUrl()).addUserName(username).addPassword(password).buildFactory();

			RuntimeEngineWrapper.username = username;
			RuntimeEngineWrapper.password = password;

			RuntimeEngineWrapper.setEngine(factory.newRuntimeEngine());

		}
		return instance;
	}

	public static RemoteRuntimeEngine getEngine() {
		return engine;
	}

	public static void setEngine(RemoteRuntimeEngine engine) {
		RuntimeEngineWrapper.engine = engine;
	}

	public static JBPMProperties getProperties() {
		return properties;
	}

	public static void setProperties(JBPMProperties properties) {
		RuntimeEngineWrapper.properties = properties;
	}

}
