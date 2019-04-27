package com.cjc.utils.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.cjc.utils.CJCArrayUtil;
import com.cjc.utils.CJCDataUtil;

/**
 * 小端输出流
 * @author CJC
 * @date 2018-1
 */
public class CJCDataOutputStreamLittleEndian extends CJCDataOutputStream {

	public CJCDataOutputStreamLittleEndian(DataOutputStream dos) {
		super(dos);
	}

	@Override
	public void writeShort(short data) throws IOException {
		byte d0 = (byte) (data & 0x00FF);
		byte d1 = (byte) (data >> 8 & 0x00FF);
		mDos.writeByte(d0);
		mDos.writeByte(d1);
	}

	@Override
	public void writeInt(int data) throws IOException {
		byte d0 = (byte) (data & 0x000000FF);
		byte d1 = (byte) (data >> 8 & 0x000000FF);
		byte d2 = (byte) (data >> 16 & 0x000000FF);
		byte d3 = (byte) (data >> 24 & 0x000000FF);
		mDos.writeByte(d0);
		mDos.writeByte(d1);
		mDos.writeByte(d2);
		mDos.writeByte(d3);
	}

	@Override
	public void writeFloat(float f) throws IOException {
		byte[] bytes = CJCArrayUtil.reverseArray(CJCDataUtil.float2ByteArray(f));
		mDos.write(bytes);
	}

	// ------------------------------------------------------------------//
	// --------------------------以下代码未用到---------------------------//
	// ------------------------------------------------------------------//

	@Override
	public void writeLong(long data) throws IOException {
		byte d0 = (byte) (data & 0x000000FF);
		byte d1 = (byte) (data >> 8 & 0x000000FF);
		byte d2 = (byte) (data >> 16 & 0x000000FF);
		byte d3 = (byte) (data >> 24 & 0x000000FF);
		byte d4 = (byte) (data >> 32 & 0x000000FF);
		byte d5 = (byte) (data >> 40 & 0x000000FF);
		byte d6 = (byte) (data >> 48 & 0x000000FF);
		byte d7 = (byte) (data >> 56 & 0x000000FF);
		mDos.writeByte(d0);
		mDos.writeByte(d1);
		mDos.writeByte(d2);
		mDos.writeByte(d3);
		mDos.writeByte(d4);
		mDos.writeByte(d5);
		mDos.writeByte(d6);
		mDos.writeByte(d7);
	}

	@Override
	public void writeDouble(double data) throws IOException {
		ByteBuffer bbuf = ByteBuffer.allocate(8);
		bbuf.putDouble(data);
		byte[] bBuffer = bbuf.array();

		byte d0 = (byte) (bBuffer[0] & 0x000000FF);
		byte d1 = (byte) (bBuffer[1] & 0x000000FF);
		byte d2 = (byte) (bBuffer[2] & 0x000000FF);
		byte d3 = (byte) (bBuffer[3] & 0x000000FF);
		byte d4 = (byte) (bBuffer[4] & 0x000000FF);
		byte d5 = (byte) (bBuffer[5] & 0x000000FF);
		byte d6 = (byte) (bBuffer[6] & 0x000000FF);
		byte d7 = (byte) (bBuffer[7] & 0x000000FF);
		mDos.writeByte(d0);
		mDos.writeByte(d1);
		mDos.writeByte(d2);
		mDos.writeByte(d3);
		mDos.writeByte(d4);
		mDos.writeByte(d5);
		mDos.writeByte(d6);
		mDos.writeByte(d7);
	}
}
