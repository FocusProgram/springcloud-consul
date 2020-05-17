<font size=4.5>

**Consul**

---

- **文章目录**

* [1\.什么是Consul?](#1%E4%BB%80%E4%B9%88%E6%98%AFconsul)
* [2\.安装Consul](#2%E5%AE%89%E8%A3%85consul)
  * [2\.1 二进制文件安装 <a href="https://www\.consul\.io/downloads\.html" rel="nofollow">下载地址</a>](#21-%E4%BA%8C%E8%BF%9B%E5%88%B6%E6%96%87%E4%BB%B6%E5%AE%89%E8%A3%85-%E4%B8%8B%E8%BD%BD%E5%9C%B0%E5%9D%80)
  * [2\.2 源码编译](#22-%E6%BA%90%E7%A0%81%E7%BC%96%E8%AF%91)
  * [2\.3 Docker安装](#23-docker%E5%AE%89%E8%A3%85)
    * [2\.3\.1 拉取镜像](#231-%E6%8B%89%E5%8F%96%E9%95%9C%E5%83%8F)
    * [2\.3\.2 consul 参数详解](#232-consul-%E5%8F%82%E6%95%B0%E8%AF%A6%E8%A7%A3)
    * [2\.3\.3 consul 端口详解](#233-consul-%E7%AB%AF%E5%8F%A3%E8%AF%A6%E8%A7%A3)
    * [2\.3\.4 consul 集群启动Consul](#234-consul-%E9%9B%86%E7%BE%A4%E5%90%AF%E5%8A%A8consul)
* [3\. 客户端集成Consul](#3-%E5%AE%A2%E6%88%B7%E7%AB%AF%E9%9B%86%E6%88%90consul)
  * [3\.1 引入maven依赖](#31-%E5%BC%95%E5%85%A5maven%E4%BE%9D%E8%B5%96)
  * [3\.2 项目结构](#32-%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84)
  * [3\.3 环境依赖](#33-%E7%8E%AF%E5%A2%83%E4%BE%9D%E8%B5%96)
  * [3\.4 配置文件](#34-%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
    * [3\.4\.1 application\.yml](#341-applicationyml)
    * [3\.4\.2 bootstrap\.yml](#342-bootstrapyml)
    * [3\.4\.3 动态参数接收类](#343-%E5%8A%A8%E6%80%81%E5%8F%82%E6%95%B0%E6%8E%A5%E6%94%B6%E7%B1%BB)
    * [3\.4\.4 controller接口](#344-controller%E6%8E%A5%E5%8F%A3)
  * [3\.5 consul中创建节点数据](#35-consul%E4%B8%AD%E5%88%9B%E5%BB%BA%E8%8A%82%E7%82%B9%E6%95%B0%E6%8D%AE)
  * [3\.6 验证从consul中获取的配置文件](#36-%E9%AA%8C%E8%AF%81%E4%BB%8Econsul%E4%B8%AD%E8%8E%B7%E5%8F%96%E7%9A%84%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
* [4\. 注意事项](#4-%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)

# 1.什么是Consul?

> [Consul](https://github.com/hashicorp/consul) 是一个服务网格解决方案，提供了一个全功能的控制平面，具有服务发现、配置和分割功能。 这些特性中的每一个都可以根据需要单独使用，或者可以一起使用来构建一个完整的服务网格。 consul需要一个数据平面，并且支持代理和本地集成模型。 执政带有一个简单的内置代理，这样一切都可以开箱即用，而且还支持第三方代理集成。

# 2.安装Consul

* **安装方式**
  * Precompiled Binaries 预编译二进制文件
  * Compiling from Source 从源头编译
  * Docker

## 2.1 二进制文件安装 [下载地址](https://www.consul.io/downloads.html)

## 2.2 源码编译

```linux
$ mkdir -p $GOPATH/src/github.com/hashicorp && cd !$

$ git clone https://github.com/hashicorp/consul.git

$ cd consul

$ make tools

$ make dev

$ consul -v 验证安装是否成功
```

## 2.3 Docker安装

### 2.3.1 拉取镜像

```
$ docker pull consul:latest
```

### 2.3.2 consul 参数详解

> - –net=host docker参数, 使得docker容器越过了net namespace的隔离，免去手动指定端口映射的步骤
> - -server consul支持以server或client的模式运行, server是服务发现模块的核心, client主要用于转发请求
> - -advertise 将本机私有IP传递到consul
> - -retry-join 指定要加入的consul节点地址，失败后会重试, 可多次指定不同的地址
> - -client 指定consul绑定在哪个client地址上，这个地址可提供HTTP、DNS、RPC等服务，默认是>127.0.0.1
> - -bind 绑定服务器的ip地址；该地址用来在集群内部的通讯，集群内的所有节点到地址必须是可达的，>默认是0.0.0.0
> 
> allow_stale 设置为true则表明可从consul集群的任一server节点获取dns信息, false则表明每次请求都会>经过consul的server leader
> - -bootstrap-expect 数据中心中预期的服务器数。指定后，Consul将等待指定数量的服务器可用，然后>启动群集。允许自动选举leader，但不能与传统-bootstrap标志一起使用, 需要在server模式下运行。
> - -data-dir 数据存放的位置，用于持久化保存集群状态
> - -node 群集中此节点的名称，这在群集中必须是唯一的，默认情况下是节点的主机名。
> - -config-dir 指定配置文件，当这个目录下有 .json 结尾的文件就会被加载，详细可参考https://www.consul.io/docs/agent/options.html#configuration_files
> - -enable-script-checks 检查服务是否处于活动状态，类似开启心跳
> - -datacenter 数据中心名称
> - -ui 开启ui界面
> - -join 指定ip, 加入到已有的集群中

### 2.3.3 consul 端口详解

> - 8500 : http 端口，用于 http 接口和 web ui访问；
> - 8300 : server rpc 端口，同一数据中心 consul server 之间通过该端口通信；
> - 8301 : serf lan 端口，同一数据中心 consul client 通过该端口通信; 用于处理当前datacenter中LAN的gossip通信；
> - 8302 : serf wan 端口，不同数据中心 consul server 通过该端口通信; agent Server使用，处理与其他datacenter的gossip通信；
> - 8600 : dns 端口，用于已注册的服务发现；

### 2.3.4 consul 集群启动Consul

**创建挂载目录**

```
$ mkdir -p /data/consul-one/{conf,data}

$ mkdir -p /data/consul-two/{conf,data}

$ mkdir -p /data/consul-three/{conf,data}
```

**创建consul的网段**

```
$ docker network create --driver=bridge --subnet=172.20.0.0/16 consul_network
```

**启动第一个Consul节点（consul-one）**

```
$ docker run --name consul-one \
-d -p 8500:8500 -p 8300:8300 \
-p 8301:8301 -p 8302:8302 -p 8600:8600 \
--restart=always \
-v /data/consul-one/conf/:/consul/conf/ \
-v /data/consul-one/data/:/consul/data/ \
--net consul_network --ip 172.20.0.2 consul \
agent -server -bootstrap-expect 2 -ui -bind=0.0.0.0 -client=0.0.0.0
```

**查看consul-one的ip地址**

```
$ docker inspect --format='{{.NetworkSettings.IPAddress}}' consul-one

172.17.0.2
```

**启动第二个Consul节点（consul-two）加入到consul-one**

```
$ docker run --name consul-two \
-d -p 8501:8500 \
--restart=always \
-v /data/consul-two/conf/:/consul/conf/ \
-v /data/consul-two/data/:/consul/data/ \
--net consul_network --ip 172.20.0.3 consul \
agent -server -ui -bind=0.0.0.0 -client=0.0.0.0 -join 172.20.0.2
```

**启动第二个Consul节点（consul-three）加入到consul-one**

```
$ docker run --name consul-three \
-d -p 8502:8500 \
--restart=always \
-v /data/consul-three/conf/:/consul/conf/ \
-v /data/consul-three/data/:/consul/data/ \
--net consul_network --ip 172.20.0.4 consul \
agent -server -ui -bind=0.0.0.0 -client=0.0.0.0 -join 172.20.0.2
```

**查看consul集群信息**

```
docker exec -it consul-one consul members
```

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307154109.png)

访问 [http://192.168.80.128:8500](http://192.168.80.128:8500)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307154346.png)

# 3. 客户端集成Consul

## 3.1 引入maven依赖

```java
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 3.2 项目结构

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307231701.png)

## 3.3 环境依赖

| 依赖          | 版本              | 备注                                                         |
|-------------|----------------|------------------------------------------------------------|
| JDK         | 1\.8           |                                                            |
| Consul      | 2.0.0.RELEASE        |           |
| SpringCloud | Finchley.RELEASE |               |
| SpringBoot | 2.0.4.RELEASE |                   |

## 3.4 配置文件

### 3.4.1 application.yml

```
# 端口
server:
  port: 8090

# 演示数据key-value
company:
  pay:
    money: 1000
```

### 3.4.2 bootstrap.yml

```java
# 服务名
spring:
  application:
    name: waiter-service

  profiles:
    active: prd

# 是否重载本地配置
  cloud:
    config:
      override-system-properties: false
    consul:
      # 指定consul的ip地址和端口
      host: 192.168.80.130
      port: 8500
      config:
        # 指定consul配置文件目录后缀结束为consul
        data-key: consul
        # consul配置中心功能，默认true
        enabled: true
        # consul配置中心值的格式
        format: yaml
        # 指定consul配置文件目录前缀为config
        prefix: config
      discovery:
        # 指定服务版本信息
        tags: version=1.0,auth=Mr.Kong
        # 是否需要注册到consul，默认为true
        register: true
        # 注册的实例ID (唯一标志)
        instance-id: ${spring.application.name}:${spring.cloud.client.ip-address}
        # 服务名称
        service-name: ${spring.application.name}
        # 服务请求端口
        port: ${server.port}
        # 指定开启ip地址注册
        prefer-ip-address: true
        # 当前服务请求ip
        ip-address: ${spring.cloud.client.ip-address}
        # 指定consul心跳检测地址
        health-check-url: http://${spring.cloud.client.ip-address}:${server.port}/actuator/health
        # 指定consul心跳检测间隔
        health-check-interval: 15s

# consul配置中心的目录为 ${spring.cloud.consul.config.prefix}/${spring.application.name},${spring.profiles.active}/${spring.cloud.consul.config.data-key}
```

> 注：关于consul的配置文件一定要放置在bootstrap.yml中才可以生效

### 3.4.3 动态参数接收类

```java
@ConfigurationProperties("company.pay")
@RefreshScope
@Data
@Component
public class PayMoneyProperties {

    Integer money;

}
```
>@ConfigurationProperties 表示这个类关联动态配置，“company.pay”表示key的前缀部分。
>
>@RefreshScope 表示动态刷新config server 值
>
>@Component 表示将该类加载到IOC容器中
在实战中尝试用@Value的方式获取动态，只能实现服务重启后获取动态的config server 的值,最终找到解决方案在相应的取值类上加@RefreshScope注解，完美解决。

### 3.4.4 controller接口

```
@RestController
@RequestMapping("consul")
@RefreshScope
public class ConsulConfigController {

    //第一种注入值的方法
    @Value("${company.pay.money}")
    private String money;

    //第二种注入值的方法
    @Autowired
    private PayMoneyProperties payMoneyProperties;

    @RequestMapping("/pay/money")
    public Object getConfig() {
        String result = "第一种注入值获取的值为：" + money + ",第二种注入值获取的值为：" + payMoneyProperties.getMoney();
        return result;
    }
}
```

> 提供了两种注入值的方法，均可，第一种采用@Value进行注入，第二种采用ConfigurationProperties进行注入

## 3.5 consul中创建节点数据

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307233617.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307233630.png)

> 节点的目录分别为：
>
>config/waiter-service,dev/consul
>
>config/waiter-service,prd/consul


## 3.6 验证从consul中获取的配置文件

访问 [http://localhost:8090/consul/pay/money](http://localhost:8090/consul/pay/money)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307234114.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200307234217.png)

# 4. 注意事项

> consul出现Critical Checks错误

![](http://image.focusprogram.top/20200514140255.png)

![](http://image.focusprogram.top/20200514141020.png)

```
$ curl http://192.168.247.1:8090/actuator/health

{"timestamp":"2020-05-14T06:07:04.905+0000","status":404,"error":"Not Found","message":"No message available","path":"/actuator/health"}
```

解决措施：引入maven依赖spring-boot-starter-actuator

再次访问

```
$ curl http://192.168.247.1:8090/actuator/health

{"status":"UP"}
```

注：consul部署在虚拟机，所以本地网络可以和虚拟机宿主机互通，如果采用云服务器，则服务正常进行健康检查，必须将服务部署在和consul同网段下的云服务器上才可以正常通讯，健康检查才可以通过，该服务才可以正常调用。

> 引入spring-boot-starter-actuator版本依赖错误

![](http://image.focusprogram.top/20200514135934.png)

解决措施：spring-boot版本为2.0.4.RELEASE，spring-cloud版本为Finchley.RELEASE

</font>