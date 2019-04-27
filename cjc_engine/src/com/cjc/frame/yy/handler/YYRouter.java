package com.cjc.frame.yy.handler;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cjc.frame.yy.YYConfig;
import com.cjc.frame.yy.YYConfig.ServerType;
import com.cjc.frame.yy.msg.IYYErrMsgService;
import com.cjc.frame.yy.msg.IYYMsgWriter;
import com.cjc.frame.yy.msg.YYTcpWriterImpl;
import com.cjc.frame.yy.msg.YYWebSocketWriterImpl;
import com.cjc.utils.CJCExceptionUtil;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

import io.netty.channel.ChannelHandlerContext;

/**
 * router
 * @author chaijingchao
 * @date 2018-4
 */
@Component
public class YYRouter implements ApplicationContextAware, InitializingBean {

	protected static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ApplicationContext mApplicationContext;

	/** msg.xml version */
	protected String mVersion;

	/** 消息包路径 */
	protected String mMsgPackage;

	/** handler包路径 */
	protected String mHandlerPackage;

	/** msgClass->msgCode */
	protected ConcurrentHashMap<Class<? extends GeneratedMessageV3>, Integer> mMsgCodeMap = new ConcurrentHashMap<>();

	/** msgCode->handler */
	protected ConcurrentHashMap<Integer, YYHandler<?>> mHandlerMap = new ConcurrentHashMap<>();

	/** 消息发送器 */
	protected IYYMsgWriter mMsgWriter;

	/** 陌生code的错误消息 */
	protected Class<? extends Message> mStrangeCodeErrMsgClass;
	protected byte[] mStrangeCodeErrMsgBytes;

	@Autowired
	private IYYErrMsgService mErrMsgService;

	@Autowired
	private YYMsgXmlParserV1 mMsgXmlParserV1;

	@Autowired
	private YYMsgXmlParserV2 mMsgXmlParserV2;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.mApplicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final Message strangeCodeErrMsg = mErrMsgService.create(YYHandlerConstants.RET_MSG_CODE_ERR);
		mStrangeCodeErrMsgClass = strangeCodeErrMsg.getClass();
		mStrangeCodeErrMsgBytes = strangeCodeErrMsg.toByteArray();

		initWriter();
	}

	private void initWriter() {
		final ServerType serverType = YYConfig.getServerType();
		if (serverType == ServerType.TCP) {
			mMsgWriter = mApplicationContext.getBean(YYTcpWriterImpl.class);
		} else if (serverType == ServerType.WEB_SOCKET) {
			mMsgWriter = mApplicationContext.getBean(YYWebSocketWriterImpl.class);
		} else {
			assert (false) : "serverType is wrong";
		}
	}

	public YYMsgXmlParserV1 getMsgXmlParserV1() {
		return mMsgXmlParserV1;
	}

	public YYMsgXmlParserV2 getMsgXmlParserV2() {
		return mMsgXmlParserV2;
	}

	public int getMsgCode(Class<? extends Message> clazz) {
		return mMsgCodeMap.get(clazz);
	}

	public void addHandler(YYHandler<?> handler) {
		try {
			final YYHandlerAnnotation annotaion = handler.getClass().getAnnotation(YYHandlerAnnotation.class);
			final int msgCode = annotaion.code();

			//获取范型T的解析方法
			ParameterizedType type = (ParameterizedType) handler.getClass().getGenericSuperclass();
			@SuppressWarnings("unchecked")
			Class<GeneratedMessageV3> msgClass = (Class<GeneratedMessageV3>) type.getActualTypeArguments()[0];

			handler.init(msgCode, msgClass);
			mHandlerMap.put(msgCode, handler);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public boolean route(ChannelHandlerContext chc, int msgCode, byte[] bytes) {
		try {
			YYHandler<?> handler = mHandlerMap.get(msgCode);
			if (handler == null) {
				sLog.error("strange msgCode={}", msgCode);
				mMsgWriter.writeFlushSync(chc, mStrangeCodeErrMsgClass, mStrangeCodeErrMsgBytes);
				chc.close();
				return false;
			}

			handler.preHandle(chc, bytes);
			return true;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return false;
		}
	}
}
