package com.cjc.frame.yy.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cjc.frame.yy.handler.YYRouter;
import com.cjc.utils.CJCExceptionUtil;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author chaijingchao
 * @date 2017-12
 */
@Component
public class YYTcpWriterImpl implements IYYMsgWriter {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

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
			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(packBytes(msg));
			chc.writeAndFlush(buf);
			// buf.release(); // !!!会死机
			return true;
		}
		return false;
	}

	@Override
	public boolean writeFlush(ChannelHandlerContext chc, Builder<?> builder) {
		return writeFlush(chc, builder.build());
	}

	@Override
	public boolean writeFlush(Channel channel, Message msg) {
		if (channel == null || msg == null) {
			return false;
		}

		if (channel.isActive()) {
			//有必要做这个判断
			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(packBytes(msg));
			channel.writeAndFlush(buf);
			return true;
		}
		return false;
	}

	@Override
	public boolean writeFlush(Channel channel, Builder<?> builder) {
		if (builder == null) {
			return false;
		}
		return writeFlush(channel, builder.build());
	}

	@Override
	public boolean writeFlushSync(ChannelHandlerContext chc, Message msg) {
		if (chc == null || msg == null) {
			return false;
		}

		if (chc.channel().isActive()) {
			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(packBytes(msg));
			try {
				chc.writeAndFlush(buf).sync();
				return true;
			} catch (InterruptedException e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
		return false;
	}

	@Override
	public boolean writeFlushSync(ChannelHandlerContext chc, Class<? extends Message> msgClass, byte[] bytes) {
		if (chc == null || msgClass == null || bytes == null) {
			return false;
		}

		if (chc.channel().isActive()) {
			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(packBytes(msgClass, bytes));
			try {
				chc.writeAndFlush(buf).sync();
				return true;
			} catch (InterruptedException e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
		return false;
	}

	/**
	 * 测试半包消息
	 * @param channel
	 * @param msg
	 * @return
	 */
	public boolean testWriteHalfpackage(Channel channel, GeneratedMessageV3 msg) {
		if (msg == null || channel == null || !channel.isActive()) {
			sLog.warn("YYHandler.write err: msg is null, or channel is null or inactive.");
			return false;
		}

		// 先发第一部分包
		byte[] array = msg.toByteArray();
		ByteBuf part1Buf = Unpooled.buffer();
		part1Buf.writeInt(Integer.BYTES + array.length);

		int code = mHandlerRoute.getMsgCode(msg.getClass());
		part1Buf.writeInt(code);
		channel.writeAndFlush(part1Buf);

		// 等待1秒
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}

		// 再发第二部分包
		ByteBuf part2Buf = Unpooled.buffer();
		part2Buf.writeBytes(array);
		channel.writeAndFlush(part2Buf);
		return true;
	}

	private byte[] packBytes(Message msg) {
		return packBytes(msg.getClass(), msg.toByteArray());
	}

	/**
	 * 转成数组(带消息壳：(int)length + (int)msgCode + bytes)
	 * @param msgClass
	 * @param array
	 * @return
	 */
	private byte[] packBytes(Class<? extends Message> msgClass, byte[] array) {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;

		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);

			// len
			dos.writeInt(Integer.BYTES + array.length);

			// code
			int code = mHandlerRoute.getMsgCode(msgClass);
			dos.writeInt(code);

			// bytes
			dos.write(array);
			return baos.toByteArray();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
		return null;
	}
}
