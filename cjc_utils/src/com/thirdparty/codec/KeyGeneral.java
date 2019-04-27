package com.thirdparty.codec;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @Description: key生成工具
 * @date Nov 6, 2018
 */
public class KeyGeneral {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * AES 128,256
	 * DEA 56
	 */
	//===========================加密方式======================\\
	private static final String ALGORITHM_HMACMD5 = "HmacMD5";
	private static final String ALGORITHM_AES = "AES";
	private static final String ALGORITHM_DES = "DES";

	//===========================加密类型于算法无关======================\\
	//默认
	public static String initDefault() {
		String key = "";
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_HMACMD5);
			SecretKey secretKey = generator.generateKey();
			byte[] bytes = encode(secretKey.getEncoded());
			key = new String(bytes);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return key;
	}

	//安全随机种子
	public static String initAESKey(int keysize, long seed) {
		String key = "";
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_AES);
			SecureRandom random = new SecureRandom();
			random.setSeed(seed);
			generator.init(keysize, random);
			SecretKey secretKey = generator.generateKey();
			byte[] bytes = encode(secretKey.getEncoded());
			key = new String(bytes);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return key;
	}

	//安全随机种子
	public static String initDESKey(int keysize, long seed) {
		String key = "";
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_DES);
			SecureRandom random = new SecureRandom();
			random.setSeed(seed);
			generator.init(keysize, random);
			SecretKey secretKey = generator.generateKey();
			byte[] bytes = encode(secretKey.getEncoded());
			key = new String(bytes);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return key;
	}

	//===========================base64======================\\
	public static byte[] encode(byte[] plainBytes) {
		return Base64.getEncoder().encode(plainBytes);
	}

	public static byte[] decode(byte[] cipherText) {
		return Base64.getDecoder().decode(cipherText);
	}

}
