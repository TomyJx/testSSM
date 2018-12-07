package com.jiyx.test.common.constant;

import lombok.Getter;

/**
 * auther: jiyx
 * date: 2018/12/2.
 */
@Getter
public enum MapperXmlEnum {

    NAMESPACE("namespace"),
    SELECT("select"),
    UPDATE("update"),
    DELETE("delete"),
    INSERT("insert"),
    ELEMENT_ID("id"),
    ELEMENT_RESULT_TYPE("resultType");

    private String name;

    MapperXmlEnum(String name) {
        this.name = name;
    }

}
