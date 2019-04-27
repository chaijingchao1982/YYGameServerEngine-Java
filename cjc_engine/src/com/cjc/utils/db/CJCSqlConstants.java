package com.cjc.utils.db;

/**
 * @author cjc
 * @date Nov 15, 2018
*/
public class CJCSqlConstants {

	/** 数据库驱动 */
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	/** update限制单条修改，防止出错改全表 */
	public static final String LIMIT_1 = " limit 1";

	/** 如果不存在就创建表 */
	public static final String IF_NOT_EXISTS = " if not exists ";

	/** 如果主键重复就变update */
	public static final String ON_DUPLICATE_KEY_UPDATE = " ON DUPLICATE KEY UPDATE ";
}
