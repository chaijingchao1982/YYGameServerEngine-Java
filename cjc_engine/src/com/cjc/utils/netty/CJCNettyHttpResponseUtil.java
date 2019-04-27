package com.cjc.utils.netty;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;
import com.cjc.utils.http.CJCHttpConstants;
import com.google.protobuf.GeneratedMessageV3;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author chaijingchao
 * @date 2017-10
 */
public class CJCNettyHttpResponseUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void responseBytes(ChannelHandlerContext chc, byte[] bytes, boolean isOrigin) {
		FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.wrappedBuffer(bytes));

		HttpHeaders headers = resp.headers();
		if (isOrigin) {
			headers.set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		}
		headers.set(HttpHeaderNames.CONTENT_TYPE, CJCHttpConstants.OCTET_STREAM);
		headers.set(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes());

		try {
			chc.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE).sync();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public static void responseErr(ChannelHandlerContext chc, HttpResponseStatus errCode) {
		FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, errCode);

		HttpHeaders headers = resp.headers();
		headers.set(HttpHeaderNames.CONTENT_TYPE, CJCHttpConstants.TEXT_PLAIN);
		headers.set(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes());

		try {
			chc.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE).sync();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public static void responseBytes(ChannelHandlerContext chc, byte[] bytes) {
		responseBytes(chc, bytes, false);
	}

	public static void responseString(ChannelHandlerContext chc, String content) {
		responseString(chc, content, false);
	}

	public static void responseString(ChannelHandlerContext chc, String content, boolean isOrigin) {
		responseBytes(chc, content.getBytes(), isOrigin);
	}

	public static void responseProtoBuf(ChannelHandlerContext chc, GeneratedMessageV3 protobuf) {
		responseBytes(chc, protobuf.toByteArray(), false);
	}
}
