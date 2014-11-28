package org.fi.muni.diploma.thesis.handlers;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.overlord.sramp.atom.err.SrampAtomException;
import org.overlord.sramp.client.SrampClientException;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This handler applies ontology classifier to an artifact with a given UUID stored in S-RAMP Repository
 * @author osiris
 *
 */
public class ApplyOntologyHandler implements WorkItemHandler {

	private final static Logger log = Logger.getLogger(ApplyOntologyHandler.class.getName());
	private SRAMPClient srampClient;

	public void abortWorkItem(WorkItem wi, WorkItemManager wm) {

	}

	public void executeWorkItem(WorkItem wi, WorkItemManager wm) {

		Map<String, Object> params = wi.getParameters();
		
		log.info("Executing ApplyOntologyHandler");

		try {
			srampClient = new SRAMPClient((String) params.get("inUsername"), (String) params.get("inPassword"), (Integer) params.get("inPort"),
					(String) params.get("inHost"));
		} catch (SrampClientException e) {
			e.printStackTrace();
		} catch (SrampAtomException e) {
			e.printStackTrace();
		}

		try {
			log.info("handler uuid:" + params.get("inUuid"));
			srampClient.classifyArtifact((String) params.get("inUuid"), (String) params.get("inValue"));
		} catch (SrampClientException e) {
			e.printStackTrace();
		} catch (SrampAtomException e) {
			e.printStackTrace();
		}

		wm.completeWorkItem(wi.getId(), null);

	}

}
