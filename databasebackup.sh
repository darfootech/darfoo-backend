ssh root@112.124.68.27   'cd /home/darfoodatabase; \
                          mysqldump --extended-insert=FALSE --complete-insert=TRUE -uroot -p darfoo > darfoo.sql; \
                          git commit -a -m"backupdatabase";\
                          git push'

cd /Users/zjh/Documents/darfoo/darfoodatabase; git pull
