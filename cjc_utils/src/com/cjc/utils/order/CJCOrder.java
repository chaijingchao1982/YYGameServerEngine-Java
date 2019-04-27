package com.cjc.utils.order;

/**
 * 排序器
 * @author chaijingchao
 * @date 2017-10
 */
public abstract class CJCOrder<T> {

	public T[] order(T src[]) {
		for (int i = 0; i < src.length - 1; i++) {
			for (int j = i + 1; j < src.length; j++) {
				if (compare(src[i], src[j])) {
					T temp = src[i];
					src[i] = src[j];
					src[j] = temp;
				}
			}
		}
		return src;
	}

	public abstract boolean compare(T a, T b);
}
