#!/bin/bash

##build with maven
mvn -Prelease clean compile install

##upload the war file to tomcat server
scp -P443 ./com.jotunheim.mimir.web/target/release.war zhangl@bil1:~/octopus_tomcat_18080/ROOT.war

##done
echo "File uploaded to tomcat, now you can operate on the server side."

