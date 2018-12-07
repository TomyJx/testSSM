package com.jiyx.test.common.util;

/**
 *
 * auther: jiyx
 * date: 2018/11/22.
 */
public class ConvertUtils {

    /**
     * 根据传入的属性和类名，将属性名强转为类名的属性
     * @param className
     * @param parameter
     * @return
     */
    public static Object convert(String className, String parameter) {
        switch (className) {
            case "String" :
                return parameter;
            case "Integer":
                return Integer.valueOf(parameter);
            case "int":
                return Integer.valueOf(parameter);
            case "Long":
                return Long.valueOf(parameter);
            case "long":
                return Long.valueOf(parameter);
            case "Double":
                return Double.valueOf(parameter);
            case "double":
                return Double.valueOf(parameter);
            case "Float":
                return Float.valueOf(parameter);
            case "float":
                return Float.valueOf(parameter);
            case "Short":
                return Short.valueOf(parameter);
            case "short":
                return Short.valueOf(parameter);
            case "Byte":
                return Byte.valueOf(parameter);
            case "byte":
                return Byte.valueOf(parameter);
            case "Boolean":
                return Boolean.valueOf(parameter);
            case "boolean":
                return Boolean.valueOf(parameter);
            default:
                return null;
        }
    }

}
