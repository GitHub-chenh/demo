# RocketMQ

### 特性

* 订阅(broker->consumer)与发布(provider->broker)
* 消息顺序(全局顺序，分区顺序)
* 消息过滤(根据Tag)
* 消息可靠性(对于单点故障可以通过同步双写技术完全避免消息丢失，考虑性能)
* 至少一次(如果没有消费一定不会ack)
* 回溯消费
* 事务消息(最终一致)
* 定时消息(延迟队列)
* 消息重试、消息重投
* 流量控制
* 死信队列

### 架构
* Producer
* Consumer
* NameServer(Topic路由注册中心)
* BrokerServer(负责消息存储、投递、查询、服务高可用)

### 集群工作流程
* 先启动NameServer，等待Broker、Producer、Consumer连接
* 随后启动Broker,跟所有的NameServer保持长连接，心跳检测(Broker信息:ip,端口,topic)，注册成功会有topic和Broker的映射关系
* 收发消息前,先创建topic(以及存储在哪些Broker上)
* Producer发送消息，启动时与NameServer建立长连接，并从NameServer中获取当前的Topic在哪些Broker上，与Broker建立长连接，发送消息
* Consumer与NameServer建立长连接，获取当前订阅Topic存在哪些Broker上，然后与Broker连接开始消费消息


