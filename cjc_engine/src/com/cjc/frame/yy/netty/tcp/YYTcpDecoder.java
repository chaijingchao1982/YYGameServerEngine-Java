package com.cjc.frame.yy.netty.tcp;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cjc.frame.yy.codec.YYDecoder;
import com.cjc.frame.yy.handler.YYRouter;
import com.cjc.utils.CJCDataUtil;
import com.cjc.utils.CJCExceptionUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * tcp流解码器
 * 把数据流解码成msg，处理粘包、半包逻辑 (just for tcp, not for websocket)
 * @author chaijingchao
 * @date 2017-8
 */
public class YYTcpDecoder extends ByteToMessageDecoder {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@Autowired
	private YYRouter mRouter;

	private YYDecoder mDecoder;

	private YYTcpDecoder() {
	}

	@Override
	protected void decode(ChannelHandlerContext chc, ByteBuf in, List<Object> out) {
		//测试结论：不同连接不是一个线程，同一连接是同一线程
		//System.out.println("decode currentThread: " + Thread.currentThread().getName());

		try {
			while (true) {
				//死循环用于处理stick package
				int readableBytes = in.readableBytes();
				if (readableBytes < Integer.BYTES) {
					//连一个len都不够，先不读
					return;
				}

				//msg len
				in.markReaderIndex();
				int msgLen = readInt(in);
				if (in.readableBytes() < msgLen) {
					//half package
					in.resetReaderIndex();
					return;
				}

				//msg code
				int msgCode = readInt(in);

				//msg body
				byte[] bytes = new byte[msgLen - Integer.BYTES];
				in.readBytes(bytes);
				// in.release(); //不能释放，会异常

				if (mDecoder != null) {
					mDecoder.decode(bytes);
				}

				try {
					mRouter.route(chc, msgCode, bytes);
				} catch (Exception e) {
					//主动捕获异常，以免影响tcp数据流解析
					CJCExceptionUtil.log(sLog, e);
				}
			}
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public int readInt(ByteBuf in) {
		if (mDecoder != null) {
			byte[] bytes = new byte[4];
			for (int i = 0; i < 4; i++) {
				bytes[i] = in.readByte();
			}

			mDecoder.decode(bytes);
			return CJCDataUtil.byteArrayToInt(bytes);
		}

		return in.readInt();
	}
}