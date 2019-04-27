package com.cjc.frame.rpc;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * @Description: 远程调用管理器
 * @author cjc
 * @date Dec 19, 2018
*/
@Component
public class CJCRpcMgr {

	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String, CJCRpcHandler> mHandlerMap = new ConcurrentHashMap<>();

	protected void add(@SuppressWarnings("rawtypes") CJCRpcHandler handler) {
		CJCRpc annotaion = handler.getClass().getAnnotation(CJCRpc.class);
		String uri = annotaion.value();
		mHandlerMap.put(uri, handler);
	}

	public boolean isContains(String uri) {
		return mHandlerMap.containsKey(uri);
	}

	public byte[] route(String uri, byte[] reqBytes) {
		@SuppressWarnings("rawtypes")
		CJCRpcHandler handler = mHandlerMap.get(uri);
		if (handler == null) {
			return null;
		}
		return handler.handler(reqBytes);
	}

}
