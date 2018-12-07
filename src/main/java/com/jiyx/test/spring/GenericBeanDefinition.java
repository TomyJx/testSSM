package com.jiyx.test.spring;

import lombok.Data;

import java.util.List;

/**
 * auther: jiyx
 * date: 2018/11/22.
 */
@Data
public class GenericBeanDefinition {

    /**
     * className和xml中的class对应
     */
    private String className;

    /**
     * bean下面的属性集合
     */
    private List<ChildBeanDefinition> childBeanDefinitionList;
}
