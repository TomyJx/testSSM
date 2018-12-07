package com.jiyx.test.spring;

import com.jiyx.test.common.util.AnnotationUtils;
import com.jiyx.test.spring.annotation.MyAutowired;
import com.jiyx.test.spring.annotation.MyController;
import com.jiyx.test.spring.annotation.MyRepository;
import com.jiyx.test.spring.annotation.MyService;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * spring扫描包工具类
 * auther: jiyx
 * date: 2018/11/25.
 */
public class ScanUtil {

    /**
     * 排好序的实例化类名
     */
    private static List<String> componentList = new ArrayList<>();
    /**
     * 接口与实现类的对应关系表
     */
    private static Map<String, String> interfaceAndImplMap = new ConcurrentHashMap<>();

    /**
     * 包下所有的类名
     */
    private static List<String> listClassNameList = new ArrayList<>();

    private static final String PROXY = "proxy";


    /**
     * 扫描指定包下的所有的类名
     * @param packageName
     * @return
     */
    public static List<String> getClassNameList(String packageName) {

        Enumeration<URL> urls = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        // TODO 可能出错,因为人家用的是replace
        String newpackageName = packageName.replaceAll("\\.", "\\/");
        try {
            // 通过这种方式获取该包下的文件路径比较简便，用其他的方式应该需要手动拼写路径等情况了
            urls = contextClassLoader.getResources(newpackageName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File packageFile = new File(url.getPath());
                File[] files = packageFile.listFiles();
                if (files == null) {
                    break;
                }
                String currpath = packageName.replaceAll("\\/", "\\.") + ".";
                Arrays.stream(files).filter(file -> file.getName().endsWith(".class")).forEach(file -> listClassNameList.add(currpath + file.getName().substring(0, file.getName().indexOf("."))));
                Arrays.stream(files).filter(file -> !file.getName().endsWith(".class") && file.isDirectory()).forEach(file -> getClassNameList(newpackageName + "." + file.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listClassNameList;
    }

    /**
     * 返回有注解的实例化顺序的链表
     * @param packageName
     * @return
     */
    public static List<String> getComponentList(String packageName) {

        List<String> classNameList = getClassNameList(packageName);
        // 将扫描的接口和其实现类对应上，模仿spring接口注入，复杂的原因是java不支持从接口获取实现类
        makeInterfaceAndImplMap(classNameList);
        for (String className : classNameList) {
            resolveComponent(className);
        }
        return componentList;
    }

    /**
     * 对应接口和其实现类
     * @param classNameList
     */
    private static void makeInterfaceAndImplMap(List<String> classNameList) {

        // 所有的已经存在实现类的接口名
        List<String> interfaceExitsRealizationClass = new ArrayList<>();

        // 所有的接口名
        List<String> interfaceNameList = classNameList.stream().filter(ScanUtil::isInterFace).collect(Collectors.toList());

        classNameList.stream().filter(ScanUtil::isNotInterFace).forEach(className -> {
            try {
                // 当前的实现类
                Class<?> realizationClass = Class.forName(className);
                Class<?>[] interfaces = realizationClass.getInterfaces();

                // 如果实现了接口
                if (interfaces != null && interfaces.length > 0) {
                    Optional<Class<?>> matchInterface = Arrays.stream(interfaces)
                            .filter(currInterface -> isInInterfaceList(currInterface, interfaceNameList))
                            .findFirst();

                    // 实现类的接口类，没有在扫描包下
                    if (matchInterface == null) {
                        throw new RuntimeException("实现类的接口类，没有在扫描包下");
                    }
                    interfaceAndImplMap.put(matchInterface.get().getName(), className);
                    interfaceExitsRealizationClass.add(matchInterface.get().getName());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        //需要动态代理注入的接口，在map中用value = proxy来识别
        interfaceNameList.removeAll(interfaceExitsRealizationClass);

        interfaceNameList.stream().forEach(className -> interfaceAndImplMap.put(className, PROXY));
    }

    /**
     * 生成类的实例化顺序链表
     * @param className
     */
    private static void resolveComponent(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            // 这一块的添加componentList会涉及到一个重复添加的问题
            // 所以最初的想法是只添加ctrl层就可以了
            // 但是后期想了下，如果只添加ctrl层的话，有的Service是需要远程调用的，那么就会有问题了
            // 所以还是需要在添加的时候做一个去重判断就行了
            addNewAnnotation(MyController.class, clazz);
            addNewAnnotation(MyService.class, clazz);
            addNewAnnotation(MyRepository.class, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归添加需要实例化的类，这里没有对死循环做校验，也就是a中有b，b中有a
     * @param annotationClass
     * @param clazz
     * @param <A>
     */
    private static <A extends Annotation> void addNewAnnotation(Class<A> annotationClass, Class<?> clazz) {
        if (!AnnotationUtils.isEmpty(clazz.getAnnotation(annotationClass))) {
            // 有直接的注解的时候
            Field[] declaredFields = clazz.getDeclaredFields();
            if (declaredFields != null && declaredFields.length > 0) {
                if (hasAnyFieldWithAutowired(declaredFields)) {
                    anyFieldsHasMyAutowired(declaredFields, clazz);
                    addComponentList(clazz.getName());
                } else {
                    // 没有一个字段有MyAutowried注解
                    addComponentList(clazz.getName());
                }
            } else {
                addComponentList(clazz.getName());
            }
        } else if (clazz.isInterface() && PROXY.equals(interfaceAndImplMap.get(clazz.getName()))) {
            // 需要实例化的类是接口，同时，接口没有实现类的，也没有加上指定的注解的
            addComponentList(clazz.getName());
        }
    }

    /**
     * 当字段上有MyAutowired注解时，需要递归处理
     * @param declaredFields
     * @param clazz
     */
    private static void anyFieldsHasMyAutowired(Field[] declaredFields, Class clazz) {
        Arrays.stream(declaredFields)
                .filter(ScanUtil::checkFieldHasMyAutowiredAnnotation)
                .forEach(field -> {
                    String fieldClassName = field.getType().getName();
                    if (isInterFace(fieldClassName)) {
                        // 字段的类型名，是接口
                        String nextName = convertInterfaceToImpl(fieldClassName);
                        if (StringUtils.isNotBlank(nextName) && !componentList.contains(nextName)) {
                            resolveComponent(nextName);
                        }
                    } else {
                        // 如果实现类是具体的类，那么就要继续递归添加
                        resolveComponent(fieldClassName);
                    }
                });
    }

    private static void addComponentList(String name) {
        if (!componentList.contains(name)) {
            componentList.add(name);
        }
    }

    /**
     * 当字段类型是接口的时候，转换成对应的实现类
     * @param fieldClassName
     * @return
     */
    private static String convertInterfaceToImpl(String fieldClassName) {
        for (Map.Entry<String, String> entry : interfaceAndImplMap.entrySet()) {
            if (entry.getKey().equals(fieldClassName) && !PROXY.equals(entry.getValue())) {
                return entry.getValue();
            } else if (entry.getKey().equals(fieldClassName) && PROXY.equals(entry.getValue())){
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 判断字段上是否有注解
     * @param declaredFields
     * @return
     */
    private static boolean hasAnyFieldWithAutowired(Field[] declaredFields) {
        return Arrays.stream(declaredFields).anyMatch(ScanUtil::checkFieldHasMyAutowiredAnnotation);
    }

    /**
     * 校验当前类是否有指定注解
     * @param className
     * @return
     */
    private static boolean checkClassHasCtrlAndServiceAndReposityAnnotations(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return AnnotationUtils.hasAnnotion(clazz, new Class[]{MyController.class, MyRepository.class, MyService.class});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 校验字段是否有MyAutowired注解
     * @param field
     * @return
     */
    private static boolean checkFieldHasMyAutowiredAnnotation(Field field) {
        return !AnnotationUtils.isEmpty(field.getAnnotation(MyAutowired.class));
    }

    /**
     * 校验字段是否不含MyAutowired注解
     * @param field
     * @return
     */
    private static boolean checkFieldNotHasMyAutowiredAnnotation(Field field) {
        return !AnnotationUtils.isEmpty(field.getAnnotation(MyAutowired.class));
    }

    /**
     * 是否是接口
     * @param className
     * @return
     */
    private static boolean isInterFace(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isInterface()) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 非接口
     * @param className
     * @return
     */
    private static boolean isNotInterFace(String className) {
        return !isInterFace(className);
    }

    /**
     * 判断接口时否在指定的集合中
     * @param interfaceClass
     * @param interfaceNames
     * @return
     */
    private static boolean isInInterfaceList(Class interfaceClass, List<String> interfaceNames) {
        return interfaceNames.stream().anyMatch(interfaceName -> interfaceName.equals(interfaceClass.getName()));
    }
}
