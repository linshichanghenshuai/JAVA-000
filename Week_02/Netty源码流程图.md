```
NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
NioEventLoopGroup workerGroup = new NioEventLoopGroup();

try {
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(boosGroup,workerGroup)         1
            .channel(NioServerSocketChannel.class) 2
            .option(ChannelOption.SO_BACKLOG,1024) 3
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                      .addLast("encoder",new    StringEncoder(CharsetUtil.UTF_8))                 4
                      .addLast("decoder",new StringDecoder(CharsetUtil.UTF_8))                 5
                      .addLast(new NettyServerHandler()); 6
                }
            });
    System.out.println("netty server start。。");
    ChannelFuture cf = bootstrap.bind(9000).sync(); 7
```

###### 创建服务端的channel以及pipeline

从业务代码bind(9000)进入

io.netty.bootstrap.AbstractBootstrap#bind(int)

​	io.netty.bootstrap.AbstractBootstrap#bind(java.net.SocketAddress)

​		io.netty.bootstrap.AbstractBootstrap#doBind

​			io.netty.bootstrap.AbstractBootstrap#initAndRegister

走到这里之后会调用channel = channelFactory.newChannel()，根据我们在业务代码里写的channel(NioServerSocketChannel.class),通过反射创建出对应的服务端channel，到这里服务端的channel就已经创建好了,这里就没什么看的了，返回业务代码.查看channel()方法里的类的构造方法最后一直跟进去中间一部分代码省略

io.netty.channel.nio.AbstractNioChannel#AbstractNioChannel走到这里会给this.readInterestOp = readInterestOp赋值这个参数是前面传过来的channel感兴趣的事件也就是OP_ACCEPT事件 继续跟进父类代码

io.netty.channel.AbstractChannel#AbstractChannel(io.netty.channel.Channel)这里就会创建unsafe = newUnsafe() pipeline = newChannelPipeline()并赋值

<img src="D:\谷歌浏览器下载\服务端的channel和pipeline创建.jpg" alt="服务端的channel和pipeline创建"  />

总结:服务端channel是在initAndRegister中根据反射被创建的,而channel被创建的同时也会创建pipeline和unsafe.(NioServerSocketChannel类的构造方法里的newSocket（）方法里的参数SelectorProvider和返回值ServerSocketChannel都是JDK.NIO下的类说明Netty底层是调用JDK去创建的，只是做了进一步的封装)



###### 初始化服务端channel

![初始化服务端channel](D:\谷歌浏览器下载\初始化服务端channel.jpg)

总结：初始化服务端channel主要做的就是给连接器配置channelHandler.

###### 注册至selector

![注册selector](D:\谷歌浏览器下载\注册selector.jpg)

```
selectionKey = javaChannel().register(eventLoop().unwrappedSelector(), 0, this)
```

最后走到doRegister后会调用JDK底层的接口来注册并绑定到selector 而参数ops=0则表示现在暂时不关心任何事件(绑定端口后会关注accept事件)

剩下的后续陆续写上