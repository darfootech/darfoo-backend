ssh -p 33333 oofrad@112.124.68.27   'cd /home/oofrad/darfoodatabase; \
                          mysqldump --extended-insert=FALSE --complete-insert=TRUE -uroot -p darfoo > darfoo.sql; \
                          git commit -a -m"backupdatabase";\
                          git push'

cd /Users/zjh/Documents/darfoo/darfoodatabase; git pull origin master
