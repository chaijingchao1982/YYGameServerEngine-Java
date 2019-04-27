package com.cjc.frame.yy.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description: msg annotation
 * @author chaijingchao
 * @date 2018-12
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface YYHandlerAnnotation {

	public int code() default -1;

}
