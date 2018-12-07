package com.jiyx.test.spring;

import java.util.Map;

/**
 * auther: jiyx
 * date: 2018/11/26.
 */
public class FileSystemXmlApplicationContext extends XmlApplicationContext {
    @Override
    public Map<String, GenericBeanDefinition> getBeanDefinitionMap(String contextConfigLocation) {
        return super.getBeanDefinitionMap(contextConfigLocation);
    }
}
