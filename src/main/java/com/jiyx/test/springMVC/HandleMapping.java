package com.jiyx.test.springMVC;

import com.jiyx.test.common.util.AnnotationUtils;
import com.jiyx.test.spring.annotation.MyController;
import com.jiyx.test.springMVC.annotation.MyRequestMapping;
import com.jiyx.test.springMVC.annotation.RequestMethod;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理器映射器
 * auther: jiyx
 * date: 2018/11/28.
 */
@Slf4j
public class HandleMapping {

    private static Map<Method, RequestMethod> methodRequestMethodMap = new ConcurrentHashMap<>();

    /**
     * 初始化处理器映射器
     * @param beanContainerMap
     */
    public static Map<String, Method> initHandleMapping(Map<String, Object> beanContainerMap) {
        Map<String, Method> handleMapping = new ConcurrentHashMap<>();
        beanContainerMap.entrySet().stream()
                .filter(entrty -> hasMyControllerAnnotation(entrty.getKey()))
                .forEach(entrty -> putHandleMapping(entrty.getKey(), handleMapping));
        return handleMapping;
    }

    private static void putHandleMapping(String className, Map<String, Method> handleMapping) {
        try {
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getDeclaredMethods();
            Arrays.stream(methods)
                    .filter(HandleMapping::methodHasMyRequestMapping)
                    .forEach(method -> {
                        StringBuilder path = new StringBuilder();

                        // 添加类上的父目录
                        path.append(getParentPath(className));

                        path.append(getMethodAnnotationValue(method, MyRequestMapping.class));

                        putMethodRequest(method);

                        log.info("mapping {} init", path.toString());
                        Method previous = handleMapping.putIfAbsent(path.toString(), method);

                        if (previous != null) {
                            throw new RuntimeException("存在一个路径于多个方法映射");
                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void putMethodRequest(Method method) {
        MyRequestMapping methodAnnotation = method.getAnnotation(MyRequestMapping.class);
        if (!AnnotationUtils.isEmpty(methodAnnotation)) {
            methodRequestMethodMap.put(method, methodAnnotation.method());
        }
    }

    private static String getMethodAnnotationValue(Method method, Class annotation) {
        MyRequestMapping methodAnnotation = method.getAnnotation(MyRequestMapping.class);
        if (!AnnotationUtils.isEmpty(methodAnnotation)) {
            return methodAnnotation.value();
        }
        return "";
    }

    private static boolean methodHasMyRequestMapping(Method method) {
        return !AnnotationUtils.isEmpty(method.getAnnotation(MyRequestMapping.class));
    }

    private static String getParentPath(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            MyRequestMapping annotation = clazz.getAnnotation(MyRequestMapping.class);
            if (!AnnotationUtils.isEmpty(annotation)) {
                return annotation.value();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean hasMyControllerAnnotation(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (!AnnotationUtils.isEmpty(clazz.getAnnotation(MyController.class))) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Map<Method, RequestMethod> getMethodRequestMethodMap() {
        return methodRequestMethodMap;
    }
}
