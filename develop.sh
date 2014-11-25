## just for the development env to automatic deploy the project on localhost
rm -rf target/
mvn package -Dmaven.test.skip=true
#mvn package
rm -rf /Library/apache-tomcat-7.0.56/webapps/darfoobackend
rm /Library/apache-tomcat-7.0.56/webapps/darfoobackend.war
cp target/darfoobackend.war /Library/apache-tomcat-7.0.56/webapps
catalina.sh run
