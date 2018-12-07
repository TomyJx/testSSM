package com.jiyx.test.springMVC.bindParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * auther: jiyx
 * date: 2018/11/29.
 */
public class BindingPackgeParamter implements BindingParamter{

    private static BindingPackgeParamter instance = new BindingPackgeParamter();

    private BindingPackgeParamter(){}


    @Override
    public Object bindParamter(Parameter parameter, HttpServletRequest request) {
        return null;
    }

    public static Object bindPackgeParamter(Parameter parameter, HttpServletRequest request) {
        return instance.bindParamter(parameter, request);
    }
}
