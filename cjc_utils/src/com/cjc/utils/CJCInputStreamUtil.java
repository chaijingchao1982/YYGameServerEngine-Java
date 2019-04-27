package com.cjc.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author cjc
 * @date Dec 20, 2018
*/
public class CJCInputStreamUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static String readText(InputStream is) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			String line;
			String ret = "";
			while ((line = br.readLine()) != null) {
				ret += line;
			}
			return ret;
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			if (br != null) {
				br.close();
			}
			is.close();
		}
	}

	public static byte[] readBytes(InputStream is) throws Exception {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			if (baos != null) {
				baos.close();
			}
			is.close();
		}
	}
}
