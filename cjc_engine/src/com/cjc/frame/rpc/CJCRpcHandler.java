package com.cjc.frame.rpc;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.cjc.utils.CJCExceptionUtil;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @Description: 远程调用处理器
 * @author cjc
 * @date Dec 19, 2018
*/
public abstract class CJCRpcHandler<REQ extends GeneratedMessageV3, RESP extends GeneratedMessageV3>
		implements InitializingBean {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	protected Method mProtoBufParseFromMethod;

	@Autowired
	protected CJCRpcMgr mRpcMgr;

	@Override
	public void afterPropertiesSet() throws Exception {
		mProtoBufParseFromMethod = getReqClass().getMethod("parseFrom", byte[].class);
		mRpcMgr.add(this);
	}

	/**
	 * 获取req类
	 * @return
	 */
	public Class<REQ> getReqClass() {
		@SuppressWarnings("unchecked")
		Class<REQ> ret = (Class<REQ>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		return ret;
	}

	/**
	 * 来调用了
	 * @param reqBytes
	 * @return
	 */
	public byte[] handler(byte[] reqBytes) {
		try {
			@SuppressWarnings("unchecked")
			REQ req = (REQ) mProtoBufParseFromMethod.invoke(null, reqBytes);
			GeneratedMessageV3 resp = call(req);
			return resp.toByteArray();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	/**
	 * 调用
	 * @param req
	 * @return
	 */
	public abstract RESP call(REQ req);
}
