Service Lifecycle Management Application
======================

This readme file describes the deployment process for this application - specifically for the Proof of Concept version.
The overall architecture looks like this:

JVM 1 - JBoss EAP 6.3 + jBPM execution server + database

JVM 2 - JBoss EAP 6.3 + Service Lifecycle Management Application based on Vaadin framework
 
      
JVM1 < ---REST API---- > JVM2

While technically it should be possible to deploy all components of this architecture on one single JVM,
since all the development has been done using two separate machines, this README will describe this approach.


Setting up JVM 1
----------------

1. [Download and unzip JBoss EAP 6.3](http://www.jboss.org/products/eap/download/)
2. run '$JBOSS_HOME/bin/add-user.sh' script, and add user with following characteristics:
```
username: anton (this value must match)
password: password1! (this value can be arbitrary)
roles: admin,analyst,user,SOAGovernanceSpecialist,ServiceAnalyst,ServiceDeveloper,QASpecialist (these roles must match)
```

