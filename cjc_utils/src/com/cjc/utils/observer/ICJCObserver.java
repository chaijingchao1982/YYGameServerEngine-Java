package com.cjc.utils.observer;

/**
 * @Description: 观察者
 * @author CJC
 * @date 2014-8-30
 */
public interface ICJCObserver {

	/**
	 * 初始化观察者
	 */
	public void obInit();

	/**
	 * 释放观察者
	 */
	public void obRelease();

	/**
	 * 观察者收到通知
	 * 
	 * @param sender
	 * @param msgCode
	 * @param data
	 */
	public void obNotify(Object sender, int msgCode, Object... data);
}