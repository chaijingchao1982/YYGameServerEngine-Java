package com.cjc.frame.route;

import org.springframework.beans.factory.annotation.Autowired;

import com.cjc.frame.yy.codec.YYXorDecoder;
import com.google.protobuf.GeneratedMessageV3;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author chaijingchao
 * @date Mar 5, 2019
 */
public abstract class CJCXorProtoBuffHandler<R extends GeneratedMessageV3> extends CJCProtoBuffHandler<R> {

	@Autowired
	private YYXorDecoder mXorDecoder;

	@Override
	public void preHandle(ChannelHandlerContext chc, byte[] bytes) throws Exception {
		mXorDecoder.decode(bytes);
		super.preHandle(chc, bytes);
	}
}
