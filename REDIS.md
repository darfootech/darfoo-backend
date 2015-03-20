##Redis Cluster Configuration
#Use twemproxy + 3 redis server
#参考网站
```
https://github.com/twitter/twemproxy
```

#Twemproxy install @CentOS
* get source (git clone git://github.com/twitter/twemproxy.git)
* 需要 automake  libtool  autoreconf(2.69及以上)
* install twemproxy
```
cd twemproxy
autoreconf -ivf (这边要注意系统也自带了个低版本的autoreconf,太坑了,所以注意用2.69那个版本的)
./configure --enable-debug=full
make
src/nutcracker -h(help command)
```
*安装完成

#Twemproxy configuration
*vim conf/nutcracker.yml
```
alpha:
  listen: 0.0.0.0:6000      #twemproxy listen port
  hash: fnv1a_64            #hash function
  distribution: ketama      #key distribution mode
  auto_eject_hosts: true    #need
  redis: true
  redis_auth: jihui
  timeout: 1000
  server_retry_timeout: 30000
  server_failure_limit: 1
  servers:
   - 192.168.1.33:6001:1    #redis server
   - 192.168.1.33:6002:1
   - 192.168.1.33:6003:1
```
*启动脚本（示例）nutcracker -d -c /usr/local/redis-proxy/twemproxy/conf/nutcracker.yml -i 500 -o /usr/local/redis-proxy/redis-proxy.log
（后台启动，配置文件，日志文件）


#Redis intsll
略（只要一个redis就可以了，配置文件弄多份）
#Redis Configuration
*产生3个配置文件，每个配置文件修改一下端口，然后将dump那边的save...注释掉（当然如果你想落盘，那么请为各个redis server分配一个dump文件名）
*改成后台启动模式 daemonize yes
*日志文件 logfile ../logs/redis-6001.log 日志级别选择为 loglevel notice
*启动脚本（单台示例） redis-server /usr/local/redis-proxy/redis-conf/redis-6001.conf


#TODO
*redis 的master-slave模式
*redis 监控
*redis 宕机重启
