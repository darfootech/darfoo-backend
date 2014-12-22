## just for the production env to automatic deploy the project on remote server
rm -rf target/
mvn package -Dmaven.test.skip=true
#mvn package
#rm -rf /usr/local/darfoo/apache-tomcat-7.0.47/webapps/darfoobackend
#rm /usr/local/darfoo/apache-tomcat-7.0.47/webapps/darfoobackend.war
#cp target/darfoobackend.war /usr/local/darfoo/apache-tomcat-7.0.47/webapps
rm -rf /usr/local/darfoo/webapps/darfoobackend
rm /usr/local/darfoo/webapps/darfoobackend.war
cp target/darfoobackend.war /usr/local/darfoo/webapps
catalina.sh run
