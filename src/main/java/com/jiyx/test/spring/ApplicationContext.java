package com.jiyx.test.spring;

import com.jiyx.test.common.constant.XmlElementEnum;
import com.jiyx.test.common.util.ConvertUtils;
import com.jiyx.test.common.util.MethodUtils;
import com.jiyx.test.spring.interfaces.BeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * auther: jiyx
 * date: 2018/11/26.
 */
@Slf4j
public class ApplicationContext extends FileSystemXmlApplicationContext implements BeanFactory {

    private Map<String, GenericBeanDefinition> beanDefinitionMap;

    public ApplicationContext(String contextConfigLocation) {
        this.beanDefinitionMap = super.getBeanDefinitionMap(contextConfigLocation);
    }

    @Override
    public Object getBean(String beanId) {
        assert beanId == null : "beanId 不存在";
        Object object = null;
        Class clazz = null;
        Set<Map.Entry<String, GenericBeanDefinition>> beanDefinitionSet = beanDefinitionMap.entrySet();

        // 如果容器中存在beanId
        if (beanDefinitionMap.containsKey(beanId)) {

            // 如果存在。开始遍历每一个bean
            for (Map.Entry<String, GenericBeanDefinition> entry : beanDefinitionSet) {
                if (entry.getKey().equals(beanId)) {
                    GenericBeanDefinition beanDefinition = entry.getValue();
                    String className = beanDefinition.getClassName();
                    List<ChildBeanDefinition> childBeanDefinitionList = beanDefinition.getChildBeanDefinitionList();

                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        log.error("{}没有找到", className);
                    }

                    try {
                        object = clazz.newInstance();
                    } catch (InstantiationException e) {
                        log.error("{}实例化异常", className);
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    if (childBeanDefinitionList != null && childBeanDefinitionList.size() > 0) {
                        if (XmlElementEnum.SET_INJECT.getType().equals(childBeanDefinitionList.get(0).getChildrenType())) {
                            // set依赖注入
                            setValue(object, childBeanDefinitionList);
                        } else if (XmlElementEnum.CONS_INJECT.getType().equals(childBeanDefinitionList.get(0).getChildrenType())) {
                            object = consValue(clazz, childBeanDefinitionList);
                        }
                    }

                    return object;
                }
            }

        }

        if (object == null) {
            throw new RuntimeException("获取bean:{}失败");
        }
        log.info("实例化类：{}", object);
        return object;
    }

    /**
     * 通过构造器重新实例化一个对象， 这里想了想，如果构造器的参数顺序和构造器中的参数顺序不一致该如何处理呢？
     * 解决思路：因为在xml中的bean配置的时候，没有配置参数的类型，所以不知道是那种构造器了，因此，这里采用的
     * 方法是，只要构造器的参数长度和配置的相同，就会直接赋值。所以要求不能出现多个构造器参数长度相同，否则可能报错
     * @param clazz
     * @param childBeanDefinitionList
     * @return
     */
    private Object consValue(Class clazz, List<ChildBeanDefinition> childBeanDefinitionList) {
        Constructor[] constructors = clazz.getConstructors();
        Object object = null;

        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();

            // 参数列表长度不一样
            if (parameterTypes.length != childBeanDefinitionList.size()) {
                continue;
            }

            Object[] params = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                params[i] = cast(parameterTypes[i], childBeanDefinitionList.get(i).getAttributeOne());
            }

            try {
                object = constructor.newInstance(params);
                break;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        assert object == null : String.format("实例化%s失败", clazz.getName());

        return object;
    }

    /**
     * object的属性set赋值
     * @param object
     * @param childBeanDefinitionList
     */
    private void setValue(Object object, List<ChildBeanDefinition> childBeanDefinitionList) {

        for (ChildBeanDefinition childBeanDefinition : childBeanDefinitionList) {
            String methodName = MethodUtils.getAttrSetMethodName(childBeanDefinition.getAttributeOne());
            Class paramType = null;

            try {
                paramType = object.getClass().getDeclaredField(childBeanDefinition.getAttributeOne()).getType();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            try {
                Method setMethod = object.getClass().getDeclaredMethod(methodName, paramType);
                // 这里没有考虑boolean，还有引用其他对象的引用类型，而且这段代码有点丑到天际了
                setMethod.invoke(object, cast(paramType, childBeanDefinition.getAttributetwo()));
            } catch (NoSuchMethodException e) {
                log.error("{}方法获取失败", methodName);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                log.error("{}类型转换出错", childBeanDefinition.getAttributetwo());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                log.error("{}方法调用失败", methodName);
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据参数类型，转换类型
     * @param paramType
     * @param attribute
     * @return
     */
    private Object cast(Class paramType, String attribute) {
        return ConvertUtils.convert(paramType.getSimpleName(), attribute);
    }

    public static <T> T getBean(Class<? extends T> clazz) {
        return (T) InitBean.getBeanContainerMap().get(clazz.getName());
    }
}
