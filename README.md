## spring-cloud-docker

## docker 安装


### 删除老版本
在CentOS中，老版本Docker名称是 docker 或 docker-engine ，而Docker CE的软件包名称是 docker-ce 。因此，如已安装过老版本的Docker，需使用如下命令卸载。
```
sudo yum remove docker \

                  docker-common \

                  docker-selinux \

                  docker-engine
```
需要注意的是，执行该命令只会卸载Docker本身，而不会删除Docker存储的文件，例如镜像、容器、卷以及网络文件等。这些文件保存在 /var/lib/docker 目录中，需要手动删除。


### 配置静态ip 忽略
###  安装仓库
- 执行以下命令，安装Docker所需的包。其中， yum-utils 提供了 yum-config-manager 工具； device-mapper-persistent-data 及 lvm2 则是 devicemapper存储驱动所需的包
```
[root@docker-node ~]# yum install -y yum-utils device-mapper-persistent-data lvm2
Loaded plugins: fastestmirror, langpacks
base                                                                                                               | 3.6 kB  00:00:00     
extras                                                                                                             | 3.4 kB  00:00:00     
updates                                                                                                            | 3.4 kB  00:00:00     
(1/4): base/7/x86_64/group_gz                                                                                      | 166 kB  00:00:01     
(2/4): extras/7/x86_64/primary_db                                                                                  | 156 kB  00:00:03     
(3/4): base/7/x86_64/primary_db                                                                                    | 6.0 MB  00:00:10     
(4/4): updates/7/x86_64/primary_db                                                                                 | 1.3 MB  00:00:10     
Determining fastest mirrors
 * base: ftp.sjtu.edu.cn
 * extras: mirrors.huaweicloud.com
 * updates: mirrors.huaweicloud.com
Resolving Dependencies
--> Running transaction check
---> Package device-mapper-persistent-data.x86_64 0:0.6.3-1.el7 will be updated
---> Package device-mapper-persistent-data.x86_64 0:0.7.3-3.el7 will be an update
---> Package lvm2.x86_64 7:2.02.166-1.el7 will be updated
---> Package lvm2.x86_64 7:2.02.180-10.el7_6.2 will be an update
--> Processing Dependency: lvm2-libs = 7:2.02.180-10.el7_6.2 for package: 7:lvm2-2.02.180-10.el7_6.2.x86_64
--> Processing Dependency: libdevmapper.so.1.02(DM_1_02_141)(64bit) for package: 7:lvm2-2.02.180-10.el7_6.2.x86_64
--> Processing Dependency: libdevmapper.so.1.02(DM_1_02_138)(64bit) for package: 7:lvm2-2.02.180-10.el7_6.2.x86_64
---> Package yum-utils.noarch 0:1.1.31-40.el7 will be updated
---> Package yum-utils.noarch 0:1.1.31-50.el7 will be an update
--> Running transaction check
---> Package device-mapper-libs.x86_64 7:1.02.135-1.el7 will be updated
--> Processing Dependency: device-mapper-libs = 7:1.02.135-1.el7 for package: 7:device-mapper-1.02.135-1.el7.x86_64
---> Package device-mapper-libs.x86_64 7:1.02.149-10.el7_6.2 will be an update
---> Package lvm2-libs.x86_64 7:2.02.166-1.el7 will be updated
---> Package lvm2-libs.x86_64 7:2.02.180-10.el7_6.2 will be an update
--> Processing Dependency: device-mapper-event = 7:1.02.149-10.el7_6.2 for package: 7:lvm2-libs-2.02.180-10.el7_6.2.x86_64
--> Running transaction check
---> Package device-mapper.x86_64 7:1.02.135-1.el7 will be updated
---> Package device-mapper.x86_64 7:1.02.149-10.el7_6.2 will be an update
---> Package device-mapper-event.x86_64 7:1.02.135-1.el7 will be updated
---> Package device-mapper-event.x86_64 7:1.02.149-10.el7_6.2 will be an update
--> Processing Dependency: device-mapper-event-libs = 7:1.02.149-10.el7_6.2 for package: 7:device-mapper-event-1.02.149-10.el7_6.2.x86_64
--> Running transaction check
---> Package device-mapper-event-libs.x86_64 7:1.02.135-1.el7 will be updated
---> Package device-mapper-event-libs.x86_64 7:1.02.149-10.el7_6.2 will be an update
--> Finished Dependency Resolution

Dependencies Resolved

==========================================================================================================================================
 Package                                       Arch                   Version                               Repository               Size
==========================================================================================================================================
Updating:
 device-mapper-persistent-data                 x86_64                 0.7.3-3.el7                           base                    405 k
 lvm2                                          x86_64                 7:2.02.180-10.el7_6.2                 updates                 1.3 M
 yum-utils                                     noarch                 1.1.31-50.el7                         base                    121 k
Updating for dependencies:
 device-mapper                                 x86_64                 7:1.02.149-10.el7_6.2                 updates                 292 k
 device-mapper-event                           x86_64                 7:1.02.149-10.el7_6.2                 updates                 188 k
 device-mapper-event-libs                      x86_64                 7:1.02.149-10.el7_6.2                 updates                 187 k
 device-mapper-libs                            x86_64                 7:1.02.149-10.el7_6.2                 updates                 320 k
 lvm2-libs                                     x86_64                 7:2.02.180-10.el7_6.2                 updates                 1.1 M

Transaction Summary
==========================================================================================================================================
Upgrade  3 Packages (+5 Dependent packages)

Total download size: 3.9 M
Downloading packages:
No Presto metadata available for base
updates/7/x86_64/prestodelta                                                                                       | 182 kB  00:00:00     
(1/8): device-mapper-event-libs-1.02.149-10.el7_6.2.x86_64.rpm                                                     | 187 kB  00:00:00     
(2/8): device-mapper-1.02.149-10.el7_6.2.x86_64.rpm                                                                | 292 kB  00:00:00     
(3/8): device-mapper-persistent-data-0.7.3-3.el7.x86_64.rpm                                                        | 405 kB  00:00:01     
(4/8): yum-utils-1.1.31-50.el7.noarch.rpm                                                                          | 121 kB  00:00:00     
(5/8): device-mapper-libs-1.02.149-10.el7_6.2.x86_64.rpm                                                           | 320 kB  00:00:02     
(6/8): lvm2-2.02.180-10.el7_6.2.x86_64.rpm                                                                         | 1.3 MB  00:00:03     
(7/8): device-mapper-event-1.02.149-10.el7_6.2.x86_64.rpm                                                          | 188 kB  00:00:05     
(8/8): lvm2-libs-2.02.180-10.el7_6.2.x86_64.rpm                                                                    | 1.1 MB  00:00:04     
------------------------------------------------------------------------------------------------------------------------------------------
Total                                                                                                     675 kB/s | 3.9 MB  00:00:05     
Running transaction check
Running transaction test
Transaction test succeeded
Running transaction
Warning: RPMDB altered outside of yum.
** Found 3 pre-existing rpmdb problem(s), 'yum check' output follows:
ipa-client-4.4.0-12.el7.centos.x86_64 has installed conflicts freeipa-client: ipa-client-4.4.0-12.el7.centos.x86_64
ipa-client-common-4.4.0-12.el7.centos.noarch has installed conflicts freeipa-client-common: ipa-client-common-4.4.0-12.el7.centos.noarch
ipa-common-4.4.0-12.el7.centos.noarch has installed conflicts freeipa-common: ipa-common-4.4.0-12.el7.centos.noarch
  Updating   : 7:device-mapper-libs-1.02.149-10.el7_6.2.x86_64                                                                       1/16 
  Updating   : 7:device-mapper-1.02.149-10.el7_6.2.x86_64                                                                            2/16 
  Updating   : 7:device-mapper-event-libs-1.02.149-10.el7_6.2.x86_64                                                                 3/16 
  Updating   : 7:device-mapper-event-1.02.149-10.el7_6.2.x86_64                                                                      4/16 
  Updating   : 7:lvm2-libs-2.02.180-10.el7_6.2.x86_64                                                                                5/16 
  Updating   : device-mapper-persistent-data-0.7.3-3.el7.x86_64                                                                      6/16 
  Updating   : 7:lvm2-2.02.180-10.el7_6.2.x86_64                                                                                     7/16 
  Updating   : yum-utils-1.1.31-50.el7.noarch                                                                                        8/16 
  Cleanup    : yum-utils-1.1.31-40.el7.noarch                                                                                        9/16 
  Cleanup    : 7:lvm2-2.02.166-1.el7.x86_64                                                                                         10/16 
  Cleanup    : 7:lvm2-libs-2.02.166-1.el7.x86_64                                                                                    11/16 
  Cleanup    : 7:device-mapper-event-1.02.135-1.el7.x86_64                                                                          12/16 
  Cleanup    : 7:device-mapper-event-libs-1.02.135-1.el7.x86_64                                                                     13/16 
  Cleanup    : 7:device-mapper-libs-1.02.135-1.el7.x86_64                                                                           14/16 
  Cleanup    : 7:device-mapper-1.02.135-1.el7.x86_64                                                                                15/16 
  Cleanup    : device-mapper-persistent-data-0.6.3-1.el7.x86_64                                                                     16/16 
  Verifying  : device-mapper-persistent-data-0.7.3-3.el7.x86_64                                                                      1/16 
  Verifying  : 7:device-mapper-1.02.149-10.el7_6.2.x86_64                                                                            2/16 
  Verifying  : yum-utils-1.1.31-50.el7.noarch                                                                                        3/16 
  Verifying  : 7:lvm2-libs-2.02.180-10.el7_6.2.x86_64                                                                                4/16 
  Verifying  : 7:lvm2-2.02.180-10.el7_6.2.x86_64                                                                                     5/16 
  Verifying  : 7:device-mapper-event-libs-1.02.149-10.el7_6.2.x86_64                                                                 6/16 
  Verifying  : 7:device-mapper-event-1.02.149-10.el7_6.2.x86_64                                                                      7/16 
  Verifying  : 7:device-mapper-libs-1.02.149-10.el7_6.2.x86_64                                                                       8/16 
  Verifying  : 7:device-mapper-event-libs-1.02.135-1.el7.x86_64                                                                      9/16 
  Verifying  : 7:device-mapper-1.02.135-1.el7.x86_64                                                                                10/16 
  Verifying  : 7:lvm2-2.02.166-1.el7.x86_64                                                                                         11/16 
  Verifying  : yum-utils-1.1.31-40.el7.noarch                                                                                       12/16 
  Verifying  : device-mapper-persistent-data-0.6.3-1.el7.x86_64                                                                     13/16 
  Verifying  : 7:device-mapper-libs-1.02.135-1.el7.x86_64                                                                           14/16 
  Verifying  : 7:device-mapper-event-1.02.135-1.el7.x86_64                                                                          15/16 
  Verifying  : 7:lvm2-libs-2.02.166-1.el7.x86_64                                                                                    16/16 

Updated:
  device-mapper-persistent-data.x86_64 0:0.7.3-3.el7       lvm2.x86_64 7:2.02.180-10.el7_6.2       yum-utils.noarch 0:1.1.31-50.el7      

Dependency Updated:
  device-mapper.x86_64 7:1.02.149-10.el7_6.2                             device-mapper-event.x86_64 7:1.02.149-10.el7_6.2                 
  device-mapper-event-libs.x86_64 7:1.02.149-10.el7_6.2                  device-mapper-libs.x86_64 7:1.02.149-10.el7_6.2                  
  lvm2-libs.x86_64 7:2.02.180-10.el7_6.2                                

Complete!
[root@docker-node ~]# 
```
-  执行如下命令，安装 stable 仓库。必须安装 stable 仓库，即使你想安装 edge 或 test 仓库中的Docker构建版本。

```
[root@docker-node ~]# yum-config-manager --add-repo https//download.docker.com/linux/centos/docker-ce.repo
Loaded plugins: fastestmirror, langpacks
adding repo from: https//download.docker.com/linux/centos/docker-ce.repo
grabbing file https//download.docker.com/linux/centos/docker-ce.repo to /etc/yum.repos.d/docker-ce.repo
Could not fetch/save url https//download.docker.com/linux/centos/docker-ce.repo to file /etc/yum.repos.d/docker-ce.repo: [Errno 14] curl#37 - "Couldn't open file /root/https/download.docker.com/linux/centos/docker-ce.repo"
[root@docker-node ~]# 
```
- [可选] 执行如下命令，启用 edge 及 test 仓库。edge/test仓库其实也包含在了 docker.repo 文件中，但默认是禁用的，可使用以下命令来启用

```
[root@docker-node ~]# yum-config-manager --enable docker-ce-edge
Loaded plugins: fastestmirror, langpacks
[root@docker-node ~]# yum-config-manager --enable docker-ce-test
Loaded plugins: fastestmirror, langpacks
[root@docker-node ~]# 
```
如需再次禁用，可加上 --disable 标签。例如，执行如下命令即可禁用edge仓库。

**TIPS**：从Docker 17.06起，stable版本也会发布到edge以及test仓库中。
### 安装Docker CE

- 执行以下命令，更新 yum的包索引

```
[root@docker-node ~]# yum makecache fast
Loaded plugins: fastestmirror, langpacks
base                                                                                                               | 3.6 kB  00:00:00     
extras                                                                                                             | 3.4 kB  00:00:00     
updates                                                                                                            | 3.4 kB  00:00:00     
Loading mirror speeds from cached hostfile
 * base: ftp.sjtu.edu.cn
 * extras: mirrors.huaweicloud.com
 * updates: mirrors.huaweicloud.com
Metadata Cache Created
[root@docker-node ~]# 
```

- 执行如下命令即可安装最新版本的Docker CE

```
[root@docker-node ~]# yum install docker
```
- 在生产环境中，可能需要指定想要安装的版本，此时可使用如下命令列出当前可用的Docker版本。

```
[root@docker-node ~]# yum list docker-ce.x86_64   --showduplicates  |sort -r
```
这样，列出版本后，可使用如下命令，安装想要安装的Docker CE版本。

```
sudo yum install docker-ce-<VERSION>
```
- 启动docker

```
[root@docker-node ~]# systemctl  start docker
Job for docker.service failed because the control process exited with error code. See "systemctl status docker.service" and "journalctl -xe" for details.
```

启动失败 查看原因

```
[root@docker-node ~]# systemctl status docker.service
 ● docker.service - Docker Application Container Engine
   Loaded: loaded (/usr/lib/systemd/system/docker.service; disabled; vendor preset: disabled)
   Active: failed (Result: exit-code) since Thu 2018-12-27 11:00:03 CST; 2min 2s ago
     Docs: http://docs.docker.com
  Process: 5335 ExecStart=/usr/bin/dockerd-current --add-runtime docker-runc=/usr/libexec/docker/docker-runc-current --default-runtime=docker-runc --exec-opt native.cgroupdriver=systemd --userland-proxy-path=/usr/libexec/docker/docker-proxy-current --init-path=/usr/libexec/docker/docker-init-current --seccomp-profile=/etc/docker/seccomp.json $OPTIONS $DOCKER_STORAGE_OPTIONS $DOCKER_NETWORK_OPTIONS $ADD_REGISTRY $BLOCK_REGISTRY $INSECURE_REGISTRY $REGISTRIES (code=exited, status=1/FAILURE)
 Main PID: 5335 (code=exited, status=1/FAILURE)

Dec 27 11:00:02 docker-node systemd[1]: Starting Docker Application Container Engine...
Dec 27 11:00:02 docker-node dockerd-current[5335]: time="2018-12-27T11:00:02.320998598+08:00" level=warning msg="could not change...found"
Dec 27 11:00:02 docker-node dockerd-current[5335]: time="2018-12-27T11:00:02.330666501+08:00" level=info msg="libcontainerd: new ... 5339"
Dec 27 11:00:03 docker-node dockerd-current[5335]: Error starting daemon: SELinux is not supported with the overlay2 graph driver...false)
Dec 27 11:00:03 docker-node systemd[1]: docker.service: main process exited, code=exited, status=1/FAILURE
Dec 27 11:00:03 docker-node systemd[1]: Failed to start Docker Application Container Engine.
Dec 27 11:00:03 docker-node systemd[1]: Unit docker.service entered failed state.
Dec 27 11:00:03 docker-node systemd[1]: docker.service failed.
Hint: Some lines were ellipsized, use -l to show in full.
[root@docker-node ~]# 
```


将 SELINUX=disabled 重启机器
```
[root@docker-node ~]# vim /etc/sysconfig/selinux 
[root@docker-node ~]# 
[root@docker-node ~]# cat /etc/sysconfig/selinux 

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


[root@docker-node ~]# systemctl start docker.service
```

- 验证安装是否正确。这样，Docker将会下载测试镜像，并使用该镜像启动一个容器。如能够看到类似如下的输出，则说明安装成功。

```
[root@docker-node ~]# docker run hello-world
Unable to find image 'hello-world:latest' locally
Trying to pull repository docker.io/library/hello-world ... 
latest: Pulling from docker.io/library/hello-world
d1725b59e92d: Pull complete 
Digest: sha256:b3a26e22bf55e4a5232b391281fc1673f18462b75cdc76aa103e6d3a2bce5e77
Status: Downloaded newer image for docker.io/hello-world:latest

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

[root@docker-node ~]# 
```


- 升级Docker CE 

```
[root@docker-node ~]# yum makecache fast
Loaded plugins: fastestmirror, langpacks
base                                                                                                                                                    | 3.6 kB  00:00:00     
extras                                                                                                                                                  | 3.4 kB  00:00:00     
updates                                                                                                                                                 | 3.4 kB  00:00:00     
Loading mirror speeds from cached hostfile
 * base: ftp.sjtu.edu.cn
 * extras: mirrors.huaweicloud.com
 * updates: mirrors.huaweicloud.com
Metadata Cache Created
[root@docker-node ~]# 
```
- docker 安装Jenkins


```
[root@docker-node ~]# docker search jenkins
INDEX       NAME                                             DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
docker.io   docker.io/jenkins                                Official Jenkins Docker image                   4043      [OK]       
docker.io   docker.io/jenkins/jenkins                        The leading open source automation server       1199                 
docker.io   docker.io/jenkinsci/jenkins                      Jenkins Continuous Integration and Deliver...   349                  
docker.io   docker.io/jenkinsci/blueocean                    https://jenkins.io/projects/blueocean           307                  
docker.io   docker.io/jenkinsci/jnlp-slave                   A Jenkins slave using JNLP to establish co...   97                   [OK]
docker.io   docker.io/jenkins/jnlp-slave                     a Jenkins agent (FKA "slave") using JNLP t...   63                   [OK]
docker.io   docker.io/jenkinsci/slave                        Base Jenkins slave docker image                 47                   [OK]
docker.io   docker.io/jenkinsci/ssh-slave                    A Jenkins SSH Slave docker image                34                   [OK]
docker.io   docker.io/cloudbees/jenkins-enterprise           CloudBees Jenkins Enterprise (Rolling rele...   32                   [OK]
docker.io   docker.io/openshift/jenkins-2-centos7            A Centos7 based Jenkins v2.x image for use...   20                   
docker.io   docker.io/h1kkan/jenkins-docker                  烙 Extended Jenkins docker image, bundled w...   17                   
docker.io   docker.io/bitnami/jenkins                        Bitnami Docker Image for Jenkins                16                   [OK]
docker.io   docker.io/blacklabelops/jenkins                  Docker Jenkins Swarm-Ready with HTTPS and ...   13                   [OK]
docker.io   docker.io/cloudbees/jenkins-operations-center    CloudBees Jenkins Operation Center (Rollin...   13                   [OK]
docker.io   docker.io/mesosphere/jenkins                     Jenkins on DC/OS Docker image.                  12                   
docker.io   docker.io/fabric8/jenkins-docker                 Fabric8 Jenkins Docker Image                    10                   [OK]
docker.io   docker.io/killercentury/jenkins-slave-dind       Generic Jenkins Slave with Docker Engine a...   8                    [OK]
docker.io   docker.io/vfarcic/jenkins-swarm-agent            Jenkins agent based on the Swarm plugin         8                    [OK]
docker.io   docker.io/publicisworldwide/jenkins-slave        Jenkins Slave based on Oracle Linux             5                    [OK]
docker.io   docker.io/openshift/jenkins-1-centos7            DEPRECATED: A Centos7 based Jenkins v1.x i...   4                    
docker.io   docker.io/openshift/jenkins-slave-base-centos7   A Jenkins slave base image.                     4                    
docker.io   docker.io/amazeeio/jenkins-slave                 A jenkins slave that connects to a master ...   0                    [OK]
docker.io   docker.io/ansibleplaybookbundle/jenkins-apb      An APB which deploys Jenkins CI                 0                    [OK]
docker.io   docker.io/jameseckersall/jenkins                 docker-jenkins (based on openshift jenkins...   0                    [OK]
docker.io   docker.io/mashape/jenkins                        Just a jenkins image with the AWS cli adde...   0                    [OK]

```

- 下载Jenkins 镜像
```
[root@docker-node ~]# docker pull docker.io/jenkins
```

## 参考文章：
- 1 [**springboot输出log到graylog2**](https://segmentfault.com/a/1190000006857382)

- 2 [**docker ELK 中安装插件**](https://blog.csdn.net/a243293719/article/details/82021823)

- 3 [**Docker安装graylog和详解**](https://blog.csdn.net/u012954706/article/details/79592060)

- 4 [**idea 中配置 docker**](https://blog.csdn.net/jackcheng1117/article/details/83080303)

- 5 [**安装Docker-Compose**](https://www.cnblogs.com/YatHo/p/7815400.html)

- 6 [**centos7 下安装docker-compose 下报错**](https://blog.csdn.net/hillwooda/article/details/80027976)

- 7 [**解决执行脚本文本格式不正确**](https://blog.csdn.net/u012453843/article/details/69803244/)

- 8 [**解决docker下找不到 elasticsearch:latest**](https://blog.csdn.net/weixin_40161254/article/details/85795941)

- 9 [**使用graylog来收集你的Docker日志**](https://www.jianshu.com/p/25e310596559)

- 10 [**使用 docker-compose.yml 定义多容器应用程序**](https://docs.microsoft.com/zh-cn/dotnet/standard/microservices-architecture/multi-container-microservice-net-applications/multi-container-applications-docker-compose)

## **要获取所有容器名称及其IP地址只需一个命令**
```bash
docker inspect -f '{{.Name}} - {{.NetworkSettings.IPAddress }}' $(docker ps -aq)
#docker-compose命令
docker inspect -f '{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)
```


## 显示所有容器IP地址：
```bash
docker inspect --format='{{.Name}} - {{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $(docker ps -aq)
```
## 注意事项
- 1 由于本机是windows 所以*.sh 和*.yml文件 的格式为doc 在 Linux 中编辑文件的时候 使用如下命令更改

```bash
:set ff=unix
```
- 2 添加nacos 配置 及pom 文件中修改 及启动main 中添加

```properties
spring.cloud.nacos.discovery.server-addr=192.168.61.137:8848
```

```xml

   <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
```
```java

@EnableDiscoveryClient
@SpringBootApplication
public class SpringNacosClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringNacosClientApplication.class, args);
    }
}
```
## 编写docker 运行命令
```bash
#!/usr/bin/env bash

set -e

# TODO: set env for docker-machine in Windows and OSX


# export docker-machine IP
IP=127.0.0.1
unamestr=`uname`
if [[ "$unamestr" != 'Linux' ]]; then
  # Set docker-machine IP
  IP="$(docker-machine ip)"
fi
export EXTERNAL_IP=$IP

# Get this script directory (to find yml from any directory)
export DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Stop
docker-compose -f $DIR/docker-compose.yml stop


# Start container cluster
# First start persistence and auth container and wait for it
docker-compose -f $DIR/docker-compose.yml up -d elasticsearch  kibana graylog2 nacos
echo "Waiting for persistence init..."
sleep 30

# Start other containers
docker-compose -f $DIR/docker-compose.yml up

```
## docker-compose.yml 配置
```yaml
version: "2"
services:
  mongo:
    image: mongo:latest
    expose:
    - "27017"
  nacos:
    image: paderlol/nacos:latest
    container_name: nacos-standalone
    environment:
      - PREFER_HOST_MODE=hostname
      - MODE=standalone
      - TZ=Asia/Shanghai
    ports:
      - "8848:8848"
    volumes:
      - ./logs:/home/nacos/logs
  elasticsearch:
    image: elasticsearch:5.1.1
    ports:
      - "9200:9200"
      - "9300:9300"
    command: elasticsearch
    environment:
      ES_JAVA_OPTS: "-Xms512m -Xmx512m"
      TZ: "Asia/Shanghai"

  kibana:
    image: kibana:5.1.1
    ports:
      - "5601:5601"
    environment:
      - ELASTICSEARCH_URL=http://elasticsearch:9200
      - TZ=Asia/Shanghai
    links:
      - elasticsearch
      - graylog2
    depends_on:
      - graylog2
  # Graylog: https://hub.docker.com/r/graylog/graylog/
  graylog2:
    image: graylog/graylog:2.4.0-1
    environment:
      #CHANGE ME!
      - GRAYLOG_PASSWORD_SECRET=somepasswordpepper
      # Password: admin
      - GRAYLOG_ROOT_PASSWORD_SHA2=8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
      - GRAYLOG_WEB_ENDPOINT_URI=http://192.168.61.137:9000/api
      - TZ=Asia/Shanghai
    links:
      - elasticsearch
      - mongo
    depends_on:
      - elasticsearch
      - mongo
    ports:
      - 9000:9000
      - 514:514
      - 514:514/udp
      - 12201:12201
      - 12201:12201/udp
    command: -e 'input { gelf { host => "0.0.0.0" port => 12201 } }
          output { elasticsearch { hosts => ["elasticsearch"] } }'
  spring-nacos-server:
    image: spring-cloud-docker/spring-nacos-server
    depends_on:
      - nacos
      - graylog2
    links:
      - nacos
      - graylog2
    ports:
      - "8001:8001"
    environment:
      - TZ=Asia/Shanghai
  spring-nacos-client:
    image: spring-cloud-docker/spring-nacos-client
    depends_on:
      - nacos
      - graylog2
    environment:
      - TZ=Asia/Shanghai
    links:
      - nacos
      - graylog2
    ports:
      - "8002:8002"
    extra_hosts:
      - "dockernet:${EXTERNAL_IP}"

```
## 编写项目docker 构建命令
```bash
#!/usr/bin/env bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# 执行docker build -t 文件时为主动找到 我们编写的DockerFile 文件并执行
docker build -t "spring-cloud-docker/spring-nacos-server" $DIR/../spring-nacos-server
docker build -t "spring-cloud-docker/spring-nacos-client" $DIR/../spring-nacos-client

```
## 编写DockerFile
```dockerfile


#指定java版本
FROM java:8-jre

# 宿主机 执行文件目录
ENV SPRING_CLOUD_DOCKER_FILE target/spring-nacos-client-1.0-SNAPSHOT.jar

# docker 虚拟机执行文件目录
ENV SPRING_CLOUD_DOCKER_HOME /opt/spring-cloud-docker
#对外暴露端口
EXPOSE 8002

#将宿主机文件copy 到 docker虚拟机中
COPY $SPRING_CLOUD_DOCKER_FILE $SPRING_CLOUD_DOCKER_HOME/
#COPY src/config/docker.json $SPRING_CLOUD_DOCKER_HOME/

# 创建docker虚拟机文件位置
WORKDIR $SPRING_CLOUD_DOCKER_HOME

ENTRYPOINT ["sh", "-c"]

CMD ["java    -jar spring-nacos-client-1.0-SNAPSHOT.jar"]
```

