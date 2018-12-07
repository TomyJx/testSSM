package com.jiyx.test.common.util;

/**
 * 方法工具类
 * auther: jiyx
 * date: 2018/11/22.
 */
public class MethodUtils {

    static {
        System.out.println("MethodUtils : oust");
    }

    /**
     * 根据属性名拼接set方法,属性名准守驼峰命名
     * @param filedName
     * @return
     */
    public static String getAttrSetMethodName(String filedName) {
        return "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);
    }

}
