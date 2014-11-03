Service Lifecycle Management Application
======================

This readme file describes the deployment process for this application - specifically for the Proof of Concept version.
The overall architecture looks like this:

JVM 1 - JBoss EAP 6.3 + jBPM execution server + database

JVM 2 - JBoss EAP 6.3 + Service Lifecycle Management Application based on Vaadin framework
 
      
JVM1 < ---REST API---- > JVM2

While technically it should be possible to deploy all components of this architecture on one single JVM,
since all the development has been done using two separate machines, this README will describe this approach.

Absolutely first thing, before attempting anything is to have this repository cloned locally on your filesystem. You can achieve it by issuing following command:

$ git clone https://github.com/agiertli/service-lifecycle-mgmt.git


Setting up JVM 1
----------------

 - [Download and unzip JBoss EAP 6.3](http://www.jboss.org/download-manager/file/jboss-eap-6.3.0.GA.zip)

 -  run '$JBOSS_HOME/bin/add-user.sh' script, and add user with following characteristics:

```
username: anton (this value must match)
password: password1! (this value can be arbitrary)
roles: admin,analyst,user,SOAGovernanceSpecialist,ServiceAnalyst,ServiceDeveloper,QASpecialist (these roles must match)
```

 -  [Download and unzip JBPM Execution Service 6.2.0.CR1](http://repository.jboss.org/nexus/content/groups/public-jboss/org/kie/kie-wb-distribution-wars/6.2.0.CR1/kie-wb-distribution-wars-6.2.0.CR1-eap6_3.war)
We need to make this war an  'exploded' one. So once unzipped, backup the original *.war file and add war extension to the freshly created directory.
 
 - Drop the directory under $JBOSS_HOME/standalone/deployments and create a empty file with filename

```
kie-wb-distribution-wars-6.2.0.CR1-eap6_3.war.dodeploy
```

- Open support folder and copy s-ramp-api-0.6.0.Final.jar, s-ramp-atom-0.6.0.Final.jar, s-ramp-client-0.6.0.Final.jar, handlers-0.0.1.jar under $JBOSS_HOME/standalone/deployments/kie-wb-distribution-wars-6.2.0.CR1-eap6_3.war/WEB-INF/lib

- Navigate to $JBOSS_HOME/bin folder and start the JBoss using this command: ./standalone.sh --server-config=standalone-full.xml 

- Navigate to localhost:8080/kie-wb-distribution-wars-6.2.0.CR1-eap6_3 and log into the workbench with the users created in previous step

- Select 'Upload' under Authoring->Artifactory and locate support/service-lifecycle-model-1.0.Final.jar file

- Repeat the same process for service-lifecycle-2.0.3.jar file

- Go back to workbench and navigate to Deploy - > Process deployments  and add new deployment unit with following details:

```
group id:org.fi.muni.diploma.thesis
artifact id:service-lifecycle
version: 2.0.3
Runtime strategy: Singleton
Kie Base Name: myBase
Kie Session name: Session1
```

That's it! You have successfully set up first JVM - deployment should be successful and to verify it, there should be bunch of entries under Process Management -> Process Definitions in the kie workbench
