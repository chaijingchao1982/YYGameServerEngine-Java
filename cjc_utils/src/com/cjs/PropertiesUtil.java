package com.cjs;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 属性文件工具
 * 
 * @author JasonChan
 * @date 2013-4-29
 */
public final class PropertiesUtil {

	/**
	 * 通过key获取name文件里属性
	 * 
	 * @param name
	 * @param key
	 * @return String
	 */
	public static String findText(String name, String key) {
		try {
			ResourceBundle resourceBundle = ResourceBundle.getBundle(name, Locale.getDefault());
			if (null != resourceBundle) {
				String value = resourceBundle.getString(key);
				return value;
			}
		} catch (Exception e) {

		}
		return "";
	}
}
