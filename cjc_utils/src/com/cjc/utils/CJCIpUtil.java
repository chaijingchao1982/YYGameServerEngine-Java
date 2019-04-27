package com.cjc.utils;

import java.util.Random;

/**
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCIpUtil {

	/** ip范围 */
	private static final int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
			{ 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
			{ 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
			{ 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
			{ 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
			{ -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
			{ -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
			{ -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
			{ -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
			{ -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
	};

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String args[]) {
		for (int i = 0; i < 100; i++) {
			System.out.println(getRandomIp());
		}
	}

	public static String getRandomIp() {
		Random random = new Random();
		int index = random.nextInt(10);
		String ip = num2Ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
		return ip;
	}

	public static String num2Ip(int ip) {
		int[] bytes = new int[4];
		bytes[0] = (int) ((ip >> 24) & 0xff);
		bytes[1] = (int) ((ip >> 16) & 0xff);
		bytes[2] = (int) ((ip >> 8) & 0xff);
		bytes[3] = (int) (ip & 0xff);
		String ret = Integer.toString(bytes[0]) + "." + Integer.toString(bytes[1]) + "." + Integer.toString(bytes[2])
				+ "." + Integer.toString(bytes[3]);
		return ret;
	}
}
