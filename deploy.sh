git add .
git commit -a
git push upstream release
fab -f autodeploy.py backend
