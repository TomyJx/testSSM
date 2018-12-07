package com.jiyx.test.spring;

import lombok.Data;

/**
 * auther: jiyx
 * date: 2018/11/22.
 */
@Data
public class ChildBeanDefinition {

    /**
     * 这个是property或者constructor-arg类型
     */
    private String childrenType;

    /**
     * 标签的第一个属性
     */
    private String attributeOne;

    /**
     * 标签的第二个属性
     */
    private String attributetwo;

}
