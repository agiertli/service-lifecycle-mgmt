echo "unzipping jboss"
unzip jboss-eap-6.3.0.zip -d jvm2-service-lifecycle > /dev/null

echo "copying user/role/password configuration"
cp -rf application-roles.properties jvm2-service-lifecycle/jboss-eap-6.3/standalone/configuration/
cp -rf application-users.properties jvm2-service-lifecycle/jboss-eap-6.3/standalone/configuration/

echo "importing jboss configuration"
cp -rf standalone-service-lifecycle.xml jvm2-service-lifecycle/jboss-eap-6.3/standalone/configuration/standalone.xml > /dev/null

echo "building service lifecycle management application - depending on the content of your local repository and internet speed connection this can take up to 30 min"
cd ..
cd vaadin-frontend
mvn clean package install -Dmaven.test.skip=true

echo "moving the deployment war file"
cd ..
mv  vaadin-frontend/target/vaadin-frontend-1.0 installation/jvm2-service-lifecycle/jboss-eap-6.3/standalone/deployments > /dev/null
mv  installation/jvm2-service-lifecycle/jboss-eap-6.3/standalone/deployments/vaadin-frontend-1.0 installation/jvm2-service-lifecycle/jboss-eap-6.3/standalone/deployments/vaadin-frontend-1.0.war

echo "creating deployment marker file inside jboss"
touch installation/jvm2-service-lifecycle/jboss-eap-6.3/standalone/deployments/vaadin-frontend-1.0.war.dodeploy

echo "installation finished"
