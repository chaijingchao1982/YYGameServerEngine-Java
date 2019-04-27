package com.cjc.utils;

import java.util.ArrayList;

/**
 * @Description: 数组工具
 * @author cjc
 * @date Feb 1, 2014
 */
public class CJCArrayUtil {

	/**
	 * 数组反转
	 * @param array
	 * @return
	 */
	public static byte[] reverseArray(byte[] array) {
		ArrayList<Byte> al = new ArrayList<Byte>();
		for (int i = array.length - 1; i >= 0; i--) {
			al.add(array[i]);
		}

		byte[] buffer = new byte[al.size()];
		for (int i = 0; i <= buffer.length - 1; i++) {
			buffer[i] = al.get(i);
		}
		return buffer;
	}

	public static boolean isArrayObject(Object obj) {
		if (obj == null) {
			return false;
		}

		// 反射 获得类型
		return obj.getClass().isArray();
	}
}
