package com.cjc.frame.yy.msg;

import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author cjc
 * @date Jan 3, 2019
 */
public interface IYYMsgWriter {

	/**
	 * 发送消息（异步执行）
	 * @param chc
	 * @param msg
	 */
	public boolean writeFlush(ChannelHandlerContext chc, Message msg);

	public boolean writeFlush(ChannelHandlerContext chc, Builder<?> builder);

	public boolean writeFlush(Channel channel, Message msg);

	public boolean writeFlush(Channel channel, Builder<?> builder);

	/**
	 * 发送消息（阻塞执行）
	 * @param chc
	 * @param msg
	 * @return
	 * @throws InterruptedException
	 */
	public boolean writeFlushSync(ChannelHandlerContext chc, Message msg) throws InterruptedException;

	public boolean writeFlushSync(ChannelHandlerContext chc, Class<? extends Message> msgClass, byte[] bytes)
			throws InterruptedException;

	/**
	 * 发送错误消息
	 * @param chc
	 * @param errCode
	 * @return
	 */
	public boolean writeFlushErrMsg(ChannelHandlerContext chc, int errCode);
}
