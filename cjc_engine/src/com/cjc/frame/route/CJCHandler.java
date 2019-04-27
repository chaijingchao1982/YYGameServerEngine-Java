package com.cjc.frame.route;

import org.springframework.beans.factory.InitializingBean;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Description: handler base class
 * @author chaijingchao
 * @date 2018-9
 */
public abstract class CJCHandler<R> implements InitializingBean {

	protected CJCRouter mRouter;

	protected boolean isCompress = true;

	@Override
	public void afterPropertiesSet() throws Exception {
		CJCHandlerAnnotation annotaion = this.getClass().getAnnotation(CJCHandlerAnnotation.class);
		isCompress = annotaion.isCompress();

		mRouter = createRouter();
		mRouter.add(this);
	}

	public boolean isCompress() {
		return isCompress;
	}

	public abstract CJCRouter createRouter();

	public abstract void preHandle(ChannelHandlerContext chc, byte[] bytes) throws Exception;

	public abstract void handle(ChannelHandlerContext chc, R req) throws Exception;
}
