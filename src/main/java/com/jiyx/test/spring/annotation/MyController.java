package com.jiyx.test.spring.annotation;

import java.lang.annotation.*;

/**
 * auther: jiyx
 * date: 2018/11/24.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyController {
}
