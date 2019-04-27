package com.cjc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author chaijingchao
 * @date 2018-1
 */
public class CJCRestartUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/** 重启计数 */
	private static int mRestartCount = 0;

	/** 等待mMillisecond毫秒，保证分身启动完成后，再关掉自己 */
	private int mWaitMsec = 0;

	/** 重启运行的命令 */
	private String mCmd;

	public CJCRestartUtil(int mSec, String cmd) {
		this.mWaitMsec = mSec;
		this.mCmd = cmd;
	}

	public void restart() {
		sLog.info("restart1");
		mRestartCount++;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					Runtime.getRuntime().exec(mCmd);
					Thread.sleep(mWaitMsec);
					sLog.info("restart success");
				} catch (Exception e) {
					CJCExceptionUtil.log(sLog, e);
				}
			}
		});

		sLog.info("restart2");
		System.exit(0);
		sLog.info("restart3");
	}

	public int getRestartCount() {
		return mRestartCount;
	}
}
