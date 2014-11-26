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

Setting up JVM 3 - SOA Server
-----------------------------
- Make sure no other JBoss instances are running on the machine and that the port 8480 is open
- Place [jboss-eap.6.3.0.zip](http://www.jboss.org/download-manager/file/jboss-eap-6.3.0.GA.zip) into installation folder
- Place [switchyard-2.0.0.Alpha3-EAP6.3.0.GA.zip](http://downloads.jboss.org/switchyard/releases/v2.0.Alpha3/switchyard-2.0.0.Alpha3-EAP6.3.0.GA.zip) into installation folder and unzip it
- Place [s-ramp-0.6.0.Final.zip](http://downloads.jboss.org/overlord/sramp/s-ramp-0.6.0.Final.zip) into installation folder and unzip it
- Place [overlord-rtgov-2.0.0.Final.zip](http://downloads.jboss.org/overlord/rtgov/overlord-rtgov-2.0.0.Final.zip) into installation folder and unzip it
- create jvm3-soa-server folder and unzip jboss-eap.6.3.0.zip into in
- Install SwitchYard by following included README file
- Install RTGov by following included README file, don't forget to write down the passsword for 'admin' user.
- Install S-RAMP by following included README file - password for 'admin' user will be the same as the one you have specified during the RTGov installation
- Now you have to alter **s-ramp-init.txt** script with the proper username/password which you have configured for S-RAMP administrator, once done, you can run the post installation script which will configure the rest:
```
./post-install-soa-server.sh
```


You should see following output in the installation folder after all three JVMs are installed:

```
$ pwd 
~/service-lifecycle-mgmt/installation
$ tree -L 2 | grep jvm -A 1
|-- jvm1-jbpm
|   `-- jboss-eap-6.3
|-- jvm2-service-lifecycle
|   `-- jboss-eap-6.3
|-- jvm3-soa-server
|   |-- jboss-eap-6.3

```


Run the application
-------------------
- Once all three JVMs are set up, it's time to have some fun with the application!

Just execute the startup script
 ```
 ./start.sh
```

And browser window will be opened once the applications loads - it can take up to 2 minutes.

For turning off the application, execute the shutdown script:
```
./stop.sh
```
Next steps
-----------
- If you have carefully followed the steps above the application should be automatically opened in the web browser under following url:
```
http://localhost:8280/service-lifecycle-mgmt
```

Here is a list of available users and their roles which you can use:
```
anton=admin,analyst,user,SOAGovernanceSpecialist,ServiceAnalyst,ServiceDeveloper,QASpecialist
anton2=SOAGovernanceSpecialist
anton3=ServiceDeveloper,analyst
```

Password for *every* user is 'password1!'. Users with SOAGovernanceSpecialist role has access to the whole application. Other users has access only to the Lifecycle Tasks menu.

The S-RAMP repository is populated with OrderService, if you finish the Service Lifecycle bound to this service and make this Service Retired, then you can explore another
functionality - Notification Actions. It displays every invocations of a retired service and allows you to deal with them.
If you want to invoke OrderService then just execute following class:
```
jvm3-soa-server/jboss-eap-6.3/quickstarts/bean-service/src/test/java/org/switchyard/quickstarts/bean/service/BeanClient.java
```
Most likely, you will have to change port *inside* this class, so it points to the SOA server where the OrderService is deployed.

Application sends emails at few points of the Service Lifecycle. There **has to be smtp server installed** on the localhost and bind to port 1025 otherwise the application will not be able to proceed.
During the developemtnt following [fakesmtp server](http://nilhcem.github.com/FakeSMTP/downloads/fakeSMTP-latest.zip) was used and it is possible to use it like this:
```
java -jar fakeSMTP.jar -s -b -p 1025 -a 127.0.0.1 -o ~/emailInbox
```
The above command will start the smtp server (-s), without gui (-b), bind it to port 1025 (-p) and ip address 127.0.0.1 (-a) and store all the emails into ~/emailInbox directory (-o)


Properties file
-----------
There are three properties file used by the application, stored in src/main/resources folder:

 - **client.properties**
 
*database.jndi* = Identifies the datasource configured in:
 ```
jvm2-service-lifecycle/jboss-eap-6.3/standalone/configuration/standalone.xml
 ```
 This datasource is used for storing the processed retired service invocations.
 
 - **jbpm.properties**
 
jbpm.url = URL which points to the jBPM execution server is deployed

jbpm.processid.notification = process id of the BPMN process which sends email notification

jbpm.deploymentid = deployment id of the deployment unit which includes all business resources and is deployed in the jBPM execution server

jbpm.processid.newservice = process id of the BPMN process which is used for service lifecycle of a new service

jbpm.processid.existingservice = process id of the BPMN process which is used for service lifecycle of a existing service

- **rtgov.properties**

rtgov.url= URL which points to the RTGov server, which is used for monitoring invocations of retired services

rtgov.username = username

rtgov.password= password


 There are two workflows available for execution:
 
**Service Lifecycle - for new service** 
![Service Lifecycle - for new service](https://raw.githubusercontent.com/agiertli/service-lifecycle-mgmt/master/installation/newservice.png)
**Service lifecycle - existing service**
![Service lifecycle - existing service](https://raw.githubusercontent.com/agiertli/service-lifecycle-mgmt/master/installation/existingservice.png)
 
