## RabbitMQ简单介绍:
RabbitMQ是一个开源的消息代理和队列服务器，用来通过普通协议在完全不同的应用之间传递数据，RabbitMQ是使用Erlang语言来编写的，并且RabbitMQ是基于AMQP协议的。

## RabbitMQ延迟队列介绍：
1. [使用RabbitMQ实现延迟任务](https://www.cnblogs.com/haoxinyue/p/6613706.html)

## RabbitMQ安装插件
### 1，rabbitmq_delayed_message_exchange

插件下载地址：https://www.rabbitmq.com/community-plugins.html

进入页面之后，先搜索rabbitmq_delayed_message_exchange，然后选择对应版本的软件下载。

```shell
wget https://dl.bintray.com/rabbitmq/community-plugins/3.6.x/rabbitmq_delayed_message_exchange/rabbitmq_delayed_message_exchange-20171215-3.6.x.zip
unzip rabbitmq_delayed_message_exchange-20171215-3.6.x.zip
mv /opt/rabbitmq_delayed_message_exchange-20171215-3.6.x.ez /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.10/plugins/
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
systemctl restart rabbitmq-server
```

即可成功安装。

```shell
$ rabbitmq-plugins list | grep rabbitmq_delayed_message_exchange
[E*] rabbitmq_delayed_message_exchange 20171215-3.6.x
```
参考：http://t.cn/Ai0O2DFP

## 学习记录
1. [RabbitMQ安装与配置](https://github.com/suxiongwei/suxiongwei.github.io/blob/master/article/other/rabbitmq_install.md)
2. [RabbitMQ：消息发送确认与消息接收确认（ACK）](https://www.jianshu.com/p/2c5eebfd0e95)
3. [消息中间件选型分析——从Kafka与RabbitMQ的对比来看全局](http://blog.didispace.com/%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E9%80%89%E5%9E%8B%E5%88%86%E6%9E%90/)
4. [RabbitMQ必备核心知识](http://www.imooc.com/article/75201)
5. [Spring Boot 实现 RabbitMQ 延迟消费和延迟重试队列](https://www.cnblogs.com/xishuai/p/spring-boot-rabbitmq-delay-queue.html)
6. [IM系统的MQ消息中间件选型：Kafka还是RabbitMQ？](https://zhuanlan.zhihu.com/p/37993013)
7. [(转载)如何保证消息队列的高可用？](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/how-to-ensure-high-availability-of-message-queues.md)
8. [(转载)为什么使用消息队列？消息队列有什么优点和缺点？Kafka、ActiveMQ、RabbitMQ、RocketMQ 都有什么优点和缺点？](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/why-mq.md)
9. [(转载)如何保证消息的可靠性传输？（如何处理消息丢失的问题）](https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/how-to-ensure-the-reliable-transmission-of-messages.md)
10. [Spring AMQP 1.6完整参考指南-第四部分](http://www.blogjava.net/qbna350816/archive/2016/08/13/431563.aspx)

### 代码实现
1. 引入相关依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```
2. 配置application.yml
```
## producer配置
spring:
  rabbitmq:
    addresses: 127.0.0.1:5672
    username: guest
    password: guest
    virtual-host: /
  datasource:
    url: jdbc:mysql://localhost:3306/rabbitmq?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull
    username: root
    password: root
    driverClassName: com.mysql.jdbc.Driver
server:
  port: 8001
  servlet:
    context-path: /
```

```
## consumer配置
## springboot整合rabbitmq的基本配置
spring:
  rabbitmq:
    addresses: 127.0.0.1:5672
    username: guest
    password: guest
    virtual-host: /
## 消费端配置
    listener:
      simple:
        concurrency: 5
        acknowledge-mode: manual
        max-concurrency: 10
        prefetch: 1
  datasource:
      url: jdbc:mysql://localhost:3306/rabbitmq?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull
      username: root
      password: root
      driverClassName: com.mysql.jdbc.Driver
server:
  port: 8002
  servlet:
    context-path: /
```
## 完整实例代码[springboot-rabbitmq](https://github.com/alexandaking/springboot-rabbitmq)

## 数据库文件
```sql
-- ----------------------------
-- Table structure for broker_message_log
-- ----------------------------
DROP TABLE IF EXISTS `broker_message_log`;
CREATE TABLE `broker_message_log` (
  `message_id` varchar(255) NOT NULL COMMENT '消息唯一ID',
  `message` varchar(4000) NOT NULL COMMENT '消息内容',
  `try_count` int(4) DEFAULT '0' COMMENT '重试次数',
  `status` varchar(10) DEFAULT '' COMMENT '消息投递状态 0投递中，1投递成功，2投递失败',
  `next_retry` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '下一次重试时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for activity_over_msg
-- ----------------------------
DROP TABLE IF EXISTS `activity_over_msg`;
CREATE TABLE `activity_over_msg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(255) DEFAULT NULL,
  `activity_over_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '活动结束时间',
  `message_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2018091102 DEFAULT CHARSET=utf8;
```

## 演示步骤:
1. 修改配置文件中的rabbitmq配置和数据库配置
2. run ConsumerApplication 来开启消费者服务
3. run ProducerApplication 来开启生产者服务
4. run SpringbootProducerApplicationTests 中的testSend方法来发送消息进行测试
