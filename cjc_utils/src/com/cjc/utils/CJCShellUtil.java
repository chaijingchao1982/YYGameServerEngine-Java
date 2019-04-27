package com.cjc.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Description: shell执行工具
 * @author cjc
 * @date Feb 14, 2019
*/
public class CJCShellUtil {

	/**
	 * 阻塞执行
	 * @param shell
	 * @return
	 * @throws Exception
	 */
	public static String executeSyn(String shell) throws Exception {
		Process process = Runtime.getRuntime().exec(shell);
		process.waitFor();
		return getShellErr(process);
	}

	/**
	 * 获取shell执行错误
	 * @param process
	 * @return
	 * @throws Exception
	 */
	public static String getShellErr(Process process) throws Exception {
		InputStream is = process.getErrorStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String line;
		String err = "";
		while ((line = br.readLine()) != null) {
			err = err + (line + "\n");
		}
		return err;
	}
}
