package com.jiyx.test.springMVC.bindParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

/**
 * auther: jiyx
 * date: 2018/11/29.
 */
public interface BindingParamter {
    Object bindParamter(Parameter parameter, HttpServletRequest request);
}
