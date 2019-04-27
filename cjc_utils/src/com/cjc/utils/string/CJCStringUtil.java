package com.cjc.utils.string;

/**
 * @author chaijingchao
 * @date 2017.8
 */
public class CJCStringUtil {

	private static final String NUMBER = "0123456789";

	@SuppressWarnings("unused")
	private static final String CHAR_AND_NUMBER = "abcdefghijklmnopqrstuvwxyz" + NUMBER;

	public static String getRandomNumberString(int length) {
		StringBuffer sb = new StringBuffer();
		int len = NUMBER.length();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				sb.append(NUMBER.charAt(getRandom(len - 2) + 1));
				continue;
			}
			sb.append(NUMBER.charAt(getRandom(len - 1)));
		}
		return sb.toString();
	}

	public static String getRandomString(int length, String strModel) {
		StringBuffer sb = new StringBuffer();
		int len = strModel.length();
		for (int i = 0; i < length; i++) {
			sb.append(strModel.charAt(getRandom(len - 1)));
		}
		return sb.toString();
	}

	private static int getRandom(int count) {
		return (int) Math.round(Math.random() * (count));
	}

	// substring split replace
	// 用stringbuffer或stringbuilder

	// 当我们调用字符串a的substring得到字符串b，其实这个操作，无非就是调整了一下b的offset和count
	// 用到的内容还是a之前的value字符数组，并没有重新创建新的专属于b的内容字符数组

	// 举个和上面重现代码相关的例子，比如我们有1G的字符串a，我们使用substring(0,2)得到了一个只有两个字符的字符串b
	// 如果b的生命周期要长于a或手动设置a为null，当垃圾回收进行后，a被回收掉，b没有回收掉，
	// 那么1G的内存占用依旧存在，因为b持有1G大小的字符数组的引用

	// 共享内容字符数组
	// 其实substring中生成的字符串与原字符串共享内容数组是一个很棒的设计
	// 这样避免了每次进行substring重新进行字符数组复制
	// 正如其文档说明的,共享内容字符数组为了就是速度

	// 如何解决
	// 对于之前比较不常见的1G字符串只截取2个字符的情况可以使用下面的代码，这样的话，就不会持有1G字符串的内容数组引用了
	// String littleString = new String(largeString.substring(0,2));

	public static String append(String... strArray) {
		StringBuffer sb = new StringBuffer(100);
		for (String s : strArray) {
			if (s != null && s.length() > 0) {
				sb.append(s);
			}
		}
		return sb.toString();
	}

	/**
	 * 首字符大写
	 * @param str
	 * @return
	 */
	public static String getHeadCharUpperCaseString(String str) {
		String headStr = str.substring(0, 1);
		String bodyStr = str.substring(1, str.length());
		return append(headStr.toUpperCase(), bodyStr);
	}
}
