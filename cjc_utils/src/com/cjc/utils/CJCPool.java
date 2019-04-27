package com.cjc.utils;

import java.util.Vector;

/**
 * 对象池（线程安全）
 * @author chaijingchao
 * @param <T>
 */
public abstract class CJCPool<T> {

	protected Vector<T> mElements = new Vector<T>();

	public CJCPool() {
		init();
	}

	public T get() {
		if (mElements.size() == 0) {
			return create();
		}
		return mElements.remove(0);
	}

	public boolean put(T e) {
		return mElements.add(e);
	}

	public abstract void init();

	public abstract T create();
}
