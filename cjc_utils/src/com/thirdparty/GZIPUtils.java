package com.thirdparty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @Description: GZIP工具类
 * @author wenqi5
 * @date 2016
 */
public class GZIPUtils {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/** 缓冲区尺寸 */
	public static final int BUFF_SIZE = 256;

	/** utf8编码 */
	public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

	/** iso编码 */
	public static final String GZIP_ENCODE_ISO_8859_1 = "ISO-8859-1";

	/**
	 * 字符串压缩为GZIP字节数组
	 * @param str
	 * @return
	 */
	public static byte[] compress(String str) {
		return compress(str, GZIP_ENCODE_UTF_8);
	}

	/**
	 * 字符串压缩为GZIP字节数组
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static byte[] compress(String str, String encoding) {
		try {
			return compress(str.getBytes(encoding));
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}

	/**
	 * 压缩
	 * @param bytes
	 * @return
	 */
	public static byte[] compress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(bytes);
			gzip.close();
			return out.toByteArray();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			try {
				if (gzip != null) {
					gzip.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
	}

	/**
	 * 解压缩
	 * @param bytes
	 * @return
	 */
	public static byte[] uncompress(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream ungzip = null;
		try {
			ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[BUFF_SIZE];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toByteArray();
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			try {
				if (ungzip != null) {
					ungzip.close();
				}
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
	}

	/**
	 * 解压成字符串
	 * @param bytes
	 * @return
	 */
	public static String uncompressToString(byte[] bytes) {
		return uncompressToString(bytes, GZIP_ENCODE_UTF_8);
	}

	/**
	 * 解压成字符串（指定编码格式）
	 * @param bytes
	 * @param encoding
	 * @return
	 */
	public static String uncompressToString(byte[] bytes, String encoding) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream ungzip = null;
		try {
			ungzip = new GZIPInputStream(in);
			byte[] buffer = new byte[BUFF_SIZE];
			int n;
			while ((n = ungzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
			return out.toString(encoding);
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			try {
				if (ungzip != null) {
					ungzip.close();
				}
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}
	}

	public static void main(String[] args) {
		try {
			String str = "abc上课到九分裤123";
			byte bs[] = GZIPUtils.compress(str.getBytes());
			byte unbs[] = GZIPUtils.uncompress(bs);
			System.out.println("解压缩后字符串：" + new String(unbs));
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public static void main2(String[] args) {
		try {
			String str = "%5B%7B%22lastUpdateTime%22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221";
			System.out.println("原长度：" + str.length());
			System.out.println("压缩后字符串：" + GZIPUtils.compress(str).toString().length());
			System.out.println("解压缩后字符串：" + new String(GZIPUtils.uncompress(GZIPUtils.compress(str)), "UTF-8"));
			System.out.println("解压缩后字符串：" + GZIPUtils.uncompressToString(GZIPUtils.compress(str)));
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}
}
