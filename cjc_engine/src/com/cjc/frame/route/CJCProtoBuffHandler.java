package com.cjc.frame.route;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import com.google.protobuf.GeneratedMessageV3;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author chaijingchao
 * @date Mar 5, 2019
 */
public abstract class CJCProtoBuffHandler<R extends GeneratedMessageV3> extends CJCHandler<R> {

	private static final String PARSE_FROM = "parseFrom";

	private Method mProtoBufParseFromMethod;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();

		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		@SuppressWarnings("unchecked")
		Class<R> clazz = (Class<R>) type.getActualTypeArguments()[0];
		mProtoBufParseFromMethod = clazz.getMethod(PARSE_FROM, byte[].class);
	}

	@Override
	public void preHandle(ChannelHandlerContext chc, byte[] bytes) throws Exception {
		@SuppressWarnings("unchecked")
		R req = (R) mProtoBufParseFromMethod.invoke(null, bytes);

		handle(chc, req);
	}
}
