package com.cjc.frame.yy.handler;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.cjc.utils.CJCExceptionUtil;
import com.google.protobuf.GeneratedMessageV3;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description: handler base class
 * @author chaijingchao
 * @date 2017-8
 */
public abstract class YYHandler<R extends GeneratedMessageV3> implements InitializingBean {

	/** logger (create logger by subclass)*/
	protected final Logger mLog;

	private int mMsgCode;

	private Method mProtoBufParseFromMethod;

	@Autowired
	protected YYRouter mRouter;

	public YYHandler() {
		final String subclassName = this.getClass().getName();
		mLog = LogManager.getLogger(subclassName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.getClass().getAnnotation(YYHandlerAnnotation.class) != null) {
			//has annotation，so add to router
			mRouter.addHandler(this);
		}
	}

	protected void init(int msgCode, Class<GeneratedMessageV3> msgClass) {
		try {
			this.mMsgCode = msgCode;
			this.mProtoBufParseFromMethod = msgClass.getMethod("parseFrom", byte[].class);
		} catch (Exception e) {
			CJCExceptionUtil.log(mLog, e);
		}
	}

	public int getMsgCode() {
		return mMsgCode;
	}

	/**
	 * handle data from the client
	 * @param chc
	 * @param message bytes
	 */
	protected void preHandle(ChannelHandlerContext chc, byte[] bytes) {
		try {
			GeneratedMessageV3 req = (GeneratedMessageV3) mProtoBufParseFromMethod.invoke(null, bytes);
			handle(chc, req);
		} catch (Exception e) {
			CJCExceptionUtil.log(mLog, e);
		}
	}

	/**
	 * handle the message
	 * @param chc
	 * @param req
	 */
	protected abstract void handle(ChannelHandlerContext chc, GeneratedMessageV3 req);
}
