package com.thirdparty.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @Description: http工具
 * @author 来自于互联网
 * @date Mar 9, 2019
 */
public class HttpUtils {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://182.92.228.160:80/otrue-cn/healthmanager/chatfiles/22ecda70-ad43-11e5-9531-d7e3b2ec0d8e";
		String localPath = "E:/ceshi/";
		String type = "jpg";
		String filename = downloadFile(localPath, url, type);
		System.out.println(filename);
	}

	/**
	 * LOCAL_PATH 文件存储的位置 fileUrl 待下载文件地址 type 文件类型 jpg,png,mp3...
	 * @param localPath
	 * @param fileUrl
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String downloadFile(String localPath, String fileUrl, String type) {
		InputStream in = null;
		OutputStream out = null;
		HttpURLConnection conn = null;
		String fileName = null;
		try {
			// 初始化连接
			URL url = new URL(fileUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// 获取文件名
			String disposition = conn.getHeaderField("Content-Disposition");
			if (disposition != null && !"".equals(disposition)) {
				// 从头中获取文件名
				fileName = disposition.split(";")[1].split("=")[1].replaceAll("\"", "");
			} else {
				// 从地址中获取文件名
				fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			}

			if (fileName != null && !"".equals(fileName)) {
				// 文件名解码
				fileName = URLDecoder.decode(fileName, "utf-8") + ".jpg";
			} else {
				// 如果无法获取文件名，则随机生成一个
				fileName = "file_" + (int) (Math.random() * 10) + type;
			}

			// 读取数据
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				byte[] buffer = new byte[2048];
				in = conn.getInputStream();
				out = new FileOutputStream(new File(localPath, fileName));
				int count = 0;
				int finished = 0;
				int size = conn.getContentLength();
				while ((count = in.read(buffer)) != -1) {
					if (count != 0) {
						out.write(buffer, 0, count);
						finished += count;
						// System.out.printf("---->%1$.2f%%\n",(double)finished/size*100);
					} else {
						break;
					}
				}
			}
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				out.close();
				in.close();
				conn.disconnect();
			} catch (IOException e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}

		return fileName;
	}
}