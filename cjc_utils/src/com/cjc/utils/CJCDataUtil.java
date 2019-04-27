package com.cjc.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cjc.utils.string.CJCStringUtil;

/**
 * @Description: 数据工具
 * @author chaijingchao
 * @date 2017-9
 */
public class CJCDataUtil {

	/**
	 * byte数组转int（按照大端方式）
	 * @param b
	 * @return
	 */
	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	/**
	 * int转byte数组
	 * @param a
	 * @return
	 */
	public static byte[] intToByteArray(int a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF) };
	}

	/**
	 * 字节转换为浮点
	 * @param bytes
	 * @return
	 */
	public static float byteArray2float(byte[] bytes) {
		return Float.intBitsToFloat(byteArrayToInt(bytes));
	}

	/**
	 * 浮点转换为字节数组
	 * @param f
	 * @return
	 */
	public static byte[] float2ByteArray(float f) {
		ByteBuffer buff = ByteBuffer.allocate(Float.BYTES);
		buff.putFloat(f);
		return buff.array();
	}

	/**
	 * 浮点转换为字节数组(方法2)
	 * @param f
	 * @return
	 */
	public static byte[] float2ByteArrayFun2(float f) {
		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);

		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 翻转数组
		int len = bytes.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] ret = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(bytes, 0, ret, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = ret[i];
			ret[i] = ret[len - i - 1];
			ret[len - i - 1] = temp;
		}
		return ret;
	}

	public static String intArray2String(int[] intArray) {
		if (intArray == null) {
			return "";
		}
		String ret = null;
		for (int i = 0; i < intArray.length; i++) {
			String a = String.valueOf(intArray[i]);
			if (i == 0) {
				ret = a;
			} else {
				ret = CJCStringUtil.append(ret, ",", a);
			}
		}
		return ret;
	}

	public static int[] integerArray2IntArray(Integer[] integers) {
		int[] ints = Arrays.stream(integers).mapToInt(Integer::valueOf).toArray();
		return ints;
	}

	public static Integer[] intArray2integerArray(int[] src) {
		Integer[] ret = new Integer[src.length];
		for (int i = 0; i < src.length; i++) {
			ret[i] = src[i];
		}
		return ret;
	}

	public static int[] integerList2IntArray(List<Integer> src) {
		int[] ret = new int[src.size()];
		for (int i = 0; i < src.size(); i++) {
			ret[i] = src.get(i);
		}
		return ret;
	}

	public static Integer[] integerList2Array(List<Integer> src) {
		Integer[] ret = new Integer[src.size()];
		for (int i = 0; i < src.size(); i++) {
			ret[i] = src.get(i);
		}
		return ret;
	}

	public static String[] stringList2Array(List<String> src) {
		String[] ret = new String[src.size()];
		for (int i = 0; i < src.size(); i++) {
			ret[i] = src.get(i);
		}
		return ret;
	}

	public static List<Integer> integerArray2List(Integer[] src) {
		List<Integer> ret = new ArrayList<Integer>();
		ret.addAll(Arrays.asList(src));
		return ret;
	}

	/**
	 * list转成一维数组
	 * @param list
	 * @return
	 */
	public static CJCHold<int[], int[]> intListToArray(List<int[]> list) {
		// length
		int totalLength = 0;
		int[] lengthArray = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			int[] as = list.get(i);
			if (as == null) {
				lengthArray[i] = 0;
				continue;
			}
			lengthArray[i] = as.length;
			totalLength += lengthArray[i];
		}

		// array
		int[] array = new int[totalLength];
		int idx = 0;
		for (int i = 0; i < list.size(); i++) {
			int[] as = list.get(i);
			if (as == null) {
				continue;
			}
			System.arraycopy(as, 0, array, idx, lengthArray[i]);
			idx += lengthArray[i];
		}

		// hold
		CJCHold<int[], int[]> ret = new CJCHold<int[], int[]>();
		ret.setL(lengthArray);
		ret.setR(array);
		return ret;
	}
}
