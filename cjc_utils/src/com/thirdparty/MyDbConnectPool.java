package com.thirdparty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * 数据库连接池（这个类未被使用）
 * @author 互联网
 * @date 2017-9
 */
public class MyDbConnectPool {

	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/** 数据库url */
	private String mDBUrl;

	/** 数据库用户名 */
	private String mName;

	/** 数据库用户密码 */
	private String mPassword;

	/** 设置连接池属性 */
	private int mInitSize = 10;

	@SuppressWarnings("unused")
	private int mMaxSize = 40;

	/** 用LinkedList对象来保存connection对象 */
	private LinkedList<Connection> connList = new LinkedList<Connection>();

	/** 声明一个临时变量来计算连接对象的数量 */
	private int mCreateConnCount = 0;

	/**
	 * 声明YYJDBCPool对象时自动注册驱动
	 */
	static {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String args[]) {
		// 获得连接池
		MyDbConnectPool mypool = new MyDbConnectPool("jdbc:mysql://localhost:3306/yy_ddz", "root", "root");

		// 从连接池中尝试获取9个连接
		for (int i = 0; i < 9; i++) {
			Connection conn = mypool.getConnectionFromPool();
			System.out.println(conn.toString());
		}

		// 获取第五个连接后，释放一下，然后再获取
		for (int i = 0; i < 9; i++) {
			Connection conn = mypool.getConnectionFromPool();
			if (i == 5) {
				mypool.releaseConnection(conn);
			}
			System.out.println(conn.toString());
		}
	}

	/**
	 * 构造方法，初始化连接池，并往里面添加连接对象
	 * @param dbUrl
	 * @param name
	 * @param psw
	 */
	public MyDbConnectPool(String dbUrl, String name, String psw) {
		mDBUrl = dbUrl;
		mName = name;
		mPassword = psw;

		for (int i = 0; i < mInitSize; i++) {
			Connection connection = this.getConnection();
			connList.add(connection);
			mCreateConnCount++;
		}
	}

	/**
	 * 获取连接的方法
	 * @return
	 */
	private Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(mDBUrl, mName, mPassword);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return conn;
	}

	/**
	 * 获取连接池中的一个连接对象
	 * @return
	 */
	public Connection getConnectionFromPool() {
		// 当连接池还没空
		if (connList.size() > 0) {
			Connection connection = connList.getFirst();
			connList.removeFirst();
			return connection;
		} else if (connList.size() == 0 && mCreateConnCount < 8) {
			// 连接池被拿空，且连接数没有达到上限，创建新的连接
			mCreateConnCount++;
			connList.addLast(this.getConnection());

			Connection connection = connList.getFirst();
			connList.removeFirst();
			return connection;
		}

		throw new RuntimeException("连接数达到上限，请等待");
	}

	/**
	 * 把用完的连接放回连接池
	 * @param connection
	 */
	public void releaseConnection(Connection connection) {
		connList.addLast(connection);
	}
}
