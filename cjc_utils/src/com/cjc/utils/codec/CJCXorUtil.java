package com.cjc.utils.codec;

/**
 * @Description: xor util
 * @author cjc
 * @date 2018
 */
public class CJCXorUtil {

	/** default xor code */
	private static final byte[] XOR_CODE_ARRAY = "123456".getBytes();

	/**
	 * test
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String args[]) {
		byte[] a = { 1, 2, 3, 4, 5, 6 };
		int b = 0;

		xor(a, XOR_CODE_ARRAY);
		b++;

		xor(a, XOR_CODE_ARRAY);
		b++;
	}

	/**
	 * 使用指定的xors
	 * @param bytes
	 * @param xors
	 */
	public static void xor(byte[] bytes, byte[] xors) {
		final int xorsLen = xors.length;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] ^= xors[i % xorsLen];
		}
	}

	/**
	 * use the default xor code
	 * @param bytes
	 */
	public static void xor(byte[] bytes) {
		xor(bytes, XOR_CODE_ARRAY);
	}
}
