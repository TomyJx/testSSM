package com.jiyx.test.spring;

import com.jiyx.test.common.constant.XmlElementEnum;
import com.jiyx.test.common.util.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * auther: jiyx
 * date: 2018/11/22.
 */
@Slf4j
public class XmlApplicationContext {

    /**
     * 获取配置文件中的bean信息,xml中bean配置的类信息应该都是实体类，不是接口类
     * @param contextConfigLocation
     * @return
     */
    public Map<String, GenericBeanDefinition> getBeanDefinitionMap(String contextConfigLocation) {
        log.debug("XmlApplicationContext getBeanDefinitionMap begin");
        // 定义好容器
        Map<String, GenericBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
        // 解析配合文件
        List<Element> elementList = XmlUtils.getElements(contextConfigLocation);

        if (elementList == null) {
            return null;
        }

        // 解析bean等二级元素
        for (Element element : elementList) {

            // 如果不是bean元素，跳过
            if (!element.getName().equals("bean")) {
                continue;
            }

            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            List<ChildBeanDefinition> childBeanDefinitionList = new ArrayList<>(4);

            // 获取当前bean元素的id和className
            String beanId = element.attributeValue(XmlElementEnum.BEAN_RULE.getName());
            String beanClass = element.attributeValue(XmlElementEnum.BEAN_RULE.getValue());

            if (StringUtils.isNotBlank(beanId) && StringUtils.isNotBlank(beanClass)) {
                beanDefinition.setClassName(beanClass);
                List<Element> childrenElements = element.elements();

                // 解析子属性
                if (childrenElements != null) {
                    for (Element childrenElement : childrenElements) {
                        if (childrenElement.getName().equals(XmlElementEnum.SET_INJECT.getType())) {
                            // set 注入
                            childBeanDefinitionList.add(setInject(childrenElement));
                        } else {
                            // 构造器注入
                            childBeanDefinitionList.add(consInject(childrenElement));
                        }

                    }

                }

                beanDefinition.setChildBeanDefinitionList(childBeanDefinitionList);
                beanDefinitionMap.put(beanId, beanDefinition);
            }

        }

        log.debug("XmlApplicationContext getBeanDefinitionMap end");
        return beanDefinitionMap;
    }

    /**
     * 构造器注入
     * @param childrenElement
     * @return
     */
    private ChildBeanDefinition consInject(Element childrenElement) {
        ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition();
        childBeanDefinition.setChildrenType(XmlElementEnum.CONS_INJECT.getType());
        childBeanDefinition.setAttributeOne(childrenElement.attributeValue(XmlElementEnum.CONS_INJECT.getName()));
        childBeanDefinition.setAttributetwo(childrenElement.attributeValue(XmlElementEnum.CONS_INJECT.getValue()));
        return childBeanDefinition;
    }

    /**
     * set注入的值设置
     * @param childrenElement
     * @return
     */
    private ChildBeanDefinition setInject(Element childrenElement) {
        ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition();
        childBeanDefinition.setChildrenType(XmlElementEnum.SET_INJECT.getType());
        childBeanDefinition.setAttributeOne(childrenElement.attributeValue(XmlElementEnum.SET_INJECT.getName()));
        childBeanDefinition.setAttributetwo(childrenElement.attributeValue(XmlElementEnum.SET_INJECT.getValue()));
        return childBeanDefinition;
    }

    /**
     * 根据指定的xml，获得注解扫描的bean容器
     * @param contextConfigLocation
     * @return
     */
    public List<String> getComponentScanList(String contextConfigLocation) {
        log.debug("XmlApplicationContext getComponentScanList begin");
        List<String> componentList = new ArrayList<>();
        for (Element element : XmlUtils.getElements(contextConfigLocation)) {
            if (element.getName().equals(XmlElementEnum.SNAN_RULE.getType())) {
                componentList.addAll(resolveComponentList(element.attributeValue(XmlElementEnum.SNAN_RULE.getName())));
            }
        }
        log.debug("XmlApplicationContext getComponentScanList end");
        return componentList;
    }

    /**
     * 根据扫描的包名，返回有注解的类
     * @param packageName
     * @return
     */
    public List<String> resolveComponentList(String packageName) {
        if (StringUtils.isBlank(packageName)) {
            throw new IllegalArgumentException("error config with" + XmlElementEnum.SNAN_RULE.getType());
        }
        return ScanUtil.getComponentList(packageName);
    }

}
