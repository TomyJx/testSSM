package com.jiyx.test.springMVC;

import java.util.LinkedHashMap;

/**
 * auther: jiyx
 * date: 2018/11/30.
 */
public class MyModelMap extends LinkedHashMap<String, Object> implements MyModel {
    @Override
    public MyModel addAttribute(String attributeName, Object attributeValue) {
        put(attributeName, attributeValue);
        return this;
    }
}
