package com.cjc.frame.rpc;

import com.cjc.utils.codec.CJCXorUtil;
import com.cjc.utils.http.CJCHttpRequestUtil;
import com.thirdparty.GZIPUtils;

/**
 * @Description: 远程调用者基类
 * @author cjc
 * @date Dec 19, 2018
*/
public class CJCRpcCaller {

	protected String mUrl;

	public void init(String url) {
		this.mUrl = url;
	}

	public static byte[] requestPost(String url, byte[] bytes) {
		CJCXorUtil.xor(bytes);
		return CJCHttpRequestUtil.requestPost(url, GZIPUtils.compress(bytes));
	}
}
