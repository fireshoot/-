### Netty的概念和体系结构

#### 异步和事件驱动

* 关注点分离--业务和网络逻辑解耦
* 模块化和可复用性
* 可测试性



```java
// java io
// 创建一个新的ServerSocket, 用以监听指定端口上的连接请求。
ServerSocket serverSocket = new ServerSocket(8080);
// 对accept()方法调用将被阻塞，直到一个连接的建立
Socket socket = serverSocket.accept();
BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
String request, response;
while ((request = in.readLine()) != null) {
     if ("Done".equals(request)){
        break;
     }
 response = handler(request);
}

```

Netty的核心组件

Channel

回调

Future

事件 和 ChannelHandler

这些模块代表不同类型的构造：资源、逻辑、以及通知。你的应用程序将使用他们来访问网络以及流经网络的数据。

#### Channel

​	Channel 是Java Nio的一个基本构造。代表一个到实体(eg：一个硬件设备、文件、网络套接字、一个能够执行一个或者多个不同的I/O操作的程序组件)的开放连接，如读操作和写操作。

​	目前，可以把Channel看做传入(入站)或者传出(出站)数据的载体。因此，他可以被打开和关闭，连接或者断开连接。

#### 回调

​	一个回调其实就是一个方法，一个指向已经被提供给另外一个的方法的引用。使得后者可以在适当的时候调用前者。回调广泛的编程场景中都有应用，而且也是在操作完成后通知相关方最常见方式之一。

​	Netty在内部使用回调来处理事件，当一个回调被触发时，相关的事件可以被一个interface

ChannelHandler实现处理。当一个新的连接已经被建立时候，ChannelHandler的channelActive()回调方法将被调用，并打印出一条信息。



#### Future































