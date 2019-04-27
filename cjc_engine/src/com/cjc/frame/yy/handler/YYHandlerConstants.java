package com.cjc.frame.yy.handler;

/**
 * 处理器常量
 * @author chaijingchao
 * @date 2017-12
 */
public class YYHandlerConstants {

	/** 处理成功 */
	public static final int RET_SUCCESS = 0;
	/** 链接已关闭 */
	public static final int RET_CHANNEL_CLOSED = -1;
	/** 消息码错误 */
	public static final int RET_MSG_CODE_ERR = -2;
	/** 未知错误 */
	public static final int RET_UNKNOW_ERR = -3;

	/** 点 */
	public static final String DOT = ".";

	/** XML配置文件中的key */
	public static final String KEY_MSG = "msg";
	public static final String KEY_CODE = "code";

	/** 请求和回复 */
	public static final String REQ = "Req";
	public static final String RESP = "Resp";

	/** 处理器名结尾部分 */
	public static final String HANDLER = "Handler";
}
