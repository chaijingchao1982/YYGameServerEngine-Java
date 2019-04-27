package com.cjc.utils.random;

/**
 * @Description: 严格分布随机数
 * @author cjc
 * @date Mar 6, 2019
*/
public class CJCSeriousDistributionRandom {

	private int mMin;

	private int mMax;

	/** 分布数组 */
	private int[] mDistribution;
	/** 分布概率综合 */
	private int mTotal;

	/** 严格随机数 */
	private CJCSeriousRandomByList mSeriousRandom;

	/**
	 * 创建
	 * @param min
	 * @param max
	 * @param distribution
	 * @return
	 * @throws Exception
	 */
	public static CJCSeriousDistributionRandom create(int min, int max, int[] distribution) throws Exception {
		min = Math.min(min, max);
		max = Math.max(min, max);
		if (max - min + 1 != distribution.length) {
			throw new Exception("err distribution.length");
		}

		//count total
		int total = 0;
		for (int i = 0; i < distribution.length; i++) {
			if (distribution[i] < 0) {
				throw new Exception("err distribution <0");
			}
			total += distribution[i];
		}

		return new CJCSeriousDistributionRandom(min, max, distribution, total);
	}

	private CJCSeriousDistributionRandom(int min, int max, int[] distribution, int total) {
		mMin = min;
		mMax = max;
		mTotal = total;
		mDistribution = distribution;
		mSeriousRandom = CJCSeriousRandomByList.createWithLenth(mTotal + 1);
	}

	public void reset() {
		mSeriousRandom.reset();
	}

	public int nextInt() {
		final int random = mSeriousRandom.nextInt();
		int tempTotal = 0;
		for (int i = 0; i < mDistribution.length; i++) {
			tempTotal += mDistribution[i];
			if (random <= tempTotal) {
				return mMin + i;
			}
		}
		return mMax;
	}
}
