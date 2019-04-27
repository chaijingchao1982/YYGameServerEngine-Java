package com.cjc.utils.io;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * 包装的输出流
 * @author CJC
 * @date 2018-1
 */
public abstract class CJCDataOutputStream {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/** 空字符串 */
	public static final String STR_NULL = "";

	protected DataOutputStream mDos;

	/**
	 * 创建
	 * @param dos
	 * @param isBigEndian 是否大端
	 * @return
	 */
	public static CJCDataOutputStream create(DataOutputStream dos, boolean isBigEndian) {
		if (dos == null) {
			return null;
		}

		if (isBigEndian) {
			return new CJCDataOutputStreamBigEndian(dos);
		} else {
			return new CJCDataOutputStreamLittleEndian(dos);
		}
	}

	/**
	 * 创建（默认大端）
	 * @param dos
	 * @return
	 */
	public static CJCDataOutputStream create(DataOutputStream dos) {
		if (dos == null) {
			return null;
		}
		return new CJCDataOutputStreamBigEndian(dos);
	}

	public CJCDataOutputStream(DataOutputStream dos) {
		try {
			mDos = dos;
		} catch (Exception e) {
			close();
			CJCExceptionUtil.log(sLog, e);
		}
	}

	/**
	 * 关闭流
	 */
	public final void close() {
		try {
			if (mDos != null) {
				mDos.close();
				mDos = null;
			}
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public final void writeByte(byte data) throws IOException {
		mDos.writeByte(data);
	}

	public abstract void writeShort(short data) throws IOException;

	public abstract void writeInt(int data) throws IOException;

	public abstract void writeFloat(float f) throws IOException;

	public final void writeUTF(String str) throws IOException {
		if (str == null) {
			mDos.writeUTF(STR_NULL);
			return;
		}
		mDos.writeUTF(str);
	}

	public final void writeByteArray(byte[] array) throws IOException {
		int len = (array == null ? 0 : array.length);
		writeInt(len);
		if (len > 0) {
			mDos.write(array);
		}
	}

	public final void writeShortArray(short[] array) throws IOException {
		int len = (array == null ? 0 : array.length);
		writeShort((short) len);
		if (len > 0) {
			for (int i = 0; i < array.length; i++) {
				writeShort(array[i]);
			}
		}
	}

	public final void writeIntArray(int[] array) throws IOException {
		int len = (array == null ? 0 : array.length);
		writeShort((short) len);
		if (len > 0) {
			for (int i = 0; i < array.length; i++) {
				writeInt(array[i]);
			}
		}
	}

	public final void writeFloatArray(float[] array) throws IOException {
		int len = (array == null ? 0 : array.length);
		writeShort((short) len);
		if (len > 0) {
			for (int i = 0; i < array.length; i++) {
				writeFloat(array[i]);
			}
		}
	}

	public final void writeUTFArray(String[] array) throws IOException {
		int len = (array == null ? 0 : array.length);
		writeShort((short) len);
		if (len > 0) {
			for (int i = 0; i < array.length; i++) {
				writeUTF(array[i]);
			}
		}
	}

	// ------------------------------------------------------------------//
	// --------------------------以下代码未用到---------------------------//
	// ------------------------------------------------------------------//

	public final void writeLongArray(long[] array) throws IOException {
		int len = (array == null ? 0 : array.length);
		writeShort((short) len);
		if (len > 0) {
			for (int i = 0; i < array.length; i++) {
				writeLong(array[i]);
			}
		}
	}

	public abstract void writeLong(long data) throws IOException;

	public abstract void writeDouble(double data) throws IOException;
}
