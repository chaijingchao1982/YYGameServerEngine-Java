package com.cjc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author chaijingchao
 * @date 2017-10
 */
public class CJCDateUtil {

	/**
	 * 获取当前时间（YMDHMS格式）
	 * @return
	 */
	public static String getNowString() {
		return getYearMonthDayHourMinSecString(System.currentTimeMillis());
	}

	/**
	 * 获取指定（格林尼治毫秒）时间的字符串
	 * @param ms
	 * @return
	 */
	public static String getYearMonthDayHourMinSecString(long ms) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));//时区定义并进行时间获取
		return formatter.format(new Date(ms));
	}

	/**
	 * 获取当前时间（时分秒）
	 * @param ms
	 * @return
	 */
	public static String getHourMinSecString(long ms) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		//formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		return formatter.format(ms);
	}

	/**
	 * 获取自当前时间指定min分钟后的时间
	 * @param minute
	 * @return
	 */
	public static Calendar getMinuteAfter(int minute) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar;
	}

	/**
	 * 获取两个日期的间隔秒数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getIntervalSecond(Date date1, Date date2) {
		return getIntervalSecond(date1.getTime(), date2.getTime());
	}

	/**
	 * 获取两个日期的间隔秒数
	 * @param msec1
	 * @param msec2
	 * @return
	 */
	public static long getIntervalSecond(long msec1, long msec2) {
		long ks = Math.abs(msec2 - msec1);
		long s = (ks / ((long) 1000));
		return s;
	}

	public static float second2Day(long sec) {
		long min = sec / 60;
		long hour = min / 60;
		float day = hour / 24;
		return day;
	}

	public static boolean isExpire(Date srcDate, int dayCount) {
		long sec = getIntervalSecond(srcDate, new Date());
		float day = second2Day(sec);
		return (day > dayCount);
	}
}
