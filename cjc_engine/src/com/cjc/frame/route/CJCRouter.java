package com.cjc.frame.route;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author chaijingchao
 * @date 2018-9
 */
public class CJCRouter {

	/** key->handler */
	private ConcurrentHashMap<String, CJCHandler<?>> mHandlerMap = new ConcurrentHashMap<>();

	public void add(CJCHandler<?> handler) {
		CJCHandlerAnnotation annotaion = handler.getClass().getAnnotation(CJCHandlerAnnotation.class);
		final String key = annotaion.key();
		mHandlerMap.put(key, handler);
	}

	public boolean route(ChannelHandlerContext chc, String key, byte[] bytes) throws Exception {
		CJCHandler<?> handler = mHandlerMap.get(key);
		if (handler == null) {
			return false;
		}

		handler.preHandle(chc, bytes);
		return true;
	}

	public boolean isContainsKey(String key) {
		return mHandlerMap.containsKey(key);
	}

	public CJCHandler<?> get(String key) {
		return mHandlerMap.get(key);
	}
}
