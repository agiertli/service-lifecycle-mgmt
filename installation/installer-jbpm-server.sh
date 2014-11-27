
#unzip jboss
#copy standalone-full.xml (because of the file datasource)
#copy application users and properties
#start the jboss
#execute sql script http://www.java2s.com/Code/JarDownload/h2/h2-1.3.168.jar.zip
#kill the jboss
#unzip war
#create dodeploy file
#copy war file
#start the jboss using standalone command

echo "unzipping jboss"
unzip jboss-eap-6.3.0.zip -d jvm1-jbpm > /dev/null
echo "importing jboss configuration"
cp -f standalone-full-jbpm.xml jvm1-jbpm/jboss-eap-6.3/standalone/configuration/standalone-full.xml > /dev/null
echo "unzipping jbpm"
unzip kie-wb-distribution-wars-6.2.0.CR1-eap6_3.war -d kie-wb.war> /dev/null
echo "moving jbpm inside deployment directory"
mv kie-wb.war jvm1-jbpm/jboss-eap-6.3/standalone/deployments/
echo "creating deployment marker file inside jboss"
touch jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war.dodeploy
echo "Downloading s-ramp libraries"
wget https://repository.jboss.org/nexus/content/groups/public/org/overlord/sramp/s-ramp-api/0.6.0.Final/s-ramp-api-0.6.0.Final.jar
wget https://repository.jboss.org/nexus/content/groups/public/org/overlord/sramp/s-ramp-client/0.6.0.Final/s-ramp-client-0.6.0.Final.jar
wget https://repository.jboss.org/nexus/content/groups/public/org/overlord/sramp/s-ramp-atom/0.6.0.Final/s-ramp-atom-0.6.0.Final.jar
wget https://repository.jboss.org/nexus/content/groups/public/org/overlord/sramp/s-ramp-common/0.6.0.Final/s-ramp-common-0.6.0.Final.jar
echo "moving sramp libraries into jbpm installation"
mv s-ramp-api-0.6.0.Final.jar jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war/WEB-INF/lib/
mv s-ramp-client-0.6.0.Final.jar jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war/WEB-INF/lib/
mv s-ramp-atom-0.6.0.Final.jar jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war/WEB-INF/lib/
mv s-ramp-common-0.6.0.Final.jar jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war/WEB-INF/lib/

echo "building jbpm model classes"
cd ..
cd service-lifecycle-pojos
mvn clean package install -Dmaven.test.skip=true

echo "building jbpm work item handlers"
cd ..
cd jbpm-custom-workitem-handlers
mvn clean package install -Dmaven.test.skip=true

echo "building bpmn processes" 
cd ..
cd service-lifecycle-bpmn-processes
mvn clean install -Dmaven.test.skip=true

echo "copying jbpm model classes into jbpm installation"
cd ../installation
cp -rf ../service-lifecycle-pojos/target/service-lifecycle-model-1.0.Final.jar jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war/WEB-INF/lib/

echo "copying jbpm handlers into the jbpm installation"
cp -rf ../jbpm-custom-workitem-handlers/target/handlers-0.0.1.jar jvm1-jbpm/jboss-eap-6.3/standalone/deployments/kie-wb.war/WEB-INF/lib/

echo "copying user/role/password configuration"
cp -rf application-roles.properties jvm1-jbpm/jboss-eap-6.3/standalone/configuration/
cp -rf application-users.properties jvm1-jbpm/jboss-eap-6.3/standalone/configuration/

started="false";
cd jvm1-jbpm/jboss-eap-6.3/bin
echo "starting the jboss for further configuration"
./standalone.sh --server-config=standalone-full.xml & > /dev/null
cd ../standalone

while [ $started = "false" ]; do
echo "Waiting for jboss to start"
sleep 20

if grep -q "JBoss EAP 6.3.0.GA (AS 7.4.0.Final-redhat-19) started" log/server.log ; then
    started="true";
fi

done

echo "JBoss successfully started"
echo "deploying kjar to jbpm execution server"
curl -s -u anton:password1! -X POST http://localhost:8080/kie-wb/rest/deployment/org.fi.muni.diploma.thesis:service-lifecycle:2.4.0:myBase:Session1/deploy

deployed="false"
while [ $deployed = "false" ]; do
echo "Waiting for deployment to complete"
sleep 10
if 
curl -s -u anton:password1! -X GET http://localhost:8080/kie-wb/rest/deployment/org.fi.muni.diploma.thesis:service-lifecycle:2.4.0:myBase:Session1 | grep -q "<status>DEPLOYED</status>"; then
deployed="true"
fi
done
echo "deployment complete"
echo "killing jboss instance"
#JBOSS_PID=`ps a | grep "jboss.home.dir" | grep "jvm1-jbpm" | awk '{print $1}'`
#kill -9 $JBOSS_PID
cd ../../../
./jvm1-jbpm/jboss-eap-6.3/bin/jboss-cli.sh --connect controller=localhost:9999 command=:shutdown > /dev/null 2>&1
echo "configuring database"
java -cp jvm1-jbpm/jboss-eap-6.3/modules/system/layers/base/com/h2database/h2/main/h2-1.3.168.redhat-4.jar org.h2.tools.RunScript -user sa -password sa -url jdbc:h2:~/test -script database.sql 
echo -e "jbpm server installed successfully! \n"

exit

