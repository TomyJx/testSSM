package com.jiyx.test.spring;

import java.util.List;
import java.util.Map;

/**
 * auther: jiyx
 * date: 2018/11/26.
 */
public class BeanDefinition extends XmlApplicationContext {

    @Override
    public List<String> getComponentScanList(String contextConfigLocation) {
        return super.getComponentScanList(contextConfigLocation);
    }

    @Override
    public Map<String, GenericBeanDefinition> getBeanDefinitionMap(String contextConfigLocation) {
        return super.getBeanDefinitionMap(contextConfigLocation);
    }
}
