package com.tstd2.soa.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yancey
 * @date 2020/2/19 16:47
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Service {

    String interfaceName() default "";

    String protocol() default "";

    int timeout() default 0;

    String value() default "";
}
