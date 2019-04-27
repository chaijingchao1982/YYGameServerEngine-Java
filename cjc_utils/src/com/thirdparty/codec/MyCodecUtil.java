package com.thirdparty.codec;

import java.security.Key;
import java.util.Base64;

import com.thirdparty.codec.aes.AesUtil;
import com.thirdparty.codec.md5.MyMD5Util;

/**
 * @Description: 加解密工具
 * @author cjc
 * @date Nov 6, 2018
*/
public class MyCodecUtil {

	/**
	 * 获取md5和aes加密的结果
	 * @param src
	 * @param aesKey
	 * @return
	 */
	public static String[] getMD5AndAes(String src, Key aesKey) {
		byte[] bytes = AesUtil.encrypt(src, aesKey);
		String base64Str = Base64.getEncoder().encodeToString(bytes);
		return new String[] { MyMD5Util.md5(src), base64Str };
	}
}
