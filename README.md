Service Lifecycle Management Application
======================

The application, as well as the installation has been tested with:

JDK 1.7.0_45 and Maven 3.0.3

The architecture consists of three JVMs

JVM1 = jbpm execution server

JVM2 = SOA server

JVM3 = server which hosts service lifecycle management application


all of these servers communicates with each other via REST API and must be set separately as that was the environment in which this application has been tested.


Prerequisite for the whole installation is, that the git repository is cloned and you have changed the directory into installation/ folder.

Setting up JVM 1 - jBPM Server
-------------------------------
- Place [jboss-eap.6.3.0.zip](http://www.jboss.org/download-manager/file/jboss-eap-6.3.0.GA.zip) into installation folder
- Place [kie-wb-distributions-wars-6.2.0.CR1-eap6_3.war](http://repository.jboss.org/nexus/content/groups/public-jboss/org/kie/kie-wb-distribution-wars/6.2.0.CR1/kie-wb-distribution-wars-6.2.0.CR1-eap6_3.war) into installation folder
- run installation script which will download and configure everything necessary

```
./installer-jbpm-server.sh

```
- start the jbpm server using following command 
```
cd /jvm1-jbpm/jboss-eap-6.3/bin
./standalone.sh --server-config=standalone-full.xml
```
- Once JBoss is started, turn it off - we only needed this step so the database was created successfully
- now alter freshly created database using following command:

```
java -cp jvm1-jbpm/jboss-eap-6.3/modules/system/layers/base/com/h2database/h2/main/h2-1.3.168.redhat-4.jar org.h2.tools.RunScript -user sa -password sa -url jdbc:h2:~/test -script database.sql

```
- We have just increased the size of a table column in which jbpm variables are going to be stored.
- Start the server using following command:

```
cd /jvm1-jbpm/jboss-eap-6.3/bin
./standalone.sh --server-config=standalone-full.xml -Dorg.jbpm.var.log.length=2147483647
```
- now the last step remains - we need to deploy bpmn processes which jbpm server will be executing. Execute following:
```
curl -u anton:password1! -X POST http://localhost:8080/kie-wb/rest/deployment/org.fi.muni.diploma.thesis:service-lifecycle:2.2.4:myBase:Session1/deploy
```

The request will be accepted immediatelly, without telling you if it was finished successfully or not. If you want check the status of deployment, wait a while - to allow server process the request and then execute following:
```
curl -u anton:password1! -X GET http://localhost:8080/kie-wb/rest/deployment/org.fi.muni.diploma.thesis:service-lifecycle:2.2.4:myBase:Session1
```

You should see following output:
```
{
    "groupId": "org.fi.muni.diploma.thesis",
    "artifactId": "service-lifecycle",
    "version": "2.2.4",
    "kbaseName": "myBase",
    "ksessionName": "Session1",
    "strategy": "SINGLETON",
    "status": "DEPLOYED"
}
```
If this is true, it means you have successfully installed and configured jBPM server.

