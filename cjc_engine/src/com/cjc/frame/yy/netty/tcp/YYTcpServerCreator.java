package com.cjc.frame.yy.netty.tcp;

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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Description: tcp服务创建器
 * @author cjc
 * @date Apr 23, 2019
*/
@Component
@Scope("prototype")
public class YYTcpServerCreator implements Runnable, ApplicationContextAware {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ApplicationContext mApplicationContext;

	private int mProt;

	/** idle处理器，用于心跳 */
	private Class<? extends ChannelHandler> mHeartbeatHandlerClass;
	/** idle时间 */
	private int mReadIdleTime;
	private int mWriteIdleTime;
	private int mAllIdleTime;

	/** 链接处理器 */
	private Class<? extends YYTcpConnectHandler> mChannelConnectHandlerClass;

	/** 解码器处理器 */
	private Class<? extends YYTcpDecoder> mDecoderHandlerClass;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.mApplicationContext = applicationContext;
	}

	public void initAndStartByThread(int port, Class<? extends YYTcpConnectHandler> channelConnectHandlerClass,
			Class<? extends YYTcpDecoder> decoderHandlerClass, Class<? extends ChannelHandler> heartbeatHandlerClass,
			int readIdleTime, int writeIdleTime, int allIdleTime) {
		this.mProt = port;

		this.mChannelConnectHandlerClass = channelConnectHandlerClass;
		this.mDecoderHandlerClass = decoderHandlerClass;

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
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new MyInitializer());
			sLog.info("tcp service startup on port:{}", mProt);
			ChannelFuture cf = serverBootstrap.bind(mProt).sync();

			cf.channel().closeFuture().sync();
			sLog.fatal("tcp service crush");
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

	private class MyInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel sc) throws Exception {
			ChannelPipeline cp = sc.pipeline();

			//netty心跳需两步：
			//1.使用了 IdleStateHandler，分别设置了读、写超时的时间
			cp.addLast(new IdleStateHandler(mReadIdleTime, mWriteIdleTime, mAllIdleTime, TimeUnit.SECONDS));
			//2.定义了一个 WsHeartBeatHandler，用来处理超时时，发送心跳
			cp.addLast(mApplicationContext.getBean(mHeartbeatHandlerClass));

			//链接处理器
			cp.addLast(mApplicationContext.getBean(mChannelConnectHandlerClass));

			//解码器
			cp.addLast(mApplicationContext.getBean(mDecoderHandlerClass));
		}
	}
}
