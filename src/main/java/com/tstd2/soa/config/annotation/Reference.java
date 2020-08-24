package com.tstd2.soa.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yancey
 * @date 2020/2/19 21:37
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Reference {
    String loadbalance() default "";

    String protocol() default "";

    String cluster() default "";

    String retries() default "";

    int timeout() default 0;

    String proxy() default "";

    String value() default "";
}
