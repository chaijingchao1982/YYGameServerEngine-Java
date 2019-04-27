package com.cjc.utils.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 中文字符串工具
 * @author cjc
 * @date Oct 15, 2018
*/
public class CJCCnStringUtil {

	/** 中文样式 */
	private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

	public static void main(String args[]) {
		if (true) {
			System.out.println("1:" + isContainChinese("body=大乐?2.1&"));
			System.out.println("2:" + isContainChinese("asjkdfhsdfh1029381298/，，，,../123;123"));
			System.out.println("3:" + isContainChinese("可视对讲"));
			System.out.println("4:" + isContainChinese("lsdjflsjdfskldjflsdjfkl"));
			System.out.println();

			System.out.println("11:" + filterChinese("body=大，，乐2.1&"));
			System.out.println("22:" + filterChinese("asjkdfhsdfh1029381298/,../123;123"));
			System.out.println("33:" + filterChinese("可视对讲"));
			System.out.println("44:" + filterChinese("lsdjflsjdfskldjflsdjfkl"));
		}
	}

	/**
	 * 是否包含中文字符（不能判断是否包含中文标点）
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		Matcher m = CHINESE_PATTERN.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 过滤掉中文（不能过滤掉中文标点）
	 * @param str
	 * @return
	 */
	public static String filterChinese(String str) {
		// 用于返回结果
		String ret = str;
		boolean flag = isContainChinese(str);
		if (flag) {// 包含中文
			// 用于拼接过滤中文后的字符
			StringBuffer sb = new StringBuffer();
			// 用于校验是否为中文
			boolean flag2 = false;
			// 用于临时存储
			char chinese = 0;
			// 5.去除掉文件名中的中文
			// 将字符串转换成char[]
			char[] charArray = str.toCharArray();
			// 过滤到中文及中文字符
			for (int i = 0; i < charArray.length; i++) {
				chinese = charArray[i];
				flag2 = isContainChinese("" + chinese);
				if (!flag2) {// 不是中日韩文字及标点符号
					sb.append(chinese);
				}
			}
			ret = sb.toString();
		}
		return ret;
	}
}
