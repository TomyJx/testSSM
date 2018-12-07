package com.jiyx.test.springMVC.annotation;

import java.lang.annotation.*;

/**
 * auther: jiyx
 * date: 2018/11/29.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyModelAttribute {
    String value() default "";
}
