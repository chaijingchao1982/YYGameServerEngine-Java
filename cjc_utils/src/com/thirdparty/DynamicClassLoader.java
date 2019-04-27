package com.thirdparty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * 动态加载class文件
 * @author Ken
 * @since 2013-02-17
 */
public class DynamicClassLoader<T> extends ClassLoader {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/** 文件最后修改时间 */
	private long mLastModified;

	/** 加载class文件的path */
	private String mClassFilePath;

	/**
	 * 检测class文件是否被修改
	 * @param filename
	 * @return
	 */
	private boolean isClassModified(String name) {
		File file = getFile(name);
		if (file.lastModified() > mLastModified) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public Class<T> loadClass(String classFilePath, String classWholeUrl) throws ClassNotFoundException {
		this.mClassFilePath = classFilePath;
		if (isClassModified(classWholeUrl)) {
			return (Class<T>) findClass(classWholeUrl);
		}
		return null;
	}

	/**
	 * 获取class文件的字节码
	 * @param name 类的全名
	 * @return
	 */
	private byte[] getBytes(String name) {
		byte[] buffer = null;
		FileInputStream in = null;
		try {
			File file = getFile(name);
			mLastModified = file.lastModified();
			in = new FileInputStream(file);
			buffer = new byte[in.available()];
			in.read(buffer);
			return buffer;
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e2) {
				CJCExceptionUtil.log(sLog, e2);
			}
		}
		return buffer;
	}

	/**
	 * 获取class文件的真实路径
	 * @param name
	 * @return
	 */
	private File getFile(String name) {
		String simpleName = "";
		String packageName = "";
		if (name.indexOf(".") != -1) {
			simpleName = name.substring(name.lastIndexOf(".") + 1);
			packageName = name.substring(0, name.lastIndexOf(".")).replaceAll("[.]", "/");
		} else {
			simpleName = name;
		}
		File file = new File(MessageFormat.format("{0}/{1}/{2}.class", mClassFilePath, packageName, simpleName));
		return file;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		byte[] byteCode = getBytes(name);
		return defineClass(null, byteCode, 0, byteCode.length);
	}
}