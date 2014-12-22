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
webbench -c 500 -t 30 http://targeturl # 在30秒内用500个并发连接去请求目标url
```

然后直接在命令行中看压力测试结果就好，这个工具较老但是使用比较简单

[需要关闭ddos保护](http://help.aliyun.com/view/11108300_13444171.html)

## todo

#### 自动化创建数据库和迁移数据表

#### 并发

* nginx + multi tomcat
* jetty
* redis缓存 (关于redis的使用，初步思路就是请求过的链接就把结果放redis里面，后面再请求相同的就直接从redis里拿，定时flush) 一些搜索结果更应该放入缓存，耗时的级联查询等等
* 数据库读写分离，多个主从mysql服务
* 建立mysql连接池也能抗一抗

#### 日志服务(etl)

做数据分析 记录所有行为 点击 播放 异常

#### 非service 一个线上site

* angular no jsp jstl anymore 任务完全移交前端组
