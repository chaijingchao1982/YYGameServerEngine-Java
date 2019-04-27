package com.cjc.frame.yy.netty.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Description: http服务创建器
 * @author cjc
 * @date Mar 5, 2019
 */
@Component
@Scope("prototype")
public class YYHttpServerCreator implements Runnable, ApplicationContextAware {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ApplicationContext mApplicationContext;

	private int mProt;

	/** http处理器 */
	private Class<? extends SimpleChannelInboundHandler<FullHttpRequest>> mHandlerClass;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.mApplicationContext = applicationContext;
	}

	/**
	 * 初始化并起线程开始
	 * @param port
	 * @param clazz
	 */
	public void initAndStartByThread(int port, Class<? extends SimpleChannelInboundHandler<FullHttpRequest>> clazz) {
		this.mProt = port;
		this.mHandlerClass = clazz;

		new Thread(this).start();
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new MyInitializer());

			sLog.info("http service startup on port:{}", mProt);
			ChannelFuture cf = b.bind(mProt).sync();
			cf.channel().closeFuture().sync();

			sLog.fatal("http service crush");
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
	 * @Description: 管道初始化
	 * @author cjc
	 * @date Mar 5, 2019
	 */
	private class MyInitializer extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline cp = ch.pipeline();
			// HTTP请求消息解码器
			cp.addLast(new HttpRequestDecoder());

			// HttpObjectAggregator解码器将多个消息转换为单一的FullHttpRequest或FullHttpResponse对象
			cp.addLast(new HttpObjectAggregator(65536));

			// HTTP响应编码器,对HTTP响应进行编码
			cp.addLast(new HttpResponseEncoder());

			// ChunkedWriteHandler的主要作用是支持异步发送大的码流,但不占用过多的内存,防止JAVA内存溢出
			cp.addLast(new ChunkedWriteHandler());

			// 业务处理器
			cp.addLast("HttpRouteHandler", mApplicationContext.getBean(mHandlerClass));
		}
	}
}
