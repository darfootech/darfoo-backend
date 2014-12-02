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

## 批量上传视频和音频文件

由于服务器带宽原因，上传大文件非常慢，如果让人肉上传人员一次提交信息一次上传一个视频单次上传会让人疯掉，所以就创建一个视频，教程，伴奏的时候，图片直接上传七牛，音频，视频都先保存在服务器上，然后每一天上传完之后由后台人员来用七牛的命令行上传工具批量上传一天上传的音频视频文件

```
cd $darfoo_home
qrsync conf.json
```

#### todo

* 自动化创建数据库和迁移数据表
