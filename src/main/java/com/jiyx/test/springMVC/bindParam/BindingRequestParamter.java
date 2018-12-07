package com.jiyx.test.springMVC.bindParam;

import com.jiyx.test.common.util.ClassUtils;
import com.jiyx.test.common.util.ConvertUtils;
import com.jiyx.test.springMVC.annotation.MyRequstParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter; /**
 * auther: jiyx
 * date: 2018/11/29.
 */
public class BindingRequestParamter implements BindingParamter {

    private static BindingRequestParamter instance = new BindingRequestParamter();

    private BindingRequestParamter() {
    }

    @Override
    public Object bindParamter(Parameter parameter, HttpServletRequest request) {
        MyRequstParam annotation = parameter.getAnnotation(MyRequstParam.class);
        String paramName = annotation.value();
        String requestParameter = request.getParameter(paramName);
        String simpleName = parameter.getType().getSimpleName();

        if (ClassUtils.isBasicType(simpleName)) {
            return ConvertUtils.convert(simpleName, requestParameter);
        }

        throw new RuntimeException("不支持非基本属性的赋值");
    }

    public static Object bindRequestParamter(Parameter parameter, HttpServletRequest request) {
        return instance.bindParamter(parameter, request);
    }
}
