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

- Make sure no other JBoss instances are running on the machine and that the port 8080 is open
- Place [jboss-eap.6.3.0.zip](http://www.jboss.org/download-manager/file/jboss-eap-6.3.0.GA.zip) into installation folder
- Place [kie-wb-distributions-wars-6.2.0.CR1-eap6_3.war](http://repository.jboss.org/nexus/content/groups/public-jboss/org/kie/kie-wb-distribution-wars/6.2.0.CR1/kie-wb-distribution-wars-6.2.0.CR1-eap6_3.war) into installation folder
- run installation script which will download and configure everything necessary:

```
./installer-jbpm-server.sh
```


Setting up JVM 2 - Service Lifecycle Management Application Server
------------------------------------------------------------------

- Make sure no other JBoss instances are running on the machine and that the port 8080 is open
- Place [jboss-eap.6.3.0.zip](http://www.jboss.org/download-manager/file/jboss-eap-6.3.0.GA.zip) into installation folder
- run the installation script:
```
./installer-service-lifecycle-mgmt-server.sh
```


