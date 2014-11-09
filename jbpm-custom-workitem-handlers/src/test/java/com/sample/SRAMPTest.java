package com.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.oasis_open.docs.s_ramp.ns.s_ramp_v1.BaseArtifactType;
import org.overlord.sramp.atom.err.SrampAtomException;
import org.overlord.sramp.client.SrampAtomApiClient;
import org.overlord.sramp.client.SrampClientException;
import org.overlord.sramp.client.ontology.OntologySummary;
import org.overlord.sramp.client.query.ArtifactSummary;
import org.overlord.sramp.common.ArtifactType;

/**
 * Tests related to the S-RAMP queries
 * 
 * @author osiris
 */
public class SRAMPTest {

	private static final String DEFAULT_ENDPOINT = "http://localhost:8080/s-ramp-server";
	private static final String DEFAULT_USER = "admin";
	private static final String DEFAULT_PASSWORD = "password1!";
	private final static String getAllServices = "/s-ramp/ext/SwitchYardService";

	private final static Logger log = Logger.getLogger(SRAMPTest.class.getName());

	 @Test
	 public void testOntologyImport() throws Exception {
	
	 log.info("testing ontology upload...");
	
	 SrampAtomApiClient client = new SrampAtomApiClient(DEFAULT_ENDPOINT, DEFAULT_USER, DEFAULT_PASSWORD, true);
	
	 List<OntologySummary> ontologies = client.getOntologies();
	 Map<String, String> ontologyUuids = new HashMap<String, String>();
	
	 // if test ontology has not been found, we need to upload it first
	 installTestOntology(client);
	
	 boolean foundOntology = false;
	 ontologies = client.getOntologies();
	
	 // let's check if the upload was successful
	 for (OntologySummary ontology : ontologies) {
	
	 log.info("ontology base:"+ontology.getBase());
	 log.info("ontology uuid:"+ontology.getUuid());
	
	 if (ontology.getBase().contains("http://www.jboss.org/overlord/service-lifecycle.owl")) {
	 log.info("there is a match");
	 ontologyUuids.put(ontology.getBase(), ontology.getUuid());
	 foundOntology = true;
	 break;
	 }
	 }
	
	 assertEquals("Ontology has not been found", true, foundOntology);
	
	 // now we need to delete it
	 client.deleteOntology(ontologyUuids.get("http://www.jboss.org/overlord/service-lifecycle.owl"));
	 ontologies = client.getOntologies();
	 foundOntology = false;
	
	 for (OntologySummary ontology : ontologies) {
	
	 if (ontology.getBase().contains("http://www.jboss.org/overlord/service-lifecycle.owl")) {
	
	 foundOntology = true;
	 break;
	 }
	 }
	 // let's see if it was successful
	 assertEquals("Ontology has been found  and it shouldn't been!", false, foundOntology);
	
	 }

	@Test
	public void testGetAllArtifacts() throws SrampClientException, SrampAtomException {

		log.info("testing getting all artifacts...");

		SrampAtomApiClient client = new SrampAtomApiClient(DEFAULT_ENDPOINT, DEFAULT_USER, DEFAULT_PASSWORD, true);

		// upload test artifact1 to S-RAMP repo
		InputStream content = SRAMPTest.class.getResourceAsStream("/test-artifact1.txt");
		ArtifactType type = ArtifactType.valueOf("SwitchYardService");
		BaseArtifactType metaData = client.uploadArtifact(type, content, "test-artifact1.txt");
		String artifact1UUID = metaData.getUuid();

		// upload test artifact2 to S-RAMP repo
		content = SRAMPTest.class.getResourceAsStream("/test-artifact2.txt");

		type = ArtifactType.valueOf("SwitchYardService");
		metaData = client.uploadArtifact(type, content, "test-artifact2.txt");
		String artifact2UUID = metaData.getUuid();

		Integer counter = 0;

		// get all SwitchYardService artifact
		for (ArtifactSummary sum : client.buildQuery(getAllServices).query()) {

			// if they have the same UUID we have the match
			if (sum.getUuid().equals(artifact1UUID) || sum.getUuid().equals(artifact2UUID))
				counter++;

		}

		assertEquals((Integer) 2, counter);

		client.deleteArtifact(artifact1UUID, type);
		client.deleteArtifact(artifact2UUID, type);

	}

	 @Test
	 public void testClassification() throws Exception {
	 log.info("testing classifying s-ramp artifact...");
	
	 SrampAtomApiClient client = new SrampAtomApiClient(DEFAULT_ENDPOINT, DEFAULT_USER, DEFAULT_PASSWORD, true);
	 installTestOntology(client);
	 Map<String, String> ontologyUuids = new HashMap<String, String>();
	 List<OntologySummary> ontologies = client.getOntologies();
	
	 for (OntologySummary ontology : ontologies) {
	
	 if (ontology.getBase().contains("http://www.jboss.org/overlord/service-lifecycle.owl")) {
	 ontologyUuids.put(ontology.getBase(), ontology.getUuid());
	 break;
	 }
	 }
	
	 // upload test artifact to S-RAMP repo
	 InputStream content = SRAMPTest.class.getResourceAsStream("/test-artifact3.txt");
	 ArtifactType type = ArtifactType.valueOf("Document");
	 BaseArtifactType metaData = client.uploadArtifact(type, content, "test-artifact3.txt");
	 String artifact1UUID = metaData.getUuid();
	
	 // classify artifact with our ontology
	 metaData.getClassifiedBy().add("http://www.jboss.org/overlord/service-lifecycle.owl#InTest");
	 client.updateArtifactMetaData(metaData);
	
	 // check if the artifact was really updated in the S-RAMP
	 BaseArtifactType artifact = client.getArtifactMetaData(type, metaData.getUuid());
	 assertNotNull("artifact is null", artifact);
	 assertEquals("http://www.jboss.org/overlord/service-lifecycle.owl#InTest", artifact.getClassifiedBy().get(0));
	
	 client.deleteArtifact(artifact1UUID, type);
	 client.deleteOntology(ontologyUuids.get("http://www.jboss.org/overlord/service-lifecycle.owl"));
	
	 }

	/**
	 * Installs (or ensures that it already exists) the Regions S-RAMP ontology in the S-RAMP repository.
	 * 
	 * @param client
	 * @throws Exception
	 */
	private static void installTestOntology(SrampAtomApiClient client) throws Exception {
		boolean alreadyExists = false;
		List<OntologySummary> ontologies = client.getOntologies();
		for (OntologySummary ontology : ontologies) {
			if ("http://www.jboss.org/overlord/service-lifecycle.owl".equals(ontology.getBase())) {
				alreadyExists = true;
				break;
			}
		}

		if (!alreadyExists) {
			InputStream resourceAsStream = SRAMPTest.class.getResourceAsStream("/service-lifecycle-ontology.xml");

			client.uploadOntology(resourceAsStream);
			IOUtils.closeQuietly(resourceAsStream);
			System.out.println("The 'service-lifecycle-ontology' ontology has been installed.");
		} else {
			System.out.println("The 'service-lifecycle-ontology' ontology was already installed (ok!).");
		}
	}

}
