package com.thirdparty.netty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

/**
 * HTTP请求参数解析器, 支持GET, POST
 * @author whf
 * @date 12/23/15.
 */
public class MyNettyRequestParser {

	private FullHttpRequest mFullReq;

	/**
	 * 构造一个解析器
	 * 
	 * @param req
	 */
	public MyNettyRequestParser(FullHttpRequest req) {
		this.mFullReq = req;
	}

	/**
	 * 解析请求参数
	 * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
	 * @throws Exception
	 */
	public Map<String, String> parse() throws Exception {
		HttpMethod method = mFullReq.method();

		Map<String, String> parmMap = new HashMap<>();

		if (HttpMethod.GET == method) { // 是GET请求
			QueryStringDecoder decoder = new QueryStringDecoder(mFullReq.uri());
			decoder.parameters().entrySet().forEach(entry -> {
				// entry.getValue()是一个List, 只取第一个元素
				parmMap.put(entry.getKey(), entry.getValue().get(0));
			});
		} else if (HttpMethod.POST == method) { // 是POST请求
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(mFullReq);
			decoder.offer(mFullReq);

			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

			for (InterfaceHttpData parm : parmList) {
				Attribute data = (Attribute) parm;
				parmMap.put(data.getName(), data.getValue());
			}
		} else {
			throw new Exception("not get and post"); // 不支持其它方法
		}

		return parmMap;
	}
}
