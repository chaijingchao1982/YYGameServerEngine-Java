package com.cjc.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCDbConnectUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * 获取数据库链接（数据库连接数限制，最大打开文件数：windows=1024,linux=65536)）
	 * @param dbUrl
	 * @param name
	 * @param psw
	 * @return
	 */
	public static Connection getConnection(String dbUrl, String name, String psw) {
		Connection con = null;
		try {
			Class.forName(CJCSqlConstants.JDBC_DRIVER);
			con = DriverManager.getConnection(dbUrl, name, psw); // 获取数据库连接
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return con;
	}

	public static void closeConnection(Connection con) {
		if (con == null) {
			return;
		}

		try {
			con.close();
		} catch (SQLException e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}
}
