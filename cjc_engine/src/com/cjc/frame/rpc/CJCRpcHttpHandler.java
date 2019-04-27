package com.cjc.frame.rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cjc.utils.CJCExceptionUtil;
import com.cjc.utils.codec.CJCXorUtil;
import com.cjc.utils.netty.CJCNettyHttpResponseUtil;
import com.thirdparty.GZIPUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.internal.StringUtil;

/**
 * @author chaijingchao
 * @date 2018-11
 */
@Component
@Scope("prototype")
public class CJCRpcHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@Autowired
	private CJCRpcMgr mRpcMgr;

	@Override
	protected void channelRead0(ChannelHandlerContext chc, FullHttpRequest req) {
		try {
			if (!checkReq(req)) {
				return;
			}

			if (!checkUri(chc, req)) {
				return;
			}

			final byte[] reqBytes = parseReq(chc, req);
			if (reqBytes == null) {
				return;
			}

			byte[] respBytes = mRpcMgr.route(req.uri(), reqBytes);
			if (respBytes == null || respBytes.length == 0) {
				CJCNettyHttpResponseUtil.responseErr(chc, HttpResponseStatus.INTERNAL_SERVER_ERROR);
			} else {
				//success
				CJCNettyHttpResponseUtil.responseBytes(chc, respBytes);
			}
		} catch (Exception e) {
			sLog.info("remoteAddress={}", chc.channel().remoteAddress().toString());
			CJCExceptionUtil.log(sLog, e);
		} finally {
			chc.close();
		}
	}

	private byte[] parseReq(ChannelHandlerContext chc, FullHttpRequest req) {
		final ByteBuf buf = req.content();
		final int readableBytes = buf.readableBytes();
		if (readableBytes <= 0) {
			//无内容
			CJCNettyHttpResponseUtil.responseErr(chc, HttpResponseStatus.NOT_FOUND);
			return null;
		}

		byte[] reqBytes = new byte[readableBytes];
		buf.readBytes(reqBytes);

		reqBytes = GZIPUtils.uncompress(reqBytes);
		CJCXorUtil.xor(reqBytes);
		return reqBytes;
	}

	private boolean checkUri(ChannelHandlerContext chc, FullHttpRequest req) {
		final String uri = req.uri();
		if (StringUtil.isNullOrEmpty(uri)) {
			//没有uri
			CJCNettyHttpResponseUtil.responseErr(chc, HttpResponseStatus.NOT_FOUND);
			return false;
		}

		sLog.info("uri={}", uri);
		if (!mRpcMgr.isContains(uri)) {
			//陌生uri
			sLog.info("strange uri={}", uri);
			CJCNettyHttpResponseUtil.responseErr(chc, HttpResponseStatus.NOT_FOUND);
			return false;
		}
		return true;
	}

	private boolean checkReq(FullHttpRequest req) {
		//400
		if (!req.decoderResult().isSuccess()) {
			sLog.warn("http 400");
			return false;
		}

		//405
		if (req.method() != HttpMethod.POST) {
			sLog.warn("http 405");
			return false;
		}
		return true;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext chc, Throwable cause) throws Exception {
		sLog.error("CJCRemoteCallHttpHandler exceptionCaught client={} cause={}",
				chc.channel().remoteAddress().toString(), cause.toString());
	}
}