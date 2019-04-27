package com.cjc.frame.yy.netty.websocket;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Description: websocket服务创建器
 * @author cjc
 * @date Mar 5, 2019
 */
@Component
@Scope("prototype")
public class YYWebSocketServerCreator implements Runnable, ApplicationContextAware {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ApplicationContext mApplicationContext;

	private int mProt;

	/** ws处理器 */
	private Class<? extends ChannelHandler> mWebSocketHandlerClass;

	/** idle处理器，用于心跳 */
	private Class<? extends ChannelHandler> mHeartbeatHandlerClass;
	/** idle时间 */
	private int mReadIdleTime;
	private int mWriteIdleTime;
	private int mAllIdleTime;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.mApplicationContext = applicationContext;
	}

	/**
	 * 初始化并起线程开始
	 * @param port
	 * @param webSocketHandlerClass
	 * @param heartbeatHandlerClass
	 * @param readIdleTime
	 * @param writeIdleTime
	 * @param allIdleTime
	 */
	public void initAndStartByThread(int port, Class<? extends ChannelHandler> webSocketHandlerClass,
			Class<? extends ChannelHandler> heartbeatHandlerClass, int readIdleTime,
			int writeIdleTime, int allIdleTime) {
		this.mProt = port;
		this.mWebSocketHandlerClass = webSocketHandlerClass;
		this.mHeartbeatHandlerClass = heartbeatHandlerClass;
		this.mReadIdleTime = readIdleTime;
		this.mWriteIdleTime = writeIdleTime;
		this.mAllIdleTime = allIdleTime;

		new Thread(this).start();
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();

			// final int TIMEOUT_MILLIS = 10 * 60 * 1000;
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new MyInitializer());
			//.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true)
			//.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT_MILLIS)
			//.option(ChannelOption.SO_TIMEOUT, TIMEOUT_MILLIS)
			//.option(ChannelOption.ALLOW_HALF_CLOSURE, true);

			sLog.info("websocket service startup on port:{}", mProt);
			ChannelFuture cf = serverBootstrap.bind(mProt).sync();
			cf.channel().closeFuture().sync();

			sLog.fatal("websocket service crush");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			if (bossGroup != null) {
				bossGroup.shutdownGracefully();
			}
			if (workerGroup != null) {
				workerGroup.shutdownGracefully();
			}
		}
	}

	/**
	 * 连接处理管道初始化
	 * @author chaijingchao
	 * @date 2018-3
	 */
	private class MyInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel channel) throws Exception {
			ChannelPipeline cp = channel.pipeline();

			// HttpServerCodec: 针对http协议进行编解码
			cp.addLast("HttpServerCodec", new HttpServerCodec());

			// ChunkedWriteHandler分块写处理，文件过大会将内存撑爆
			cp.addLast("ChunkedWriteHandler", new ChunkedWriteHandler());

			// 作用是将一个Http的消息组装成一个完成的HttpRequest或者HttpResponse，那么具体的是什么
			// 取决于是请求还是响应, 该Handler必须放在HttpServerCodec后的后面
			cp.addLast("HttpObjectAggregator", new HttpObjectAggregator(64 * 1024));

			// 用于处理websocket, /ws为访问websocket时的uri，以/ws开头即可
			cp.addLast("WebSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws", true));

			// netty心跳需两步：
			// 1.使用了 IdleStateHandler，分别设置了读、写超时的时间
			cp.addLast(new IdleStateHandler(mReadIdleTime, mWriteIdleTime, mAllIdleTime, TimeUnit.SECONDS));
			// 2.定义了一个 WsHeartBeatHandler，用来处理超时时，发送心跳
			cp.addLast(mApplicationContext.getBean(mHeartbeatHandlerClass)); // 必须是多例，可以参见上面都是new出来的

			// 游戏逻辑处理器
			cp.addLast(mApplicationContext.getBean(mWebSocketHandlerClass)); // 必须是多例，可以参见上面都是new出来的
		}
	}
}
