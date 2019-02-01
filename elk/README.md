## 准备三台虚拟机
- Linux版本为 centOS 7
- 准备 ip 地址分别为：
  - 192.168.61.134
  - 192.168.61.135
  - 192.168.61.136

- 修改主机名称
  - 三台机器的hosts配置为
```bash

# cat /etc/hosts
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         localhost localhost.localdomain localhost6 localhost6.localdomain6

master-node 192.168.61.134
data-node1 192.168.61.135
data-node2 192.168.61.136
```

- 配置静态IP地址：
  - 对应的ip地址修改192.168.61.134 192.168.61.135 192.168.61.136
  - 主要修改项
 ```
 BOOTPROTO="static"
 ONBOOT="yes"
 DNS1=192.168.61.2
 NETMASK=255.255.255.0
 GATEWAY=192.168.61.2
 IPADDR=192.168.61.134
 ```
 
```bash
[root@master-node ~]# cat /etc/sysconfig/network-scripts/ifcfg-ens33 
TYPE="Ethernet"
BOOTPROTO="static"
DEFROUTE="yes"
PEERDNS="yes"
PEERROUTES="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_PEERDNS="yes"
IPV6_PEERROUTES="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="cfddaf6c-a6b7-4a0f-a26e-5653b10ef895"
DEVICE="ens33"
ONBOOT="yes"
DNS1=192.168.61.2
NETMASK=255.255.255.0
GATEWAY=192.168.61.2
IPADDR=192.168.61.134
[root@master-node ~]# 
```
- 修改完成以后 修改主机名
```bash
[root@master-node ~]# cat /etc/hostname 
master-node
[root@master-node ~]# 
```

- 关闭开启防火墙命令
```
1、firewalld的基本使用
启动：       systemctl start firewalld
关闭：       systemctl stop firewalld
查看状态：   systemctl status firewalld 
开机禁用  ： systemctl disable firewalld
开机启用  ： systemctl enable firewalld
```

- systemctl是CentOS7的服务管理工具中主要的工具，它融合之前service和chkconfig的功能于一体。
```
启动一个服务：systemctl start firewalld.service
关闭一个服务：systemctl stop firewalld.service
重启一个服务：systemctl restart firewalld.service
显示一个服务的状态：systemctl status firewalld.service
在开机时启用一个服务：systemctl enable firewalld.service
在开机时禁用一个服务：systemctl disable firewalld.service
查看服务是否开机启动：systemctl is-enabled firewalld.service
查看已启动的服务列表：systemctl list-unit-files|grep enabled
查看启动失败的服务列表：systemctl --failed
```

- **环境准备结束**


## 角色划分
- 3台机器全部安装jdk1.8，因为elasticsearch是java开发的
- 3台全部安装elasticsearch (后续都简称为es)
- 192.168.61.134作为主节点
- 192.168.61.135以及192.168.61.136作为数据节点
- 主节点上安装kibana
- 192.168.61.135 上安装 logstash
- 192.168.61.136 上安装 redis 和filebeats

## ELK版本信息：

- Elasticsearch-6.0.0
- logstash-6.0.0
- kibana-6.0.0
- filebeat-6.0.0
- redis-4.0.6
## 注意yml 语言格式。接下来好多配置都会用到yml 语言
==例如 elasticsearch.yml：如果【cluster.name: master-node】为
【'空格'cluster.name:'去掉空格'master-node】将会识别错误==
```
cluster.name: master-node
node.name: master
node.master: true
node.data: false
network.host: 0.0.0.0
http.port: 9200
discovery.zen.ping.unicast.hosts: ["192.168.61.134", "192.168.61.135", "192.168.61.136"]
```

## 安装es
- 下载es
```bash

[root@master-node software]# wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.0.0.rpm
[root@master-node ~]# cd /usr/software/
[root@master-node software]# ll 
total 89796
-rw-r--r--. 1 root root 27970243 Dec  5 16:09 elasticsearch-6.0.0.rpm
-rw-r--r--. 1 root root 63979183 May 15  2018 kibana-6.0.0-x86_64.rpm
```

- 分别远程copy 到 data-node1 和data-node2两台服务器上

```bash
[root@master-node software]# scp elasticsearch-6.0.0.rpm 192.168.61.135:/usr/software/
root@192.168.61.135's password: 
```
```bash
[root@master-node software]# scp elasticsearch-6.0.0.rpm 192.168.61.136:/usr/software/
root@192.168.61.136's password: 
```

- 三台机器同时运行命令
```bash
[root@master-node ~]# rpm -ivh /usr/software/elasticsearch-6.0.0.rpm
```
## 配置ES
- 查看ES配置文件位置
```bash
[root@master-node ~]# ll /etc/elasticsearch/
total 16
-rw-rw----. 1 root elasticsearch 3080 Dec  5 17:36 elasticsearch.yml
-rw-rw----. 1 root elasticsearch 2678 Nov 11  2017 jvm.options
-rw-rw----. 1 root elasticsearch 5091 Nov 11  2017 log4j2.properties
[root@master-node ~]# 
```

```bash
[root@master-node ~]# ll /etc/sysconfig/elasticsearch 
-rw-rw----. 1 root elasticsearch 1593 Nov 11  2017 /etc/sysconfig/elasticsearch
[root@master-node ~]# 

```
- 修改主节点ES 配置
```bash
[root@master-node ~]# vim /etc/elasticsearch/elasticsearch.yml 

cluster.name: master-node
node.name: master
node.master: true
node.data: false
network.host: 0.0.0.0
http.port: 9200
discovery.zen.ping.unicast.hosts: ["192.168.61.134", "192.168.61.135", "192.168.61.136"]




# ======================== Elasticsearch Configuration =========================
```
- 修改数据节点
```bash
[root@data-node1 conf.d]# vim /etc/elasticsearch/elasticsearch.yml 

cluster.name: master-node
node.name: data-node1
node.master: false
node.data: true
network.host: 0.0.0.0
http.port: 9200
discovery.zen.ping.unicast.hosts: ["192.168.61.134", "192.168.61.135", "192.168.61.136"]



# ======================== Elasticsearch Configuration =========================
```

- 配置完成之后 先启动主节点 再启动其他节点
  - 命令如下
```bash
systemctl start elasticsearch.service
```

- 查看ES日志位置
```bash
[root@master-node ~]# ll /var/log/elasticsearch/
total 46928
-rw-r--r--. 1 elasticsearch elasticsearch     2684 Dec  6 09:08 master-node-2018-12-05-1.log.gz
-rw-r--r--. 1 elasticsearch elasticsearch     6399 Dec  7 09:10 master-node-2018-12-06-1.log.gz
-rw-r--r--. 1 elasticsearch elasticsearch     1844 Dec  7 09:58 master-node_deprecation.log
-rw-r--r--. 1 elasticsearch elasticsearch        0 Dec  5 16:41 master-node_index_indexing_slowlog.log
-rw-r--r--. 1 elasticsearch elasticsearch        0 Dec  5 16:41 master-node_index_search_slowlog.log
-rw-r--r--. 1 elasticsearch elasticsearch 40096081 Dec  7 11:09 master-node.log
[root@master-node ~]# 
```
- 如果没有生成查看系统日志
```bash
[root@master-node ~]# ll /var/log/messages 
-rw-------. 1 root root 1100754 Dec  7 11:10 /var/log/messages
[root@master-node ~]# 

[root@master-node ~]# tailf -n50 /var/log/messages 
Dec  7 09:44:32 master-node avahi-daemon[717]: Registering new address record for fe80::3b7e:6168:6fb7:1ae5 on ens33.*.
Dec  7 09:44:32 master-node avahi-daemon[717]: Registering new address record for 192.168.61.134 on ens33.IPv4.
Dec  7 09:44:32 master-node avahi-daemon[717]: Registering HINFO record with values 'X86_64'/'LINUX'.
```

- 由于系统采用的是系统jdk 所以没出现找不到jdk 错误
参照http://blog.51cto.com/zero01/2079879 方式解决错误

- 查看ES 启动进程
```bash
[root@master-node elasticsearch]# ps aux |grep elasticsearch
elastic+   2800  1.9 42.2 3258088 422356 ?      Ssl  09:10   2:54 /bin/java -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+AlwaysPreTouch -server -Xss1m -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Djna.nosys=true -XX:-OmitStackTraceInFastThrow -Dio.netty.noUnsafe=true -Dio.netty.noKeySetOptimization=true -Dio.netty.recycler.maxCapacityPerThread=0 -Dlog4j.shutdownHookEnabled=false -Dlog4j2.disable.jmx=true -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/lib/elasticsearch -Des.path.home=/usr/share/elasticsearch -Des.path.conf=/etc/elasticsearch -cp /usr/share/elasticsearch/lib/* org.elasticsearch.bootstrap.Elasticsearch -p /var/run/elasticsearch/elasticsearch.pid --quiet
root       5061  0.0  0.0 112648   964 pts/0    R+   11:42   0:00 grep --color=auto elasticsearch
[root@master-node elasticsearch]# ^C
[root@master-node elasticsearch]# 
```

- 查询集群健康情况
```bash
[root@master-node software]# curl '192.168.61.134:9200/_cluster/health?pretty'
{
  "cluster_name" : "master-node",
  "status" : "green",  # 为green则代表健康没问题，如果是yellow或者red则是集群有问题
  "timed_out" : false, # 是否有超时
  "number_of_nodes" : 3, # 集群中的节点数量
  "number_of_data_nodes" : 2, # 集群中data节点的数量
  "active_primary_shards" : 11,
  "active_shards" : 22,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 0,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0,
  "task_max_waiting_in_queue_millis" : 0,
  "active_shards_percent_as_number" : 100.0
}
[root@master-node software]# 
```
- 查看集群的详细信息：
```bash
[root@master-node software]#  curl '192.168.61.134:9200/_cluster/state?pretty'
```

## 部署kibana

- 下载并安装kibana 
```bash
[root@master-node software]# wget https://artifacts.elastic.co/downloads/kibana/kibana-6.0.0-x86_64.rpm
[root@master-node software]# ll
total 89796
-rw-r--r--. 1 root root 27970243 Dec  5 16:09 elasticsearch-6.0.0.rpm
-rw-r--r--. 1 root root 63979183 May 15  2018 kibana-6.0.0-x86_64.rpm
[root@master-node software]# rpm -ivh kibana-6.0.0-x86_64.rpm 

```
- 配置kibana 
```bash
[root@master-node ~]# vim /etc/kibana/kibana.yml 

# 配置kibana的端口
server.port: 5601
# # 配置监听ip
server.host: 192.168.61.134
# # 配置es服务器的ip，如果是集群则配置该集群中主节点的ip
elasticsearch.url: "http://192.168.61.134:9200"
# # 配置kibana的日志文件路径，不然默认是messages里记录日志  
logging.dest: /var/log/kibana.log
```
- 创建日志路径
```bash
[root@master-node ~]# touch /var/log/kibana.log; chmod 777 /var/log/kibana.log
```
- 启动kibana kibana是使用node 进行开发的
```bash
[root@master-node ~]# systemctl start kibana

[root@master-node ~]# ps aux |grep kibana
kibana     2978  0.4  8.0 1121712 80116 ?       Ssl  09:11   1:14 /usr/share/kibana/bin/../node/bin/node --no-warnings /usr/share/kibana/bin/../src/cli -c /etc/kibana/kibana.yml
root       6363  0.0  0.0 112648   960 pts/0    S+   13:38   0:00 grep --color=auto kibana
[root@master-node ~]# 

[root@master-node ~]#  netstat -lntp |grep 5601
tcp        0      0 192.168.61.134:5601     0.0.0.0:*               LISTEN      2978/node           
[root@master-node ~]# 
```
- 浏览器访问 
  - 浏览器里进行访问，如：http://192.168.61.134:5601/ ，由于我们并没有安装x-pack，所以此时是没有用户名和密码的，可以直接访问的：
  ![image](https://note.youdao.com/yws/api/personal/file/B898B96923B2417BB4363C9FDA5DCF16?method=download&shareKey=6364bfee2d6d38ad77c2852a3180dda0)

- kibana 安装完成


## rsyslog 配置
```bash
*.* @@192.168.77.130:10514
```
```bash
[root@data-node1 ~]# vim /etc/rsyslog.conf

# Turn off message reception via local log socket;
# local messages are retrieved through imjournal now.
$OmitLocalLogging on

# File to store the position in the journal
$IMJournalStateFile imjournal.state


#### RULES ####
*.* @@192.168.61.135:10514
# Log all kernel messages to the console.
# Logging much else clutters up the screen.
#kern.*                                                 /dev/console

```

- 启动rsyslog
```bash
[root@data-node1 ~]#  systemctl restart rsyslog
```

## logstash 部署

- 在135 机器上安装logstash
- 下载并安装
```bash
[root@data-node1 software]#wget https://artifacts.elastic.co/downloads/logstash/logstash-6.0.0.rpm
[root@data-node1 ~]# cd /usr/software/
[root@data-node1 software]# ll
total 137952
-rw-r--r--. 1 root root  27970243 Dec  5 16:09 elasticsearch-6.0.0.rpm
-rw-r--r--. 1 root root 113288712 May 15  2018 logstash-6.0.0.rpm

[root@data-node1 software]# rpm -ivh logstash-6.0.0.rpm 
```

- 安装完成之后，修改配置文件
- 切换到 /etc/logstash/conf.d/ 文件下
- 添加syslog.conf
```bash
[root@data-node1 software]# cd /etc/logstash/conf.d/
[root@data-node1 conf.d]# ll
total 8
-rw-r--r-- 1 root root 213 Dec  7 09:44 syslog.conf

[root@data-node1 conf.d]# cat syslog.conf 
input {  
  syslog {
    type => "system-syslog"  
    port => 10514    
  }
}
output { 
 elasticsearch {
         hosts => ["192.168.61.134:9200"]
         index => "system-syslog-%{+YYYY.MM}"
              }

}
[root@data-node1 conf.d]# 
```
- 测试logstash 配置文件是否正确
```bash
[root@data-node1 ~]# cd /usr/share/logstash/bin/
[root@data-node1 bin]# 
[root@data-node1 bin]#  ./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/syslog.conf --config.test_and_exit
Sending Logstash's logs to /var/log/logstash which is now configured via log4j2.properties
Configuration OK   # 为ok则代表配置文件没有问题
```
命令说明：

--path.settings 用于指定logstash的配置文件所在的目录

-f 指定需要被检测的配置文件的路径

--config.test_and_exit指定检测完之后就退出，不然就会直接启动了

- 启动logstash

```bash
[root@data-node1 ~]# systemctl start logstash
[root@data-node1 ~]#  ps aux |grep logstash
```

如果出现如下错误

```bash
python: SELinux is preventing /usr/sbin/rsyslogd from name_connect access on the tcp_socket port 10514.#012#012*****  Plugin connect_ports (92.2 confidence) suggests   *********************#012#012If you want to allow /usr/sbin/rsyslogd to connect to network port 10514#012Then you need to modify the port type.#012Do#012# semanage port -a -t 
```

- 改变设置为SELINUX=disabled 后重启服务reboot
```bash
[root@data-node1 ~]# vim /etc/sysconfig/selinux


# This file controls the state of SELinux on the system.
# SELINUX= can take one of these three values:
#     enforcing - SELinux security policy is enforced.
#     permissive - SELinux prints warnings instead of enforcing.
#     disabled - No SELinux policy is loaded.
SELINUX=disabled
# SELINUXTYPE= can take one of three two values:
#     targeted - Targeted processes are protected,
#     minimum - Modification of targeted policy. Only selected processes are protected. 
#     mls - Multi Level Security protection.
SELINUXTYPE=targeted
```

启动logstash后，进程是正常存在的，但是9600以及10514端口却没有被监听。于是查看logstash的日志看看有没有错误信息的输出，但是发现没有记录日志信息，那就只能转而去查看messages的日志，发现错误信息没有访问日志目录的权限

这是因为权限不够，既然是权限不够，那就设置权限即可：

```bash
[root@data-node1 ~]# chown logstash /var/log/logstash/logstash-plain.log 
[root@data-node1 ~]# ll !$
ll /var/log/logstash/logstash-plain.log
-rw-r--r-- 1 logstash root 7597 Mar  4 04:35 /var/log/logstash/logstash-plain.log
[root@data-node1 ~]# systemctl restart logstash

[root@data-node1 ~]# ll /var/lib/logstash/
total 4
drwxr-xr-x 2 root root  6 Mar  4 01:50 dead_letter_queue
drwxr-xr-x 2 root root  6 Mar  4 01:50 queue
-rw-r--r-- 1 root root 36 Mar  4 01:58 uuid
[root@data-node1 ~]# chown -R logstash /var/lib/logstash/
[root@data-node1 ~]# systemctl restart logstash
```
- 启动完成后进行查看如果监听的是127.0.0.1
```bash
[root@data-node1 ~]# netstat -lntp |grep 9600
tcp6       0      0 192.168.61.135:9600     :::*                    LISTEN      4434/java           
[root@data-node1 ~]# netstat -lntp |grep 10514
tcp6       0      0 :::10514                :::*                    LISTEN      4434/java           
[root@data-node1 ~]# 
```

- 需要修改logstash的监听ip 之后重启logstash
```bash
[root@data-node1 ~]# vim /etc/logstash/logstash.yml

http.host: "192.168.61.135"
```

- 完成logstash部署后回到主节点
```bash
[root@master-node ~]#  curl '192.168.61.134:9200/_cat/indices?v'
health status index                 uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   system-syslog-2018.12 WfWEXTQKRoeFdarzJ6l_3g   5   1      17757            0     11.8mb          5.9mb
green  open   .kibana               pPij8fhhRImxM264DKSzhA   1   1          3            0     23.9kb         11.9kb
[root@master-node ~]# 

[root@master-node ~]# curl -XGET '192.168.61.134:9200/system-syslog-2018.12?pretty'
```
- 删除索引
  - curl -XDELETE 'localhost:9200/system-syslog-2018.12'
- 至此logstash 部署完成
 


## 部署filebeat 和redis
- 安装redis参考如下

```bash
https://www.cnblogs.com/zuidongfeng/p/8032505.html
```
- 开启远程访问时需要修改
- 注释掉bind 127.0.0.1和修改protected-mode=yes 为protected-mode no

```bash
# following bind directive, that will force Redis to listen only into
# the IPv4 lookback interface address (this means Redis will be able to
# accept connections only from clients running into the same computer it
# is running).
#
# IF YOU ARE SURE YOU WANT YOUR INSTANCE TO LISTEN TO ALL THE INTERFACES
# JUST COMMENT THE FOLLOWING LINE.
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bind 127.0.0.1

# By default protected mode is enabled. You should disable it only if
# you are sure you want clients from other hosts to connect to Redis
# even if no authentication is configured, nor a specific set of interfaces
# are explicitly listed using the "bind" directive.
protected-mode no

```
- 如果出现下面情况 可以考虑删除此文件 再重新启动redis
```bash
[root@iZwz991stxdwj560bfmadtZ ~]# service redisd start
/var/run/redis_6379.pid exists, process is already running or crashed　
```
## 安装filebeat

```bash
[root@data-node2 software]# wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.0.0-x86_64.rpm
```

```bash
[root@data-node2 ~]# cd /usr/software/
[root@data-node2 software]# ll
total 39024
-rw-r--r--. 1 root root 27970243 Dec  5 16:23 elasticsearch-6.0.0.rpm
-rw-r--r--. 1 root root 11988378 May 15  2018 filebeat-6.0.0-x86_64.rpm
```


```bash
[root@data-node2 software]# rpm -ivh filebeat-6.0.0-x86_64.rpm 
```
- 配置filebeat
```bash
[root@data-node2 software]# cat /etc/filebeat/filebeat.yml
filebeat.prospectors:
# 输入日志类型
 - input_type: log 
   paths:
    - /var/log/messages
   fields:
     type: 220messages
   fields_under_root: true
 - input_type: log
   paths:
    - /var/log/secure
   fields:
     type: 220ssh
   fields_under_root: true
# 输出到redis
output.redis:
  hosts: ["192.168.61.136"]
  port: 6379
  password:   
  db: 6
  timeout: 5
  key: "redis-log"
[root@data-node2 software]# 
```

- 启动filebeat
```bash
[root@data-node2 software]# systemctl start filebeat
[root@data-node2 software]# ps axu |grep filebeat
root       2833  0.1  1.4 288160 14736 ?        Ssl  14:37   0:00 /usr/share/filebeat/bin/filebeat -c /etc/filebeat/filebeat.yml -path.home /usr/share/filebeat -path.config /etc/filebeat -path.data /var/lib/filebeat -path.logs /var/log/filebeat
root       2845  0.0  0.0 112648   956 pts/0    R+   14:37   0:00 grep --color=auto filebeat
[root@data-node2 software]# 
```


- 查看redis 中的数据

```bash
[root@data-node2 ~]# cd /usr/local/redis/src/
[root@data-node2 src]# ./redis-cli 
127.0.0.1:6379> select 6
OK
127.0.0.1:6379[6]> lrange redis 0 2
(empty list or set)
127.0.0.1:6379[6]> lrange redis-log 0 1
1) "{\"@timestamp\":\"2018-12-10T06:37:10.214Z\",\"@metadata\":{\"beat\":\"\",\"type\":\"doc\",\"version\":\"6.0.0\"},\"type\":\"220messages\",\"beat\":{\"hostname\":\"data-node2\",\"version\":\"6.0.0\",\"name\":\"data-node2\"},\"message\":\"Dec  7 18:00:42 data-node2 systemd: Stopping Daemon for power management...\",\"source\":\"/var/log/messages\",\"offset\":6627360}"
2) "{\"@timestamp\":\"2018-12-10T06:37:10.214Z\",\"@metadata\":{\"beat\":\"\",\"type\":\"doc\",\"version\":\"6.0.0\"},\"message\":\"Dec  7 18:00:42 data-node2 systemd: Stopping Disk Manager...\",\"type\":\"220messages\",\"beat\":{\"name\":\"data-node2\",\"hostname\":\"data-node2\",\"version\":\"6.0.0\"},\"source\":\"/var/log/messages\",\"offset\":6627421}"
127.0.0.1:6379[6]> 
```

## 接下来配置135 主机上的logstash 读取redis中的数据

- 由于已经配置过syslog.conf 
- 添加配置如下

```bash
Last login: Fri Dec  7 09:09:42 2018 from 192.168.61.1
[root@data-node1 ~]# cd /etc/logstash/conf.d/
[root@data-node1 conf.d]# ll
total 8
-rw-r--r-- 1 root root 392 Dec  7 09:44 redis.conf
-rw-r--r-- 1 root root 213 Dec  7 09:44 syslog.conf
[root@data-node1 conf.d]# cat redis.conf 
input {
        redis {
                type => "redislog"
                host => "192.168.61.136"
                port => "6379"
                db => "6"
                data_type => "list"
                key => "redis-log"
             }
}
output {
       elasticsearch {
           hosts => ["192.168.61.134:9200"]
           index => "redislog-%{+YYYY.MM.dd}"
                     }
}
[root@data-node1 conf.d]# 
```
- 切换到logstash bin 目录下执行如下命令 如果出现Configuration OK 就说明配置成功了
```bash
[root@data-node1 conf.d]# cd /usr/share/logstash/bin/
[root@data-node1 bin]# 
[root@data-node1 bin]# ./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/redis.conf --config.test_and_exit
Sending Logstash's logs to /var/log/logstash which is now configured via log4j2.properties
Configuration OK
[root@data-node1 bin]# 
```
- 重启logstash 使配置生效

```bash
[root@data-node1 ~]# systemctl restart rsyslog
```
## 至此全部配置完成


## 主节点上查看生成索引状态
```bash
[root@master-node elasticsearch]# curl 'localhost:9200/_cat/indices?v'
health status index                 uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   redislog-2018.12.07   X0ssM-GuSeqO9wC3ZW8S8g   5   1      21584            0     11.5mb          5.7mb
green  open   redislog-2018.12.11   At3GZoHQS5KgxxFfZXLisw   5   1       3254            0      1.3mb        641.6kb
green  open   system-syslog-2018.12 9QlGh6YxSh-QDdii3AYRWQ   5   1         90            0    579.8kb        289.9kb
green  open   redislog-2018.12.10   IChxi1LeSf-TYMj1QHPmQg   5   1       1914            0      1.4mb        803.7kb
green  open   .kibana               pPij8fhhRImxM264DKSzhA   1   1          3            0     54.5kb         27.2kb
[root@master-node elasticsearch]# 
```
## 在kibana 页面上配置索引
- ES与logstash能够正常通信后就可以去配置kibana了，浏览器访问192.168.61.134:5601，到kibana页面上配置索引：
- 配置系统索引
- ![image](https://note.youdao.com/yws/api/personal/file/B13C10E12B34491BBF5AFB1188AF43E2?method=download&shareKey=6bef9f0824eb1c899948020513fb3aa7) 
- ![image](https://note.youdao.com/yws/api/personal/file/C614508553FF4CAEA3A37A91746DE91A?method=download&shareKey=3d7050cce34329555cdca83f507cb8ba)
- 配置redis索引
- ![image](https://note.youdao.com/yws/api/personal/file/CEB3563784F9406C900DE1C431525CA2?method=download&shareKey=472db2874e70f21d761491e784e2e2b4)
- ![image](https://note.youdao.com/yws/api/personal/file/D718F0768F614E59BF3E4658C54783B3?method=download&shareKey=f738cfbf7e339005a3c18889ac2b778f)


