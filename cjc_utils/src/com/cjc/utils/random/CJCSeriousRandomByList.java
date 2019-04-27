package com.cjc.utils.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * serious random data
 * 
 * make the random data never repeat in a cycle of (min, max)
 * 
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCSeriousRandomByList {

	/** random */
	private static Random sRandom = new Random();

	/** min data */
	private int mMin;

	/** max data */
	private int mMax;

	/** data list */
	private List<Integer> mDataList = new ArrayList<Integer>(100);

	/**
	 * create SeriousRandom with (0, max-1)
	 * 
	 * @param max
	 * @return
	 */
	public static CJCSeriousRandomByList createWithLenth(int max) {
		return CJCSeriousRandomByList.create(0, max - 1);
	}

	/**
	 * create SeriousRandom with (min, max)
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static CJCSeriousRandomByList create(int min, int max) {
		if (min >= max) {
			return null;
		}
		return new CJCSeriousRandomByList(min, max);
	}

	/**
	 * construction
	 * 
	 * @param min
	 * @param max
	 */
	private CJCSeriousRandomByList(int min, int max) {
		this.mMin = min;
		this.mMax = max;
		reset();
	}

	/**
	 * reset all
	 */
	public void reset() {
		mDataList.clear();
		for (int i = mMin; i <= mMax; i++) {
			mDataList.add(i);
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
		int idx = sRandom.nextInt(mDataList.size());
		return mDataList.remove(idx);
	}
}
