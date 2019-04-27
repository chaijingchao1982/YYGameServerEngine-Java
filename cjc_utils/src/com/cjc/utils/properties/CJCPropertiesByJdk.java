package com.cjc.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @Description: 配置工具（怀疑jdk有bug，出现过：有的key-value明明在配置文件中存在，但获取失败）
 * @author chaijingchao
 * @date 2018-5
 */
public class CJCPropertiesByJdk {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/** 配置对象 */
	private Properties mProperties = new Properties();

	public CJCPropertiesByJdk(String path) {
		FileInputStream fis = null;
		try {
			File file = new File(path);
			fis = new FileInputStream(file);
			mProperties.load(fis);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
	}

	public String getString(String key) {
		String v = mProperties.getProperty(key);
		assert (v != null) : "v is null";
		return v;
	}

	public int getInt(String key) {
		String v = mProperties.getProperty(key);
		assert (v != null) : "v is null";
		return new Integer(v.trim());
	}

	public boolean getBool(String key) {
		String v = mProperties.getProperty(key);
		assert (v != null) : "v is null";
		return new Boolean(v.trim());
	}
}
