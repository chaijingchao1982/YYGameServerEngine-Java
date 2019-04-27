package com.cjc.frame.yy.handler;

import java.io.File;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cjc.frame.yy.YYConfig;
import com.cjc.utils.string.CJCStringUtil;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @Description: MsgXml解析器(v1)
 * @author cjc
 * @date Apr 17, 2019
*/
@Component
public class YYMsgXmlParserV1 implements ApplicationContextAware {

	protected static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private ApplicationContext mApplicationContext;

	@Autowired
	private YYRouter mRouter;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.mApplicationContext = applicationContext;
	}

	/**
	 * 解析msg.xml并初始化
	 * @return
	 */
	public void parseAndInit() throws Exception {
		File file = new File(YYConfig.MSG_XML_PATH);
		if (!file.exists()) {
			final String ERR = "msgXmlPath file is not exist";
			sLog.fatal(ERR);
			throw new Exception(ERR);
		}

		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		Element root = doc.getRootElement();
		if (root.getName().equals(YYMsgXmlConstants.KEY_MSG_INFO_CONFIG)) {
			parseRoot(root);
		}
	}

	@SuppressWarnings("unchecked")
	private void parseRoot(Element root) throws Exception {
		mRouter.mVersion = root.attributeValue(YYMsgXmlConstants.KEY_VERSION);

		mRouter.mMsgPackage = root.attributeValue(YYMsgXmlConstants.KEY_MSG_PACKAGE_URL);
		if (mRouter.mMsgPackage == null) {
			mRouter.mMsgPackage = "";
		}

		mRouter.mHandlerPackage = CJCStringUtil.append(root.attributeValue(YYMsgXmlConstants.KEY_HANDLER_PACKAGE_URL),
				YYHandlerConstants.DOT);

		for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
			Element element = it.next();
			String name = element.getName();
			if (name.equals(YYMsgXmlConstants.KEY_INFO)) {
				parseInfo(element);
				continue;
			}
		}
	}

	private void parseInfo(Element element) throws Exception {
		String codeStr = element.attributeValue(YYHandlerConstants.KEY_CODE);
		int code = Integer.parseInt(codeStr);
		String msgName = element.attributeValue(YYHandlerConstants.KEY_MSG);

		// 消息类全路径
		String msgClassWholeUrl = CJCStringUtil.append(mRouter.mMsgPackage, msgName);
		Class<?> msgClass = Class.forName(msgClassWholeUrl);
		if (msgClass == null) { //异常：没有加载到
			throw new Exception("can not find class");
		} else {
			@SuppressWarnings("unchecked")
			Class<GeneratedMessageV3> clazz = (Class<GeneratedMessageV3>) msgClass;
			mRouter.mMsgCodeMap.put(clazz, code);

			if (msgName.endsWith(YYHandlerConstants.REQ)) { //必须是req消息，才有处理器
				if (GeneratedMessageV3.class.isAssignableFrom(msgClass)) { //必须是GeneratedMessageV3的子类
					if (mRouter.mHandlerMap.contains(code)) { //code重复
						throw new Exception("mHandlerMap contains this code=" + code + "already");
					}
					mRouter.mHandlerMap.put(code, createHandler(code, clazz, msgName));
				} else { // 类型错误
					throw new Exception("msgClass is not extends GeneratedMessageV3");
				}
			}
		}
	}

	private YYHandler<?> createHandler(int code, Class<GeneratedMessageV3> msgClass, String msgName)
			throws Exception {
		//处理器包路径
		//去掉req
		String handlerName = msgName.substring(0, msgName.indexOf(YYHandlerConstants.REQ));
		//去掉$前面（包含$）的字符串
		int startIdx = handlerName.indexOf("$");
		if (startIdx != -1) {
			handlerName = handlerName.substring(startIdx + 1, handlerName.length());
		}
		//add handler tail
		handlerName = CJCStringUtil.append(handlerName, YYHandlerConstants.HANDLER);

		//加载处理器
		YYHandler<?> handler = null;
		String handlerClassUrl = CJCStringUtil.append(mRouter.mHandlerPackage, handlerName);
		Class<?> handlerClass = Class.forName(handlerClassUrl);
		if (handlerClass == null) {
			throw new Exception("handlerClass is not found" + handlerClass);
		} else {
			if (YYHandler.class.isAssignableFrom(handlerClass)) { //必须是YYHandler的子类
				//handler必须是单例
				handler = mApplicationContext.getBean(handlerClass.getSimpleName(), YYHandler.class);
			} else {
				//类型错误
				throw new Exception("handlerClass is not extends YYHandler");
			}
		}

		handler.init(code, msgClass);
		return handler;
	}
}
