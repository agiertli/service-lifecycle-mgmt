cd jvm3-soa-server/jboss-eap-6.3/bin
./jboss-cli.sh --connect controller=localhost:10399 command=:shutdown > /dev/null 2>&1
cd ../../..
cd jvm1-jbpm/jboss-eap-6.3/bin
./jboss-cli.sh --connect controller=localhost:9999 command=:shutdown > /dev/null 2>&1
cd ../../..
cd jvm2-service-lifecycle/jboss-eap-6.3/bin
./jboss-cli.sh --connect controller=localhost:10199 command=:shutdown > /dev/null 2>&1
