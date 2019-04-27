package com.cjc.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chaijingchao
 * @date 2012
 */
public class CJCDebugUtil {

	/** 采集要观察的对象 */
	private static List<Object> sDebugObjects = new ArrayList<Object>();

	/**
	 * 采集要监测的对象
	 * @param obj
	 */
	public static void addDebugObject(Object obj) {
		if (obj != null) {
			sDebugObjects.add(obj);
		}
	}

	/**
	 * 清空采集的监测对象
	 */
	public static void clearDebugObject() {
		sDebugObjects.clear();
	}
}
