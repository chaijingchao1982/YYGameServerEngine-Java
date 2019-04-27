package com.thirdparty;

/**
 * 根据两个位置的经纬度，来计算两地的距离（单位为KM） 参数为double类型 long1 位置1经度 lat1 位置1纬度 long2 位置2经度lat2
 * 位置2纬度
 */
public class MyDistanceUtil {

	/** 地球半径 */
	private static final double EARTH_RADIUS = 6378.137;

	/**
	 * 弧度
	 * 
	 * @param d
	 * @return
	 */
	private static double radian(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 获取距离（去掉小数部分）
	 * 
	 * @param distance
	 * @return
	 */
	public static String getDistanceStrNoFractional(double distance) {
		String distanceStr = String.valueOf(distance);
		distanceStr = distanceStr.substring(0, distanceStr.indexOf("."));
		return distanceStr;
	}

	/**
	 * 获取字符串类型距离（去掉小数部分）
	 * 
	 * @param longitude1
	 *            经度
	 * @param latitude1
	 *            纬度
	 * @param longitude2
	 *            经度
	 * @param latitude2
	 *            纬度
	 * @return
	 */
	public static String getDistanceStr(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
		if ((latitude1 == 0 && longitude1 == 0) || (latitude2 == 0 && longitude2 == 0)) {
			return "";
		}
		double distance = getDistance(longitude1, latitude1, longitude2, latitude2);
		return getDistanceStrNoFractional(distance);
	}

	/**
	 * 获取距离
	 * 
	 * @param longitude1
	 * @param latitude1
	 * @param longitude2
	 * @param latitude2
	 * @return
	 */
	public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
		double a, b, d, sa2, sb2;
		latitude1 = radian(latitude1);
		latitude2 = radian(latitude2);
		a = latitude1 - latitude2;
		b = radian(longitude1 - longitude2);

		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(latitude1) * Math.cos(latitude2) * sb2 * sb2));
		d *= 1000;
		return d;
	}

}