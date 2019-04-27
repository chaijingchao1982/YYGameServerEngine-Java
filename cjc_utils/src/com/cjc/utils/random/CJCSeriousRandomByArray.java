package com.cjc.utils.random;

import java.util.Random;

/**
 * @Description: 严格随机数生成类。
 *               <p>
 *               例如：你取0—9的随机数，在0-9都获被取过一次之前，不会有某个数出现2次。
 *               <p>
 *               注意：如果取的范围比较大，会有较大内存开销，会创建等范围数组保存出现状态。
 * @author CJC
 * @date 2011-0512
 */
public class CJCSeriousRandomByArray {

	/** 随机数种子，内部算法使用 */
	private static Random sRandom = new Random();

	/** 随机数分布数组 */
	private int mDataArray[];

	/** 一次随机过程中，已经获取过数据，和未获取过的数据的分界index */
	private int mBoundaryIdx;

	/**
	 * 工厂方法
	 * 
	 * @param maxSize
	 *            要取随机数的范围，例如10就是取0~9的随机数
	 * @return
	 */
	public static CJCSeriousRandomByArray create(int maxSize) {
		if (maxSize <= 0) {
			System.out.println("Err! CJCAbsoluteRandom maxSize must more than 0.");
			return null;
		}
		return new CJCSeriousRandomByArray(maxSize);
	}

	/**
	 * 构造
	 * 
	 * @param maxSize
	 */
	private CJCSeriousRandomByArray(int maxSize) {
		mDataArray = new int[maxSize];
		reset();
	}

	/**
	 * 在每个随机过程开始前，初始化随机数分布的功能函数，仅内部算法使用
	 */
	private void reset() {
		for (int i = 0; i < mDataArray.length; i++) {
			mDataArray[i] = i;
		}
		mBoundaryIdx = mDataArray.length;
	}

	/**
	 * 获取随机数的函数
	 * 
	 * @return 获取出来的随机数
	 */
	public int nextInt() {
		// 一个随机过程结束以后，需要重新初始化随机数分布。
		// 比如要取0-9的随机数，当0-9都获取过了一次以后就需要重新初始化一下。
		if (mBoundaryIdx == 0) {
			reset();
		}

		// 要获取的随机数的位置，只在未获取过的数据中找一个。
		int idx = sRandom.nextInt(mBoundaryIdx);

		// 获取过的随机数，就放到分界点的后面，以便实现每个数在一个随机过程中只被获取一次
		mBoundaryIdx--;
		int temp = mDataArray[mBoundaryIdx];
		mDataArray[mBoundaryIdx] = mDataArray[idx];
		mDataArray[idx] = temp;

		return mDataArray[mBoundaryIdx];
	}
}
