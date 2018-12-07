package com.jiyx.test.spring.annotation;

import java.lang.annotation.*;

/**
 * auther: jiyx
 * date: 2018/11/25.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyRepository {
}
