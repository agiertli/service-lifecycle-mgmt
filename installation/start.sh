cd jvm3-soa-server/jboss-eap-6.3/bin
./standalone.sh -Djboss.socket.binding.port-offset=400 --server-config=standalone-full.xml > /dev/null 2>&1 &
cd ../../..
cd jvm1-jbpm/jboss-eap-6.3/bin
./standalone.sh --server-config=standalone-full.xml -Dorg.jbpm.var.log.length=2147483647 > /dev/null 2>&1  &
cd ../../..
cd jvm2-service-lifecycle/jboss-eap-6.3/bin
./standalone.sh -Djboss.socket.binding.port-offset=200 > /dev/null 2>&1 &

running="false";
echo "..application is starting, wait up"

while [ $running == "false" ]; do
echo -n "."

sleep 10
if [[ "$(./jboss-cli.sh -c --controller=localhost:10199 --command='read-attribute server-state')" == "running" && "$(./jboss-cli.sh -c --controller=localhost:9999 --command='read-attribute server-state')" == "running" && "$(./jboss-cli.sh -c --controller=localhost:10399 --command='read-attribute server-state')" == "running" ]]; 
     then running="true"; fi;
done

echo "application started successfully..opening"

if which xdg-open > /dev/null
then
  xdg-open http://localhost:8280/service-lifecycle-mgmt/ > /dev/null 2>&1
elif which gnome-open > /dev/null
then
  gnome-open http://localhost:8280/service-lifecycle-mgmt/ > /dev/null 2>&1
fi
