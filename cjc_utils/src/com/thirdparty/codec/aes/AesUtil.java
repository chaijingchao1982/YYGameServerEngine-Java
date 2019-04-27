package com.thirdparty.codec.aes;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @Description: Aes工具
 * @date Nov 6, 2018
*/
public class AesUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * 生成key，作为加密和解密密钥且只有密钥相同解密加密才会成功
	 * @return
	 */
	public static Key createKey() {
		try {
			// 生成key
			KeyGenerator keyGenerator;
			//构造密钥生成器，指定为AES算法,不区分大小写
			keyGenerator = KeyGenerator.getInstance("AES");
			//生成一个128位的随机源,根据传入的字节数组
			keyGenerator.init(128);
			//产生原始对称密钥
			SecretKey secretKey = keyGenerator.generateKey();
			//获得原始对称密钥的字节数组
			byte[] keyBytes = secretKey.getEncoded();
			// key转换,根据字节数组生成AES密钥
			Key key = new SecretKeySpec(keyBytes, "AES");
			return key;
		} catch (NoSuchAlgorithmException e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	/**
	 * 生成key(二进制数组)
	 * @return
	 */
	public static byte[] createKeyBytes() {
		try {
			// 生成key
			KeyGenerator keyGenerator;
			//构造密钥生成器，指定为AES算法,不区分大小写
			keyGenerator = KeyGenerator.getInstance("AES");
			//生成一个128位的随机源,根据传入的字节数组
			keyGenerator.init(128);
			//产生原始对称密钥
			SecretKey secretKey = keyGenerator.generateKey();
			//获得原始对称密钥的字节数组
			byte[] keyBytes = secretKey.getEncoded();
			return keyBytes;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	/**
	 * 加密
	 * @param src 需加密的明文
	 * @param key 密钥
	 * @return
	 */
	public static byte[] encrypt(String src, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			//将加密并编码后的内容解码成字节数组
			byte[] result = cipher.doFinal(src.getBytes());
			//System.out.println("jdk aes:" + Base64.getEncoder().encode(result));
			return result;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	public static String encryptBase64(String src, Key key) {
		byte[] bs = encrypt(src, key);
		return Base64.getEncoder().encodeToString(bs);
	}

	/** 
	 * 解密
	 * @param src 加密后的密文byte数组
	 * @param key 密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] src, Key key) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			//初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			src = cipher.doFinal(src);
			return src;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

}
