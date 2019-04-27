package com.cjc.utils.io;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * 包装的输入流
 * @author CJC
 * @date 2018-1
 */
public class CJCDataInputStream {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	protected DataInputStream mDis;

	public static CJCDataInputStream create(DataInputStream dis) {
		if (dis == null) {
			return null;
		}
		return new CJCDataInputStream(dis);
	}

	private CJCDataInputStream(DataInputStream dis) {
		try {
			mDis = dis;
		} catch (Exception e) {
			close();
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public void close() {
		try {
			if (mDis != null) {
				mDis.close();
				mDis = null;
			}
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	public byte readByte() throws IOException {
		return mDis.readByte();
	}

	public short readShort() throws IOException {
		return mDis.readShort();
	}

	public int readInt() throws IOException {
		return mDis.readInt();
	}

	public long readLong() throws IOException {
		return mDis.readLong();
	}

	public double readDouble() throws IOException {
		return mDis.readDouble();
	}

	public float readFloat() throws IOException {
		return mDis.readFloat();
	}

	public String readUTF() throws IOException {
		return mDis.readUTF();
	}

	@SuppressWarnings("unused")
	public byte[] readByteArray() throws IOException {
		int len = mDis.readInt();
		if (len > 0) {
			byte[] ret = new byte[len];

			// 第一种：数组读
			int readLen = mDis.read(ret);

			// 第二种：循环读
			// for (int i = 0; i < len; i++) {
			// ret[i] = mDis.readByte();
			// }
			return ret;
		}
		return null;
	}

	public short[] readShortArray() throws IOException {
		short len = mDis.readShort();
		if (len > 0) {
			short[] ret = new short[len];
			for (int i = 0; i < len; i++) {
				ret[i] = mDis.readShort();
			}
			return ret;
		}
		return null;
	}

	public int[] readIntArray() throws IOException {
		short len = mDis.readShort();
		if (len > 0) {
			int[] ret = new int[len];
			for (int i = 0; i < len; i++) {
				ret[i] = mDis.readInt();
			}
			return ret;
		}
		return null;
	}

	public float[] readFloatArray() throws IOException {
		short len = mDis.readShort();
		if (len > 0) {
			float[] ret = new float[len];
			for (int i = 0; i < len; i++) {
				ret[i] = mDis.readFloat();
			}
			return ret;
		}
		return null;
	}

	public long[] readLongArray() throws IOException {
		short len = mDis.readShort();
		if (len > 0) {
			long[] ret = new long[len];
			for (int i = 0; i < len; i++) {
				ret[i] = mDis.readLong();
			}
			return ret;
		}
		return null;
	}

	public String[] readUTFArray() throws IOException {
		short len = mDis.readShort();
		if (len > 0) {
			String[] ret = new String[len];
			for (int i = 0; i < len; i++) {
				ret[i] = readUTF();
			}
			return ret;
		}
		return null;
	}
}
