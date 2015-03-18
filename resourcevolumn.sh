#i="uploadresources"
i="/mnt/darfoo/uploadresources"
j=`du -h "$i" | cut -f 1`
#echo $i size is $j
echo $j
