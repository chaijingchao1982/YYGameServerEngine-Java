package com.cjc.frame.yy.netty.tcp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author chaijingchao
 * @date 2017-8
 */
public class YYTcpConnectHandler extends ChannelInboundHandlerAdapter {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	protected ChannelGroup mChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public ChannelGroup getChannelGroup() {
		return mChannelGroup;
	}

	/**
	 * 新连接已加入
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext chc) throws Exception {
		Channel c = chc.channel();
		mChannelGroup.add(chc.channel());
		sLog.info("handlerAdded {}", c.remoteAddress().toString());
	}

	/**
	 * 连接已断开
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext chc) throws Exception {
		Channel c = chc.channel();
		mChannelGroup.remove(c);
		sLog.info("handlerRemoved {}", c.remoteAddress().toString());
	}

	/**
	 * 连接激活
	 */
	@Override
	public void channelActive(ChannelHandlerContext chc) throws Exception {
		Channel c = chc.channel();
		sLog.info("channelActive {}", c.remoteAddress().toString());
	}

	/**
	 * 连接掉线
	 */
	@Override
	public void channelInactive(ChannelHandlerContext chc) throws Exception {
		Channel c = chc.channel();
		sLog.info("channelInactive {}", c.remoteAddress().toString());
	}

	/**
	 * 连接异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext chc, Throwable cause) throws Exception {
		Channel c = chc.channel();
		// cause.printStackTrace();
		sLog.warn("exceptionCaught client={} cause={}", c.remoteAddress().toString(), cause.toString());

		// 出现异常，关闭连接
		chc.close();
	}
}
