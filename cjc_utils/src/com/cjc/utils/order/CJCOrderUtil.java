package com.cjc.utils.order;

/**
 * 排序工具
 * @author chaijingchao
 * @date 2017-8
 */
public class CJCOrderUtil {

	/**
	 * (byte)冒泡排序
	 * @param src
	 * @return
	 */
	public static byte[] orderByteArray(byte src[]) {
		for (int i = 0; i < src.length - 1; i++) {
			for (int j = i + 1; j < src.length; j++) {
				if (src[i] > src[j]) {
					byte temp = src[i];
					src[i] = src[j];
					src[j] = temp;
				}
			}
		}
		return src;
	}

	/**
	 * (int)冒泡排序
	 * 
	 * @param src
	 * @return
	 */
	public static int[] orderIntArray(int src[]) {
		for (int i = 0; i < src.length - 1; i++) {
			for (int j = i + 1; j < src.length; j++) {
				if (src[i] > src[j]) {
					int temp = src[i];
					src[i] = src[j];
					src[j] = temp;
				}
			}
		}
		return src;
	}
}
