package com.jiyx.test.springMVC.bindParam;

import com.jiyx.test.common.util.AnnotationUtils;
import com.jiyx.test.common.util.ClassUtils;
import com.jiyx.test.springMVC.annotation.MyModelAttribute;
import com.jiyx.test.springMVC.annotation.MyRequestBody;
import com.jiyx.test.springMVC.annotation.MyRequstParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * auther: jiyx
 * date: 2018/11/29.
 */
public class Binding {

    /**
     * 解析入参，返回入参参数列表
     * @param method
     * @param request
     * @return
     */
    public static List<Object> bindingMethodParamters(Method method, HttpServletRequest request) {
        List<Object> paramterValues = new ArrayList<>(method.getParameters().length);
        Arrays.stream(method.getParameters())
                .forEach(parameter -> {
                    paramterValues.add(bindingParamter(parameter, request));
                });
        return paramterValues;
    }

    private static Object bindingParamter(Parameter parameter, HttpServletRequest request) {
        if (!AnnotationUtils.isEmpty(parameter.getAnnotation(MyRequstParam.class))) {
            return BindingRequestParamter.bindRequestParamter(parameter, request);
        } else if (!AnnotationUtils.isEmpty(parameter.getAnnotation(MyRequestBody.class))) {
            return BindingJsonParamter.bindJsonParamter(parameter, request);
        } else if (!AnnotationUtils.isEmpty(parameter.getAnnotation(MyModelAttribute.class))) {
            return BindingAttributeParamter.bindAttributeParamter(parameter, request);
        } else if (parameter.getAnnotations() == null || parameter.getAnnotations().length < 1) {
            boolean isBasicType = ClassUtils.isBasicType(parameter.getType().getSimpleName());
            if (isBasicType) {
                return BindingRequestParamter.bindRequestParamter(parameter, request);
            } else {
                return BindingPackgeParamter.bindPackgeParamter(parameter, request);
            }
        }
        return null;
    }
}
