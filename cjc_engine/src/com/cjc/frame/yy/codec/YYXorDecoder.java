package com.cjc.frame.yy.codec;

import org.springframework.stereotype.Component;

import com.cjc.utils.codec.CJCXorUtil;

/**
 * @author cjc
 * @date Apr 26, 2019
*/
@Component
public class YYXorDecoder extends YYDecoder {

	@Override
	public void decode(byte[] bytes) {
		CJCXorUtil.xor(bytes);
	}
}
