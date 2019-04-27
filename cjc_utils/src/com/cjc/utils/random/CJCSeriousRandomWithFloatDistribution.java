package com.cjc.utils.random;

import java.util.ArrayList;
import java.util.List;

/**
 * serious random data
 * 
 * generate non repeated random numbers, and in the range of min to max, and by
 * distribution
 * 
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCSeriousRandomWithFloatDistribution {

	/** min data */
	private int mMin;

	/** max data */
	private int mMax;

	/** data list */
	private List<Integer> mDataList = new ArrayList<Integer>();

	/** 原始分布 */
	private List<Float> mOriginDistribution = new ArrayList<Float>();

	/** 运行时分布 */
	private List<Float> mRuntimeDistribution = new ArrayList<Float>();

	/**
	 * create
	 * 
	 * @param min
	 * @param max
	 * @param distribution
	 * @return
	 */
	public static CJCSeriousRandomWithFloatDistribution create(int min, int max, float[] distribution) {
		if (min >= max) {
			return null;
		}
		if (max - min + 1 != distribution.length) {
			return null;
		}
		return new CJCSeriousRandomWithFloatDistribution(min, max, distribution);
	}

	private CJCSeriousRandomWithFloatDistribution(int min, int max, float[] distribution) {
		this.mMin = min;
		this.mMax = max;

		for (float temp : distribution) {
			mOriginDistribution.add(temp);
		}

		reset();
	}

	public void reset() {
		mDataList.clear();
		for (int i = mMin; i <= mMax; i++) {
			mDataList.add(i);
		}

		mRuntimeDistribution.clear();
		for (float temp : mOriginDistribution) {
			mRuntimeDistribution.add(temp);
		}
	}

	/**
	 * get next random data
	 * 
	 * @return
	 */
	public int nextInt() {
		if (mDataList.size() == 0) {
			reset();
		}

		int idx = CJCRandomUtil.getRandomWithFloatDistribution(mRuntimeDistribution);
		mRuntimeDistribution.remove(idx);
		return mDataList.remove(idx);
	}
}
