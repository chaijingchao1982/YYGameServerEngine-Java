package com.cjc.frame.rpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description: 远程调用的注解
 * @author chaijingchao
 * @date 2018-12
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CJCRpc {

	String value() default "";
}
