echo "importing jboss configuration"
cp -rf standalone-full-soa.xml jvm3-soa-server/jboss-eap-6.3/standalone/configuration/standalone-full.xml > /dev/null
cp -rf standalone.conf-soa jvm3-soa-server/jboss-eap-6.3/bin/standalone.conf > /dev/null
echo "building the bean-service quickstart so we have some sample data to work with"
cd jvm3-soa-server/jboss-eap-6.3/quickstarts/bean-service
mvn clean package install -Dmaven.test.skip=true

cd ../../../..
pwd
echo "copying jar file to deployment folder"
cp jvm3-soa-server/jboss-eap-6.3/quickstarts/bean-service/target/switchyard-bean-service.jar jvm3-soa-server/jboss-eap-6.3/standalone/deployments
touch jvm3-soa-server/jboss-eap-6.3/standalone/deployments/switchyard-bean-service.jar.dodeploy

echo "starting the jboss for further configuration"
./jvm3-soa-server/jboss-eap-6.3/bin/standalone.sh -Djboss.socket.binding.port-offset=400 --server-config=standalone-full.xml &

started="false";
while [ $started = "false" ]; do
echo "Waiting for jboss to start"
sleep 20

if grep -q "JBoss EAP 6.3.0.GA (AS 7.4.0.Final-redhat-19) started" jvm3-soa-server/jboss-eap-6.3/standalone/log/server.log ; then
    started="true";
fi

done

echo "copying s-ramp shell"

cp -rf ./s-ramp-0.6.0.Final/bin/s-ramp.sh jvm3-soa-server/jboss-eap-6.3/bin
cp -rf ./s-ramp-0.6.0.Final/bin/s-ramp-shell-0.6.0.Final.jar jvm3-soa-server/jboss-eap-6.3/bin

echo "deploying bean-service into S-RAMP repository"
cp -rf jvm3-soa-server/jboss-eap-6.3/standalone/deployments/switchyard-bean-service.jar jvm3-soa-server/jboss-eap-6.3/bin
cp -rf s-ramp-init.txt jvm3-soa-server/jboss-eap-6.3/bin
cd jvm3-soa-server/jboss-eap-6.3/bin
./s-ramp.sh -f s-ramp-init.txt

echo "killing the jboss"
JBOSS_PID=`ps a | grep "jboss.home.dir" | grep "jvm3-soa-server" | awk '{print $1}'`
kill -9 $JBOSS_PID
echo -e "soa server installed successfully! \n"

exit
