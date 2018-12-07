package com.jiyx.test.springMVC.bindParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * auther: jiyx
 * date: 2018/11/29.
 */
public class BindingJsonParamter implements BindingParamter{

    private static BindingJsonParamter instance = new BindingJsonParamter();

    private BindingJsonParamter(){}

    @Override
    public Object bindParamter(Parameter parameter, HttpServletRequest request) {
        return null;
    }

    public static Object bindJsonParamter(Parameter parameter, HttpServletRequest request) {
        return instance.bindParamter(parameter, request);
    }
}
