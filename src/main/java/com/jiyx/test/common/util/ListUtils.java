package com.jiyx.test.common.util;

import java.util.List;

/**
 * 链表工具类
 * auther: jiyx
 * date: 2018/11/22.
 */
public class ListUtils {

    /**
     * 看原文就是想在给list添加元素时，不能添加重复的，但是为什么不直接使用set进行存储呢
     * @param list
     * @param t
     * @param <T>
     */
    public static <T> void add(List<T> list, T t) {
        if (!list.contains(t)) {
            list.add(t);
        }
    }

}
