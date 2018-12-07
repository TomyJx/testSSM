package com.jiyx.test.springMVC.bindParam;

import com.jiyx.test.common.util.AnnotationUtils;
import com.jiyx.test.common.util.ConvertUtils;
import com.jiyx.test.springMVC.annotation.MyModelAttribute;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * 实体类绑定
 * auther: jiyx
 * date: 2018/11/29.
 */
public class BindingAttributeParamter implements BindingParamter{

    private static BindingAttributeParamter instance = new BindingAttributeParamter();

    private BindingAttributeParamter(){}

    @Override
    public Object bindParamter(Parameter parameter, HttpServletRequest request) {
        MyModelAttribute annotation = parameter.getAnnotation(MyModelAttribute.class);
        Class<?> parameterType = null;
        if (!AnnotationUtils.isEmpty(annotation)) {
            parameterType = parameter.getType();
            if (!parameterType.getSimpleName().equals(annotation.value())) {
                throw new RuntimeException("实体类绑定异常，请重新检查");
            }
        }

        Field[] fields = parameterType.getDeclaredFields();
        try {
            Object object = parameterType.newInstance();
            Arrays.stream(fields).forEach((Field field) -> {
                String requestParameter = request.getParameter(field.getName());
                try {
                    // 这里的实体类的属性只能是基本类型
                    if (StringUtils.isNotBlank(requestParameter)) {
                        field.set(object, ConvertUtils.convert(field.getType().getSimpleName(), requestParameter));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            return object;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object bindAttributeParamter(Parameter parameter, HttpServletRequest request) {
        return instance.bindParamter(parameter, request);
    }
}
