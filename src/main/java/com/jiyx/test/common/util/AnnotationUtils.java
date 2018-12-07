package com.jiyx.test.common.util;

import java.lang.reflect.AnnotatedType;
import java.util.Arrays;

/**
 * 注解工具类
 * auther: jiyx
 * date: 2018/11/22.
 */
public class AnnotationUtils {

    /**
     * 判断注解是否为空
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T t) {
        return t == null ? true : false;
    }

    /**
     * 判断源class文件有没有配置指定的注解
     * @param annotations
     * @param sourceClass
     * @return
     */
    public static boolean hasAnnotion(Class sourceClass, Class... annotations) {

        if (annotations == null || sourceClass == null) {
            throw new IllegalArgumentException("请输入正确的入参");
        }

        for (Class annotation : annotations) {
            if (sourceClass.getAnnotation(annotation) != null) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断源class文件有没有配置指定的注解
     * @param annotation
     * @param sourceClass
     * @return
     */
    public static boolean hasAnnotion(Class sourceClass, Class annotation) {

        if (annotation == null || sourceClass == null) {
            throw new IllegalArgumentException("请输入正确的入参");
        }

        if (sourceClass.getAnnotation(annotation) != null) {
            return true;
        }

        return false;
    }

}
