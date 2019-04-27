package com.cjc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author cjc
 * @date Nov 19, 2018
*/
public class CJCExceptionUtil {

	/** 栈顶级数 */
	private static final int STACK_COUNT_TOP = 10;
	/** 栈底级数 */
	private static final int STACK_COUNT_TAIL = 10;
	/** 打印栈最大级数 */
	private static final int STACK_COUNT_MAX = (STACK_COUNT_TOP + STACK_COUNT_TAIL);

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static void log(Logger log, Exception e) {
		log.error("exception", e);
	}

	/**
	 * 异常打印(暂时弃用)
	 * @param log
	 * @param e
	 */
	@SuppressWarnings("unused")
	private static void log1(Logger log, Exception e) {
		//方式3:打印一级调用栈（用log4j,好处是能用级别过滤，还能打印时间）
		//!!!上线时要用这个!!!
		if (log == null) {
			log = sLog;
		}

		final StringBuffer sb = new StringBuffer();
		sb.append(e.toString());
		sb.append("\n\t");

		//stack top or whole
		final int count = (e.getStackTrace().length > STACK_COUNT_MAX ? STACK_COUNT_TOP : e.getStackTrace().length);
		for (int i = 0; i < count; i++) {
			StackTraceElement element = e.getStackTrace()[i];
			addElement2Buffer(element, sb);
		}

		//stack tail
		if (e.getStackTrace().length > STACK_COUNT_MAX) {
			sb.append("...");
			sb.append("\n\t");

			for (int i = STACK_COUNT_TAIL; i > 0; i--) {
				StackTraceElement element = e.getStackTrace()[e.getStackTrace().length - i];
				addElement2Buffer(element, sb);
			}

			sb.append("stack.length=");
			sb.append(e.getStackTrace().length);
			sb.append("\n\t");
		}

		log.error(sb);
	}

	/**
	 * 把异常信息添加到字符串中去
	 * @param element
	 * @param sb
	 */
	private static void addElement2Buffer(StackTraceElement element, StringBuffer sb) {
		sb.append(element.getClassName());
		sb.append(".");
		sb.append(element.getMethodName());
		sb.append(" ");
		sb.append(element.getLineNumber());
		sb.append("\n\t");
	}

	/**
	 * (暂时弃用)
	 * @param log
	 * @param e
	 */
	@SuppressWarnings("unused")
	private static void log2(Logger log, Exception e) {
		//方式1:打印全部调用栈
		//e.printStackTrace();

		//方式2:打印一级调用栈
		//System.out.println("\n" + e.toString() + "\n" + e.getStackTrace()[0].toString());
	}
}
