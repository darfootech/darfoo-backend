mvn package
rm -rf /usr/local/darfoo/apache-tomcat-7.0.47/webapps/darfoobackend
rm /usr/local/darfoo/apache-tomcat-7.0.47/webapps/darfoobackend.war
cp target/darfoobackend.war /usr/local/darfoo/apache-tomcat-7.0.47/webapps
catalina.sh run
