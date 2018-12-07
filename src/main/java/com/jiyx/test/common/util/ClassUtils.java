package com.jiyx.test.common.util;

/**
 * 类的工具类
 * auther: jiyx
 * date: 2018/11/22.
 */
public class ClassUtils {

    /**
     * 判断是否为基本类型
     * @param typeName
     * @return
     */
    public static boolean isBasicType(String typeName) {
        switch (typeName) {
            case "String" :
                return true;
            case "Integer":
                return true;
            case "Long":
                return true;
            case "Double":
                return true;
            case "Float":
                return true;
            case "Short":
                return true;
            case "Byte":
                return true;
            case "Boolean":
                return true;
            default:
                return false;
        }
    }

    public static Object castToBasicType(String className, String value) {
        if ("String".equals(className)) {
            return value;
        } else if ("Integer".equals(className)) {
            return Integer.valueOf(value);
        } else if ("int".equals(className)) {
            return Integer.valueOf(value);
        } else if ("Long".equals(className)) {
            return Long.valueOf(value);
        } else if ("long".equals(className)) {
            return Long.valueOf(value);
        } else if ("Double".equals(className)) {
            return Double.valueOf(value);
        } else if ("double".equals(className)) {
            return Double.valueOf(value);
        } else if ("Float".equals(className)) {
            return Float.valueOf(value);
        } else if ("float".equals(className)) {
            return Float.valueOf(value);
        } else if ("Short".equals(className)) {
            return Short.valueOf(value);
        } else if ("short".equals(className)) {
            return Short.valueOf(value);
        } else {
            throw new RuntimeException(String.format("%s不是基本属性", className));
        }
    }
}
