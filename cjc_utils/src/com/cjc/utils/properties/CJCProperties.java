package com.cjc.utils.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @Description: 配置工具
 * @author cjc
 * @date Nov 28, 2018
*/
public class CJCProperties {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private final Hashtable<String, String> mHashtable = new Hashtable<>();

	public CJCProperties(String path) {
		File file = new File(path);
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("//") || line.startsWith("--")) {
					continue;
				}

				String[] array = line.split("=");
				if (array.length != 2) {
					continue;
				}
				if (array[0] == null || array[1] == null) {
					continue;
				}
				if (array[0].equals("") || array[1].equals("")) {
					continue;
				}

				mHashtable.put(array[0].trim(), array[1].trim());
			}
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception ex) {
					CJCExceptionUtil.log(sLog, ex);
				}
			}
		}
	}

	public String getString(String key) {
		String value = mHashtable.get(key);
		assert (value != null) : "value is null";
		return value;
	}

	public int getInt(String key) {
		String value = mHashtable.get(key);
		assert (value != null) : "value is null";
		return new Integer(value);
	}

	public boolean getBool(String key) {
		String value = mHashtable.get(key);
		assert (value != null) : "value is null";
		return new Boolean(value);
	}

	public int getInt(String key, int defaultRet) {
		if (mHashtable.containsKey(key)) {
			return getInt(key);
		}
		return defaultRet;
	}

	public float getFloat(String key) {
		String value = mHashtable.get(key);
		assert (value != null) : "value is null";
		return new Float(value);
	}
}
