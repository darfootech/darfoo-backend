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

mkdir /usr/local/darfoo/webapps/darfoobackend
cp target/darfoobackend.war /usr/local/darfoo/webapps/darfoobackend
cd /usr/local/darfoo/webapps/darfoobackend
jar -xvf darfoobackend.war
rm darfoobackend.war
ps -ef | grep tomcat | awk '{print $2}' | xargs kill -9;
/usr/local/darfoo/tomcat_bak1/bin/startup.sh
/usr/local/darfoo/tomcat_bak2/bin/startup.sh
/usr/local/darfoo/apache-tomcat-7.0.47/bin/startup.sh
#catalina.sh run

catalina.sh run

