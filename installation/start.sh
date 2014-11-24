cd jvm3-soa-server/jboss-eap-6.3/bin
./standalone.sh -Djboss.socket.binding.port-offset=400 --server-config=standalone-full.xml &
cd ../../..
cd jvm1-jbpm/jboss-eap-6.3/bin
./standalone.sh --server-config=standalone-full.xml -Dorg.jbpm.var.log.length=2147483647 &
cd ../../..
cd jvm2-service-lifecycle/jboss-eap-6.3/bin
./standalone.sh -Djboss.socket.binding.port-offset=200 &
