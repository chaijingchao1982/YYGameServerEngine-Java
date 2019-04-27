package com.cjc.utils;

import java.util.Date;
import java.util.UUID;

import com.cjc.utils.string.CJCStringUtil;

/**
 * @author chaijingchao
 * @date 2018-4
 */
public class CJCTokenUtil {

	public static String createToken() {
		return createDifferentToken(null);
	}

	public static String createDifferentToken(String oldToken) {
		while (true) {
			String newToken = CJCStringUtil.append(new Date().toString(), UUID.randomUUID().toString());
			if (oldToken == null || !newToken.equals(oldToken)) {
				return newToken;
			}
		}
	}
}
