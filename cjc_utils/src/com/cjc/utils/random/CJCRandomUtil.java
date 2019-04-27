package com.cjc.utils.random;

import java.util.List;
import java.util.Random;

/**
 * @author chaijingchao
 * @date 2017-12
 */
public class CJCRandomUtil {

	/** 随机数种子，内部算法使用 */
	private static Random sRandom = new Random();

	/**
	 * 生成max到min范围的浮点数
	 * @param min
	 * @param max
	 * @return
	 */
	public static float nextFloat(final int min, final int max) {
		return (min + ((max - min) * sRandom.nextFloat()));
	}

	/**
	 * ab闭区间
	 * @param a
	 * @param b
	 * @return
	 */
	public static int getRandom(int a, int b) {
		if (a == b) {
			return a;
		}

		int max = Math.max(a, b);
		int min = Math.min(a, b);
		int ret = sRandom.nextInt(max - min + 1);
		return (min + ret);
	}

	public static int getRandomNaturalNumber() {
		return Math.abs(sRandom.nextInt());
	}

	/**
	 * 根据值概率分布随机获取一个值
	 * @param distribution
	 * @return
	 */
	public static int getRandomWithDistribution(int[] distribution) {
		if (distribution == null || distribution.length == 0) { // 参数错误
			return -1;
		}

		// 先计算全概率
		int total = 0;
		for (int i = 0; i < distribution.length; i++) {
			if (distribution[i] < 0) { // 参数错误
				return -1;
			}
			total += distribution[i];
		}

		final int r = Math.abs(new Random().nextInt()) % total;
		int add = 0;
		for (int i = 0; i < distribution.length; i++) {
			add += distribution[i];
			if (r <= add) {
				return i;
			}
		}

		// 错误
		return -1;
	}

	/**
	 * 这种实现误差大
	 * @param distribution
	 * @return
	 */
	@SuppressWarnings("unused")
	private static int getRandomWithDistributionError(List<Integer> distribution) {
		if (distribution == null || distribution.size() == 0) { // 参数错误
			return -1;
		}

		// 先计算全概率
		int total = 0;
		for (Integer temp : distribution) {
			if (temp < 0) { // 参数错误
				return -1;
			}
			total += temp;
		}

		final int r = new Random().nextInt(total);
		// final int r = Math.abs(new Random().nextInt()) % total;
		int add = 0;
		for (int i = 0; i < distribution.size(); i++) {
			add += distribution.get(i);
			if (r <= add) {
				return i;
			}
		}

		// 错误
		return -1;
	}

	/**
	 * 根据概率分布获取随机数(Integer版)
	 * 
	 * @param distribution
	 * @return
	 */
	public static int getRandomWithIntegerDistribution(List<Integer> distribution) {
		return doGetRandomWithDistribution(distribution, true);
	}

	/**
	 * 根据概率分布获取随机数(Float版)
	 * 
	 * @param distribution
	 * @return
	 */
	public static int getRandomWithFloatDistribution(List<Float> distribution) {
		return doGetRandomWithDistribution(distribution, false);
	}

	/**
	 * 根据概率分布获取随机数(具体实现)
	 * 
	 * @param distribution
	 * @return
	 */
	private static int doGetRandomWithDistribution(List<?> distribution, boolean isInteger) {
		if (distribution == null || distribution.size() == 0) { // 参数错误
			return -1;
		}
		if (distribution.size() == 1) { // 只有一个
			return 0;
		}

		// 先计算全概率
		float total = 0;
		for (int i = 0; i < distribution.size(); i++) {
			float temp = 0;
			if (isInteger) {
				temp = (int) distribution.get(i);
			} else {
				temp = (float) distribution.get(i);
			}

			if (temp < 0) { // 参数错误
				return -1;
			}
			total += temp;
		}

		// 重新计算概率
		float[] probability = new float[distribution.size()];
		float temp = 0;
		for (int i = 0; i < distribution.size(); i++) {
			float d = 0;
			if (isInteger) {
				d = (int) distribution.get(i);
			} else {
				d = (float) distribution.get(i);
			}
			d /= total;
			if (i == distribution.size() - 1) { // 最后一个凑足1
				probability[i] = 1 - temp;
			} else {
				probability[i] = d;
				temp += d;
			}
		}

		// 随机，并按照概率分布查找结果
		double r = Math.random();
		double add = 0;
		for (int i = 0; i < probability.length; i++) {
			add += probability[i];
			if (r <= add) {
				return i;
			}
		}

		// 出现这种情况是除法误差导致
		return (distribution.size() - 1);
	}
}
