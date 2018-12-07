package com.jiyx.test.springMVC.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * auther: jiyx
 * date: 2018/11/28.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface MyRequestMapping {
    String value() default "";

    RequestMethod method() default RequestMethod.POST;
}
