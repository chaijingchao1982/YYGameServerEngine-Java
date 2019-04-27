package com.cjc.frame.yy;

/**
 * @author chaijingchao
 * @date 2018-1
 */
public class YYConfig {

	/** msg.xml配置文件路径 */
	public static final String MSG_XML_PATH = "./res/msg.xml";

	public enum ServerType {
		TCP, WEB_SOCKET,
	};

	private static ServerType sServerType = ServerType.WEB_SOCKET;

	/**
	 * 初始化
	 * 服务器类型：至少在YYHandlerRouteService创建以前设置，才有效
	 * @param serverType
	 */
	public static void init(YYConfig.ServerType serverType) {
		sServerType = serverType;
	}

	public static ServerType getServerType() {
		return sServerType;
	}

}
