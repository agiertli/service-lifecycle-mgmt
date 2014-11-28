package org.fi.muni.diploma.thesis.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.overlord.sramp.atom.err.SrampAtomException;
import org.overlord.sramp.client.SrampClientException;

/**
 * Handler which return list of available SwitchyardApplications stored in S-RAMP Repository
 * 
 * @author Anton Giertli
 * 
 */
public class QuerySRAMPHandler implements WorkItemHandler {

	private final static Logger log = Logger.getLogger(QuerySRAMPHandler.class.getName());
	private SRAMPClient srampClient;

	public void abortWorkItem(WorkItem wi, WorkItemManager wm) {

	}

	public void executeWorkItem(WorkItem wi, WorkItemManager wm) {

		Map<String, Object> params = wi.getParameters();

		try {
			log.info("Executing QuerySRAMPHandler");

			Integer sPort = (Integer) params.get("inPort");

			srampClient = new SRAMPClient((String) params.get("inUsername"), (String) params.get("inPassword"), sPort, (String) params.get("inHost"));
		} catch (Exception e) {

			e.printStackTrace();
		}

		List<org.fi.muni.diploma.thesis.service_lifecycle_model.Service> serviceList = null;
		try {
			serviceList = this.srampClient.getServices();
		} catch (SrampClientException e) {

			e.printStackTrace();
		} catch (SrampAtomException e) {

			e.printStackTrace();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("OutServiceList", serviceList);

		wm.completeWorkItem(wi.getId(), result);

	}

}
