package org.fi.muni.diploma.thesis.handlers;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.overlord.sramp.atom.err.SrampAtomException;
import org.overlord.sramp.client.SrampClientException;

import java.util.Map;
import java.util.logging.Logger;

public class ApplyOntologyHandler implements WorkItemHandler {

    private final static Logger log = Logger.getLogger(ApplyOntologyHandler.class.getName());
    private SRAMPClient srampClient;

    public void abortWorkItem(WorkItem wi, WorkItemManager wm) {
        // TODO Auto-generated method stub

    }

    public void executeWorkItem(WorkItem wi, WorkItemManager wm) {
        // TODO Auto-generated method stub

        System.out.println("Executing Apply Ontology Handler");


        Map<String, Object> params = wi.getParameters();


        try {
            srampClient = new SRAMPClient((String) params.get("inUsername"), (String) params.get("inPassword"), (Integer) params.get("inPort"),
                    (String) params.get("inHost"));
        } catch (SrampClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SrampAtomException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
        	log.info("handler uuid:"+params.get("inUuid"));
            srampClient.classifyArtifact((String) params.get("inUuid"),(String)params.get("inValue"));
        } catch (SrampClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SrampAtomException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            log.info("[" + key + "] [" + value + "]");
        }

        wm.completeWorkItem(wi.getId(), null);

    }

}
