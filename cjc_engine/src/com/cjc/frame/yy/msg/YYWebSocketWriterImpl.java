package com.cjc.frame.yy.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cjc.frame.yy.handler.YYRouter;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author cjc
 * @date Oct 30, 2018
 */
@Component
public class YYWebSocketWriterImpl implements IYYMsgWriter {

	@Autowired
	private YYRouter mHandlerRoute;

	@Autowired
	private IYYErrMsgService mErrMsgService;

	@Override
	public boolean writeFlushErrMsg(ChannelHandlerContext chc, int errCode) {
		if (chc == null) {
			return false;
		}

		GeneratedMessageV3 errMsg = mErrMsgService.create(errCode);
		assert (errMsg != null) : "errMsg can not be null";

		return writeFlush(chc, errMsg);
	}

	@Override
	public boolean writeFlush(ChannelHandlerContext chc, Message msg) {
		if (chc == null || msg == null) {
			return false;
		}

		if (chc.channel().isActive()) {
			//有必要做这个判断
			chc.writeAndFlush(createBinaryWebSocketFrame(msg));
			return true;
		}
		return false;
	}

	@Override
	public boolean writeFlush(ChannelHandlerContext chc, Builder<?> builder) {
		if (chc == null || builder == null) {
			return false;
		}

		return writeFlush(chc, builder.build());
	}

	@Override
	public boolean writeFlush(Channel channel, Message msg) {
		if (channel == null || msg == null) {
			return false;
		}

		if (channel.isActive()) {
			//有必要做这个判断
			channel.writeAndFlush(createBinaryWebSocketFrame(msg));
			return true;
		}
		return false;
	}

	@Override
	public boolean writeFlush(Channel channel, Builder<?> builder) {
		if (channel == null || builder == null) {
			return false;
		}

		return writeFlush(channel, builder.build());
	}

	@Override
	public boolean writeFlushSync(ChannelHandlerContext chc, Message msg) throws InterruptedException {
		if (chc == null || msg == null) {
			return false;
		}

		if (chc.channel().isActive()) {
			chc.writeAndFlush(createBinaryWebSocketFrame(msg)).sync();
			return true;
		}
		return false;
	}

	@Override
	public boolean writeFlushSync(ChannelHandlerContext chc, Class<? extends Message> msgClass, byte[] bytes)
			throws InterruptedException {
		if (chc == null || msgClass == null || bytes == null) {
			return false;
		}

		if (chc.channel().isActive()) {
			chc.writeAndFlush(createBinaryWebSocketFrame(msgClass, bytes)).sync();
			return true;
		}
		return false;
	}

	private BinaryWebSocketFrame createBinaryWebSocketFrame(Message msg) {
		return createBinaryWebSocketFrame(msg.getClass(), msg.toByteArray());
	}

	/**
	 * 创建二进制websocket消息
	 * @param msg
	 * @return
	 */
	private BinaryWebSocketFrame createBinaryWebSocketFrame(Class<? extends Message> msgClass, byte[] bytes) {
		final int len = bytes.length;

		//len
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(Integer.BYTES + len);

		//code
		int code = mHandlerRoute.getMsgCode(msgClass);
		buf.writeInt(code);

		//bytes
		if (len > 0) {
			buf.writeBytes(bytes);
		}
		return new BinaryWebSocketFrame(buf);
	}
}
