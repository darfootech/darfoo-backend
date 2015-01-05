## development env

* intellij IDEA 13.1.5
* spring mvc 3.2.0.RELEASE
* maven 3.2.3
* jdk 1.7.0_67
* apache-tomcat 7.0.56

## hibernate

* mysql中建立数据库 -> `CREATE DATABASE `darfoo` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;`
* 利用`/src/main/resources/dardoo.sql`建立数据表
* 运行`/src/main/java/com/springapp/mvc/App.java`来检查hibernate支持是否成功

## 持久化配置文件

in `src/main/resources/`

* cp datasource.properties.example datasource.properties
* cp redis.properties.example redis.properties
* cp recommend.conf.example recommend.conf

## 批量上传视频和音频文件

由于服务器带宽原因，上传大文件非常慢，如果让人肉上传人员一次提交信息一次上传一个视频单次上传会让人疯掉，所以就创建一个视频，教程，伴奏的时候，图片直接上传七牛，音频，视频都先保存在服务器上，然后每一天上传完之后由后台人员来用七牛的命令行上传工具批量上传一天上传的音频视频文件

```
cd $darfoo_home
qrsync conf.json
```

## Spring中加载多个propertyplaceholder出现错误
多个配置文件各自包含placeholder,后加载的配置文件无法加载placeholder中对应的prop文件
解决方法:

1. 单独写一个配置文件，专门用来加载props
2. 网上找到的方法，设置ignoreUnresolvablePlaceholders属性为true [solution](http://blog.sina.com.cn/s/blog_82a09f100101as0i.html)

## 压力测试

* webbench(debian/ubuntu/centos)

```
# install
sudo apt-get intall ctags
wget  http://home.tiscali.cz/~cz210552/distfiles/webbench-1.5.tar.gz
tar zxvf webbench-1.5.tar.gz
cd webbench-1.5
make
sudo make install

# how to use
webbench -c 100 -t 30 http://112.124.68.27/rest/resources/video/index #在30秒内用100个并发连接去请求视频首页
```

然后直接在命令行中看压力测试结果就好，这个工具较老但是使用比较简单

[需要关闭ddos保护](http://help.aliyun.com/view/11108300_13444171.html)

## 线上部署

```
./product.sh
```

```
ps aux | grep tomcat
```

看三个tomcat进程是否已经全部停止

分别进入`darfoo`,`darfoo1`,`darfoo2`三个session运行三个tomcat的`catalina.sh run`这样既有实时log又能看现在流量压到哪一个tomcat上

## redis监控

* Gemfile

```
source 'https://ruby.taobao.org/'
gem 'redis-stat', '0.4.8'
```

and

```
bundle install
```

then

```
redis-stat -a password --server
```

and visit the `serverhost:63790` to get redis server info on the web or just watch it on the command line

## 服务器进程监控(tomcat)

* 使用newrelic for java

references

* [new-relic-java](https://docs.newrelic.com/docs/agents/java-agent/getting-started/new-relic-java)
* [java-agent-self-installer](https://docs.newrelic.com/docs/agents/java-agent/installation/java-agent-self-installer)
* [setup](https://rpm.newrelic.com/accounts/856739/applications/setup)

```
unzip newrelic-java-3.12.1.zip -d $tomcat-root-path
cd $tomcat-root-path/newrelic
java -jar newrelic.jar install
# modify newrelic.yml -> change app name
# restart tomcat process
```

## load test

* tools

[gobench](https://github.com/cmpxchg16/gobench)
[wrk](https://github.com/wg/wrk)

* use wrk examples

```
./wrk -c 10 -t 4 -d 10 http://localhost:8080/darfoobackend/rest/loadtest/normal/nocache/1
./wrk -c 10 -t 4 -d 10 http://localhost:8080/darfoobackend/rest/loadtest/normal/cache/1
./wrk -c 10 -t 4 -d 10 http://localhost:8080/darfoobackend/rest/loadtest/async/nocache/1
./wrk -c 10 -t 4 -d 10 http://localhost:8080/darfoobackend/rest/loadtest/async/cache/1
./wrk -c 10 -t 4 -d 10 http://localhost:8080/darfoobackend/rest/loadtest/deferred/nocache
./wrk -c 10 -t 4 -d 10 http://localhost:8080/darfoobackend/rest/loadtest/deferred/cache
./wrk -c 10 -t 4 -d 10 http://localhost/darfoobackend/rest/loadtest/normal/cache/1
./wrk -c 10 -t 4 -d 10 http://localhost/darfoobackend/rest/loadtest/async/cache/1
```

## workflow

* 有时候(具体啥情况会出现还不知)如果自己的`pull request`还没有被merge那么使用

```
git pull --rebase origin master
```

还是会出现冲突

所以一种比较好的方式是工作之前先看看自己的pr有没有被合并，如果被合并了就使用

```
git pull --rebase origin master
```

如果没有被合并就使用

```
git pull origin master
```

* 如何安全的工作

每次编码之前先检查自己的pr是否被merge

然后在本地开一个测试分支，然后再pull或者pull --rebase

```
git checkout -b localtest
git pull / pull --rebase origin master
```

解决了冲突以后切换回自己的开发分支，然后合并测试分支，然后push远端开发分支

```
git checkout xx_dev
git merge localtest
git branch -D localtest
git push origin xxx_dev
```

* 解决冲突

这些冲突都需要在localtest中解决

有时候(具体出现原因还在调查)如果自己的pr还没有被merge就使用pull --rebase会产生冲突，这个时候需要去掉这些新的冲突回到rebase之前然后使用git pull

```
git rebase --abort # 去掉因为错误rebase而导致的冲突
```

使用git pull会产生冲突，解决方法

```
git pull origin master
modify conflict files
git commit 
```

如果自己的pr被merge了，使用pull --rebase还是会有冲突，解决方法

```
git pull --rebase origin master
modify conflict files
git add .
git rebase --continue # 如果所有冲突都解决了，continue之后会从一个怪异的分支回到之前的分支
```

时刻用`git status`查看当前状态和提示信息

开始写代码之前pull一次，写完代码准备push之前pull一次

## todo

#### 自动化创建数据库和迁移数据表

#### 并发

* nginx + multi tomcat
* jetty netty
* redis缓存 (关于redis的使用，初步思路就是请求过的链接就把结果放redis里面，后面再请求相同的就直接从redis里拿，定时flush) 一些搜索结果更应该放入缓存，耗时的级联查询等等
* 数据库读写分离，多个主从mysql服务
* 建立mysql连接池也能抗一抗
* other techstack(已经测试了其他两种方案，效果不错)
* 升级带宽
* 添置服务器一台肯定不够
* jvm参数调整
* nginx参数配置
* 后面很有可能改成play或者go来些service(看下nginx能不能分发确定的请求)

#### security

* safe dog on linux
* https for auth

#### 日志服务(etl)

做数据分析 记录所有行为 点击 播放 异常

#### 非service 一个线上site

* angular no jsp jstl anymore 任务完全移交前端组

## how to deploy

* 数据库和缓存层配置文件

```
cp datasource.properties.example datasource.properties
cp redis.properties.example redis.properties
```

* 开启redis

```
redis-server
```

* 开始部署

```
./product.sh
```
