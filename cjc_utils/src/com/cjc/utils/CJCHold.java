package com.cjc.utils;

/**
 * @Description: 双值容器
 * @author cjc
 * @date 2014
 */
public class CJCHold<L, R> {

	private L mLeft;

	private R mRight;

	public CJCHold() {
	}

	public CJCHold(L left, R right) {
		this.mLeft = left;
		this.mRight = right;
	}

	public L getL() {
		return mLeft;
	}

	public void setL(L left) {
		this.mLeft = left;
	}

	public R getR() {
		return mRight;
	}

	public void setR(R right) {
		this.mRight = right;
	}
}