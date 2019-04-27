package com.cjc.frame.yy.msg;

import com.google.protobuf.GeneratedMessageV3;

/**
 * @author chaijingchao
 * @date 2017-9
 */
public interface IYYErrMsgService {

	/**
	 * 创建错误消息
	 * @param errCode
	 * @return
	 */
	public GeneratedMessageV3 create(int errCode);

}
