### 安装

安装erlang

```bash
# 安装 erlang 
sudo apt-get install erlang-nox
sudo apt-get update
sudo apt-get upgrade
# 安装 rabbitmq
sudo apt-get install rabbitmq-server

# 启动、停止、重启、关闭
sudo service rabbitmq-server start
sudo service rabbitmq-server stop
sudo service rabbitmq-server restart
sudo service rabbitmqctl status

#启动rabbitmq自带的web管理界面
rabbitmqctl start_app
rabbitmq-plugins enable rabbitmq_management
# http://ip:15672 
# 新建用户
rabbitmqctl add_user yangxin666 admin
# 设置权限
rabbitmqctl set_user_tags yangxin666 administrator
# 设置对所有资源的配置，写，读权限
rabbitmqctl set_permissions -p / admin '.*' '.*' '.*'

基本操作：
sudo rabbitmqctl # 查看所有命令和帮助文档
sudo rabbitmqctl stop # 停止服务
sudo rabbitmqctl status # 查看服务状态
sudo rabbitmqctl list_users # 查看当前所有用户
sudo rabbitmqctl list_user_permissions guest # 查看默认guest用户的权限
sudo rabbitmqctl delete_user guest # 删掉默认用户(由于RabbitMQ默认的账号用户名和密码都是guest。为了安全起见, 可以删掉默认用户）
sudo rabbitmqctl add_user username password # 添加新用户
sudo rabbitmqctl set_user_tags username administrator# 设置用户tag
sudo rabbitmqctl set_permissions -p / username ".*" ".*" ".*" # 赋予用户默认vhost的全部操作权限
sudo rabbitmqctl list_user_permissions username # 查看用户的权限
rabbitmqctl set_user_tags User Tag #User为用户名， Tag为角色名(对应于上面的administrator，monitoring，policymaker，management，或其他自定义名称)。

```



> 1) 超级管理员(administrator)
>
> 可登陆管理控制台(启用management plugin的情况下)，可查看所有的信息，并且可以对用户，策略(policy)进行操作。
>
> 2) 监控者(monitoring)
>
> 可登陆管理控制台(启用management plugin的情况下)，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)
>
> 3) 策略制定者(policymaker)
>
> 可登陆管理控制台(启用management plugin的情况下), 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)。
>
> 与administrator的对比，administrator能看到这些内容
>
> 4) 普通管理者(management)
>
> 仅可登陆管理控制台(启用management plugin的情况下)，无法看到节点信息，也无法对策略进行管理。
>
> 5) 其他
>
> 无法登陆管理控制台，通常就是普通的生产者和消费者。



simple简单模式、work工作模式(资源竞争)、publish/subscribe发布订阅(共享资源)、routing路由模式、topic 主题模式(路由模式的一种)、RPC

![1574156414816](markdownImage\1574156414816.png)

> 三种交换器
>
> fanout模式(广播模式)：连接到这种交换机，所有队列拿到的数据都是一模一样的，常用于那种需要通知很多服务器或者其他多个系统的消息类型。
>
> direct模式： 通过Routing key 将消息发送给指定的队列。 
>
> topic模式： 和direct模式差不多，但是更加灵活，支持模式匹配，通配符等。

AMQP

