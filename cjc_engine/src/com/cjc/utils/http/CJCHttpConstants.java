package com.cjc.utils.http;

/**
 * http常量
 * 
 * @author chaijingchao
 * @date 2017-12
 */
public class CJCHttpConstants {

	/** resp属性-二进制 */
	public static final String OCTET_STREAM = "application/octet-stream";
	/** resp属性-文本 */
	public static final String TEXT_PLAIN = "text/plain";

	/** base64头 */
	public static final String BASE64_HEADER = "data:image/png;base64,";

	public static final String POST = "POST";

	public static final String CONTENT_TYPE = "Content-Type";

	public static final String ACCEPT_CHARSET = "Accept-Charset";

	public static final String CONNECTION = "Connection";

	public static final String USER_AGENT = "User-Agent";

	public static final String ACCEPT = "Accept";

	public static final String ORIGIN = "Origin";

	/** value举例: http://127.0.0.1:5483/index.html */
	public static final String REFERER = "Referer";

	/** value举例: gzip, deflate */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	/** value举例: zh-CN */
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
}
