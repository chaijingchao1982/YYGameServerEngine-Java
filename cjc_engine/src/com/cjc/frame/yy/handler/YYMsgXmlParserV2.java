package com.cjc.frame.yy.handler;

import java.io.File;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cjc.frame.yy.YYConfig;
import com.cjc.utils.CJCExceptionUtil;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @Description: MsgXml解析器(v2)
 * @author cjc
 * @date Apr 17, 2019
*/
@Component
public class YYMsgXmlParserV2 {

	protected static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	@Autowired
	private YYRouter mRouter;

	/**
	 * 解析msg.xml并初始化
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean parseAndInit() {
		File file = new File(YYConfig.MSG_XML_PATH);
		if (!file.exists()) {
			sLog.fatal("msgXmlPath file is not exist");
			return false;
		}

		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element root = doc.getRootElement();
			if (root.getName().equals(YYMsgXmlConstants.KEY_MSG_INFO_CONFIG)) {
				for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
					Element element = it.next();
					String name = element.getName();
					if (name.equals(YYMsgXmlConstants.KEY_INFO)) {
						parseInfo(element);
						continue;
					}
				}
				return true;
			}
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void parseInfo(Element element) throws Exception {
		String codeStr = element.attributeValue(YYHandlerConstants.KEY_CODE);
		int code = Integer.parseInt(codeStr);
		String msgName = element.attributeValue(YYHandlerConstants.KEY_MSG);
		Class<? extends GeneratedMessageV3> msgClass = (Class<? extends GeneratedMessageV3>) Class
				.forName(msgName);
		mRouter.mMsgCodeMap.put(msgClass, code);
	}
}
