package org.fi.muni.diploma.thesis.utils;

import java.io.IOException;
import java.util.logging.Logger;

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
	private static RemoteRuntimeEngine engine;
	private static JBPMProperties properties;

	private final static Logger logger = Logger.getLogger(RuntimeEngineWrapper.class.getName());

	protected RuntimeEngineWrapper() {
		// Exists only to defeat instantiation.
	}

	public static RuntimeEngineWrapper getInstance()
			throws IOException {
		if (instance == null) {
			instance = new RuntimeEngineWrapper();
			properties = new JBPMProperties();

			logger.info("building factory");

			RemoteRestRuntimeEngineFactory factory = RemoteRestRuntimeEngineFactory.newBuilder()
					.addDeploymentId(properties.getDeploymentId()).addUrl(properties.getUrl()).addUserName(properties.getUsername()).addPassword(properties.getPassword())
					.buildFactory();

			RuntimeEngineWrapper.setEngine(factory.newRuntimeEngine());

			try {

				KieSession kSession = RuntimeEngineWrapper.getEngine().getKieSession();

				if (kSession == null) {

					logger.info("ksession is null");
				}

			} catch (Exception e) {

				e.printStackTrace();

			}

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