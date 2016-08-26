#!/bin/bash
set -e
STEPCOLOR=4

##build with maven
tput setaf $STEPCOLOR;
echo build with maven
tput sgr0;
mvn -Ptest clean compile install

##copy the war file to tomcat server
tput setaf $STEPCOLOR;
echo move war file to target folder
tput sgr0;
mv com.jotunheim.mimir.web/target/test.war /Users/zhangle/dev/apache-tomcat-8.0.14/webapps/ROOT.war

##done
tput setaf $STEPCOLOR;
echo Done!
tput sgr0;
