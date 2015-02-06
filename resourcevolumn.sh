#i="uploadresources"
i="/usr/local/darfoo/apache-tomcat-7.0.47/bin/uploadresources"
j=`du -h "$i" | cut -f 1`
#echo $i size is $j
echo $j
