package com.cjc.utils.io;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 大端输出流
 * @author CJC
 * @date 2018-1
 */
public class CJCDataOutputStreamBigEndian extends CJCDataOutputStream {

	public CJCDataOutputStreamBigEndian(DataOutputStream dos) {
		super(dos);
	}

	@Override
	public void writeShort(short data) throws IOException {
		mDos.writeShort(data);
	}

	@Override
	public void writeInt(int data) throws IOException {
		mDos.writeInt(data);
	}

	@Override
	public void writeFloat(float f) throws IOException {
		mDos.writeFloat(f);

		// 这样写也对
		// byte[] bytes = CJCDataUtil.float2ByteArray(f);
		// mDos.write(bytes);
	}

	// ------------------------------------------------------------------//
	// --------------------------以下代码未用到---------------------------//
	// ------------------------------------------------------------------//

	@Override
	public void writeLong(long data) throws IOException {
		mDos.writeLong(data);
	}

	@Override
	public void writeDouble(double data) throws IOException {
		mDos.writeDouble(data);
	}
}
