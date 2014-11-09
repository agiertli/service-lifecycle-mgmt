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

public class QuerySRAMPHandler implements WorkItemHandler {

    private final static Logger log = Logger.getLogger(QuerySRAMPHandler.class.getName());
    private SRAMPClient srampClient;

    public void abortWorkItem(WorkItem wi, WorkItemManager wm) {
        // TODO Auto-generated method stub
    	
    	

    }

    public void executeWorkItem(WorkItem wi, WorkItemManager wm) {
        // TODO Auto-generated method stub

        log.info("executing QuerySRAMPHandler");

        Map<String, Object> params = wi.getParameters();
       if (params == null) {
    	   log.info("param is null"); 
       } else {
    	   
    	   log.info("params size:"+params.size());
       }

        try {
        	
       
    //    	log.info((String) params.get("inPort")); //this will cause classcast exception
        	
        	Integer sPort = (Integer)params.get("inPort");
        	
        	log.info(sPort.toString());
        	
        //	Integer iPort = Integer.parseInt(sPort);
            srampClient = new SRAMPClient((String) params.get("inUsername"), (String) params.get("inPassword"),sPort,(String) params.get("inHost"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	log.info("Unable to create SRAMPClient");
            e.printStackTrace();
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            log.info("querysramphandler: [" + key + "] [" + value + "]");
        }
        List<org.fi.muni.diploma.thesis.service_lifecycle_model.Service> serviceList = null;
        try {
            serviceList = this.srampClient.getServices();
        } catch (SrampClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SrampAtomException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("OutServiceList", serviceList);

        wm.completeWorkItem(wi.getId(), result);

    }

}
