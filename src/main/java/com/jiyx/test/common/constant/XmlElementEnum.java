package com.jiyx.test.common.constant;

import lombok.Data;
import lombok.Getter;

/**
 * ioc中xml的配置规则
 * auther: jiyx
 * date: 2018/11/22.
 */
@Getter
public enum XmlElementEnum {

    BEAN_RULE("bean", "id", "class"),
    SNAN_RULE("component-scan", "base-package", "null"),
    SET_INJECT("property", "name", "value"),
    CONS_INJECT("constructor-arg", "value", "index"),;

    private String type;
    private String name;
    private String value;

    XmlElementEnum(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
