package com.cjc.utils.http;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;
import com.cjc.utils.CJCInputStreamUtil;
import com.cjc.utils.string.CJCStringUtil;

/**
 * @author chaijingchao
 * @date 2017-10
 */
public class CJCHttpRequestUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static String requestGet(String url, String param) {
		try {
			String urlAndParam = CJCStringUtil.append(url, "?", param);
			HttpURLConnection conn = (HttpURLConnection) new URL(urlAndParam).openConnection();

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.connect();

			// 获取所有响应头字段
			//Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			//for (String key : map.keySet()) {
			//	System.out.println(key + "--->" + map.get(key));
			//}

			int code = conn.getResponseCode();
			if (code == 200) {
				return CJCInputStreamUtil.readText(conn.getInputStream());
			}
			return null;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	public static byte[] requestGet(String url, String param, int timeout) {
		try {
			String urlAndParam = CJCStringUtil.append(url, "?", param);
			HttpURLConnection conn = (HttpURLConnection) new URL(urlAndParam).openConnection();

			// 设置通用的请求属性
			conn.setConnectTimeout(timeout);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.connect();

			// 获取所有响应头字段
			// Map<String, List<String>> map = conn.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }

			int code = conn.getResponseCode();
			if (code == 200) {
				return CJCInputStreamUtil.readBytes(conn.getInputStream());
			}
			return null;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	public static byte[] requestPost(String path, byte[] bytes) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setRequestMethod(CJCHttpConstants.POST);
			conn.setDoOutput(true); //设置允许写出数据,默认是不允许 false
			conn.setDoInput(true); //当前的连接可以从服务器读取内容, 默认是true
			conn.setUseCaches(false);

			OutputStream os = conn.getOutputStream();
			os.write(bytes);
			os.flush();

			//获取所有响应头字段
			//Map<String, List<String>> map = conn.getHeaderFields();
			//遍历所有的响应头字段
			//for (String key : map.keySet()) {
			//	System.out.println(key + "--->" + map.get(key));
			//}

			int code = conn.getResponseCode();
			if (code == 200) {
				return CJCInputStreamUtil.readBytes(conn.getInputStream());
			}
			return null;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	public static String requestPost(String url, String param) {
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod(CJCHttpConstants.POST);
			conn.setDoOutput(true); //设置允许写出数据,默认是不允许 false
			conn.setDoInput(true); //当前的连接可以从服务器读取内容, 默认是true

			OutputStream os = conn.getOutputStream();
			os.write(param.getBytes());
			os.flush();

			int code = conn.getResponseCode();
			if (code == 200) {
				return CJCInputStreamUtil.readText(conn.getInputStream());
			}
			return null;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	public static String requestPost(String url, Object param) {
		PrintWriter out = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();

			int code = conn.getResponseCode();
			if (code == 200) {
				return CJCInputStreamUtil.readText(conn.getInputStream());
			}
			return null;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
