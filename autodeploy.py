from fabric.api import *

env.roledefs = {
    'backendserver': ['root@112.124.68.27']
}

backend_project_dir = '/home/darfoo-backend'
war_dir = '/usr/local/darfoo/webapps/darfoobackend'
tomcat_one_dir = '/usr/local/darfoo/tomcat_bak1/bin/'
tomcat_two_dir = '/usr/local/darfoo/tomcat_bak2/bin/'

@roles('backendserver')
def killTomcatTask():
    with cd(backend_project_dir):
        run('git pull --rebase upstream release')
        run("./killtomcats.sh")

@roles('backendserver')
def updateCodeAndRebuild():
    with cd(backend_project_dir):
        run('rm -rf target/')
        run('mvn package -Dmaven.test.skip=true')
        run('rm -rf /usr/local/darfoo/webapps/darfoobackend')
        run('rm /usr/local/darfoo/webapps/darfoobackend.war')
        run('cp target/darfoobackend.war /usr/local/darfoo/webapps')
        run('mkdir /usr/local/darfoo/webapps/darfoobackend')
        run('cp target/darfoobackend.war /usr/local/darfoo/webapps/darfoobackend')
    with cd(war_dir):
        run('jar -xvf darfoobackend.war')
        run('rm darfoobackend.war')
        
@roles('backendserver')
def deploy():
    with cd(backend_project_dir):
        run('catalina.sh run')
    with cd(tomcat_one_dir):
        run('./catalina.sh run')
    with cd(tomcat_two_dir):
        run('./catalina.sh run')

def backend():
    execute(killTomcatTask)
