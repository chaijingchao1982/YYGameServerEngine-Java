package com.cjc.frame.route;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author chaijingchao
 * @date 2018-9
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CJCHandlerAnnotation {

	String key() default "";

	boolean isCompress() default true;
}
