package com.cjc.utils;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author cjc
 * @date Apr 23, 2019
 */
public class CJCDom4jUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static boolean writeFile(Document doc, String filePath) {
		XMLWriter writer = null;
		try {
			OutputFormat of = new OutputFormat("\t", true);
			of.setEncoding("UTF-8");
			writer = new XMLWriter(new FileOutputStream(filePath), of);
			writer.write(doc);
			return true;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return false;
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					CJCExceptionUtil.log(sLog, e);
				}
			}
		}
	}
}
