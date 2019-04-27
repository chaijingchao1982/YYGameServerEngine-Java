package com.cjc.utils.codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * 转码工具
 * @author chaijingchao
 * @date 2018-9
 */
public class CJCCodeConvertUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void main(String[] args) {
		try {
			String path = "D:\\ws_cjc_server\\cjc_spring_test\\src\\com\\cjc\\test\\entry\\";
			File srcFile = new File(path);
			File[] files = srcFile.listFiles();
			for (File f : files) {
				String fileStr = getFileContentFromCharset(f, "GBK");
				String dstPath = "C:\\Users\\chaijingchao\\Desktop\\aaa\\" + f.getName();
				saveFile2Charset(new File(dstPath), "UTF-8", fileStr);
			}

			System.out.println("end");
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	/** 
	 * 以指定编码方式读取文件，返回文件内容 
	 * @param file 				要转换的文件 
	 * @param fromCharsetName 	源文件的编码 
	 * @return 
	 * @throws Exception 
	 */
	public static String getFileContentFromCharset(File file, String fromCharsetName) throws Exception {
		if (!Charset.isSupported(fromCharsetName)) {
			throw new UnsupportedCharsetException(fromCharsetName);
		}
		InputStream inputStream = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(inputStream, fromCharsetName);
		char[] chs = new char[(int) file.length()];
		reader.read(chs);
		String str = new String(chs).trim();
		reader.close();
		return str;
	}

	/** 
	 * 以指定编码方式写文本文件，存在会覆盖 
	 * @param file 			要写入的文件 
	 * @param toCharsetName 要转换的编码 
	 * @param content 		文件内容 
	 * @throws Exception 
	 */
	public static void saveFile2Charset(File file, String toCharsetName, String content) throws Exception {
		if (!Charset.isSupported(toCharsetName)) {
			throw new UnsupportedCharsetException(toCharsetName);
		}
		OutputStream outputStream = new FileOutputStream(file);
		OutputStreamWriter outWrite = new OutputStreamWriter(outputStream, toCharsetName);
		outWrite.write(content);
		outWrite.close();
	}
}
