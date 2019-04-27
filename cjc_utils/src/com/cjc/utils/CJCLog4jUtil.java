package com.cjc.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;

/**
 * @Description: Log4工具
 * @author cjc
 * @date Dec 11, 2018
*/
public class CJCLog4jUtil {

	/**
	 * 设置log4j打印级别
	 * @param level
	 */
	public static void setLevel(Level level) {
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration conf = ctx.getConfiguration();
		conf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(level);
		ctx.updateLoggers(conf);
	}
}
