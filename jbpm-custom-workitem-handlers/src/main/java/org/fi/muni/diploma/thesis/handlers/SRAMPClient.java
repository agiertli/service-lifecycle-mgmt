package org.fi.muni.diploma.thesis.handlers;

import org.apache.commons.io.IOUtils;
import org.fi.muni.diploma.thesis.service_lifecycle_model.Service;
import org.oasis_open.docs.s_ramp.ns.s_ramp_v1.BaseArtifactType;
import org.overlord.sramp.atom.err.SrampAtomException;
import org.overlord.sramp.client.SrampAtomApiClient;
import org.overlord.sramp.client.SrampClientException;
import org.overlord.sramp.client.ontology.OntologySummary;
import org.overlord.sramp.client.query.ArtifactSummary;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Wrapper for SrampAtomApiClient
 *
 * @author osiris
 */
public class SRAMPClient {

    private SrampAtomApiClient client;
    private String username;
    private String password;
    private Integer port;
    private String host;

    private final static Logger log = Logger.getLogger(SRAMPClient.class.getName());

    /**
     * list of used queries
     */
    private final static String getAllServices = "/s-ramp/ext/SwitchYardService";
    private final static String applyOntology = "/s-ramp[@uuid = ?]";


    /**
     * Creates Sramp Atom Api Client provided by the Overlord
     *
     * @param username
     * @param password
     * @param port
     * @param host
     * @throws SrampClientException
     * @throws SrampAtomException
     */
    public SRAMPClient(String username, String password, Integer port, String host) throws SrampClientException, SrampAtomException {

        this.host = host;
        this.password = password;
        this.username = username;
        this.port = port;
        
        if (host != null) log.info("host:"+host);
        if (password != null) log.info("password:"+password);
        if (username != null) log.info("username:"+username);
        if (port != null) log.info("port:"+port);
        
        
        String endpoint = "http://" + this.getHost() + ":" + this.getPort().toString() + "/s-ramp-server";
        this.client = new SrampAtomApiClient(endpoint, this.username, this.password, true);

    }

    public void classifyArtifact(String UUID, String state) throws SrampClientException, SrampAtomException {

    	log.info("srampclient uuid:"+UUID);
    	
        this.installOntology();
        for (ArtifactSummary sum : client.buildQuery(applyOntology).parameter(UUID).query()) {


            BaseArtifactType metaData = this.client.getArtifactMetaData(sum);
            metaData.getClassifiedBy().add("http://www.jboss.org/overlord/service-lifecycle.owl#"+state);

            this.client.updateArtifactMetaData(metaData);

            List<String> ont = metaData.getClassifiedBy();

            for (String onto : ont) {

            	log.info("classificaion:" + onto);
            }

        }
    }

    public List<Service> getServices() throws SrampClientException, SrampAtomException {

        List<Service> serviceList = new ArrayList<Service>();


        for (ArtifactSummary sum : client.buildQuery(
                getAllServices).query()) {

            log.info("Artifact name:" + sum.getName());
            log.info("Artifact UUID:" + sum.getUuid());
            serviceList.add(new Service(sum.getName(), sum.getUuid()));

        }


        return serviceList;
    }

    /**
     * Installs (or ensures that it already exists) the Service Lifecycle S-RAMP ontology in
     * the S-RAMP repository.
     *
     * @param client
     * @throws SrampAtomException
     * @throws SrampClientException
     * @throws Exception
     */
    public void installOntology() throws SrampClientException, SrampAtomException {

        boolean alreadyExists = false;
        List<OntologySummary> ontologies = this.client.getOntologies();
        for (OntologySummary ontology : ontologies) {
            if ("http://www.jboss.org/overlord/service-lifecycle.owl".equals(ontology.getBase())) {
                alreadyExists = true;
                break;
            }
        }

        if (!alreadyExists) {
            InputStream resourceAsStream = SRAMPClient.class.getResourceAsStream("/service-lifecycle-ontology.xml");
            
            if (resourceAsStream == null) System.out.println("service-lifecycle-ontology.xml not found"); 
            
            client.uploadOntology(resourceAsStream);
            IOUtils.closeQuietly(resourceAsStream);
            log.info("The 'service-lifecycle-ontology.xml' ontology has been installed.");
        } else {
        	log.info("The 'regions.owl' ontology was already installed (ok!).");
        }

    }


    public SrampAtomApiClient getClient() {
        return client;
    }

    public void setClient(SrampAtomApiClient client) {
        this.client = client;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
