package com.cjc.utils.random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cjc.utils.CJCDataUtil;

/**
 * serious random data
 * 
 * generate non repeated random numbers, and in the range of min to max, and by
 * distribution
 * 
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCSeriousRandomWithIntegerDistribution {

	/** min data */
	private int mMin;

	/** max data */
	private int mMax;

	/** data list */
	private List<Integer> mDataList = new ArrayList<Integer>();

	/** 原始分布 */
	private List<Integer> mOriginDistribution = new ArrayList<Integer>();

	/** 运行时分布 */
	private List<Integer> mRuntimeDistribution = new ArrayList<Integer>();

	/**
	 * 创建
	 * @param min
	 * @param max
	 * @param distribution
	 * @return
	 */
	public static CJCSeriousRandomWithIntegerDistribution create(int min, int max, int[] distribution) {
		if (min >= max) {
			return null;
		}
		if (max - min + 1 != distribution.length) {
			return null;
		}
		return new CJCSeriousRandomWithIntegerDistribution(min, max, distribution);
	}

	private CJCSeriousRandomWithIntegerDistribution(int min, int max, int[] distribution) {
		this.mMin = min;
		this.mMax = max;
		this.mOriginDistribution = Arrays.asList(CJCDataUtil.intArray2integerArray(distribution));

		reset();
	}

	public void reset() {
		mDataList.clear();
		for (int i = mMin; i <= mMax; i++) {
			mDataList.add(i);
		}

		mRuntimeDistribution.clear();
		for (Integer temp : mOriginDistribution) {
			mRuntimeDistribution.add(temp);
		}
	}

	/**
	 * get next random data
	 * @return
	 */
	public int nextInt() {
		if (mDataList.size() == 0) {
			reset();
		}

		int idx = CJCRandomUtil.getRandomWithIntegerDistribution(mRuntimeDistribution);
		mRuntimeDistribution.remove(idx);
		return mDataList.remove(idx);
	}
}
