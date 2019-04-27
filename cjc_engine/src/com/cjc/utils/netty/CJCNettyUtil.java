package com.cjc.utils.netty;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author chaijingchao
 * @date 2018-8
 */
public class CJCNettyUtil {

	public static String getIp(ChannelHandlerContext chc) {
		InetSocketAddress isa = (InetSocketAddress) chc.channel().remoteAddress();
		return isa.getAddress().getHostAddress();
	}
}
