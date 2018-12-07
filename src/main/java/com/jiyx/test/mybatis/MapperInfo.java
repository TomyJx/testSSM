package com.jiyx.test.mybatis;

import lombok.Data;

/**
 * auther: jiyx
 * date: 2018/12/1.
 */
@Data
public class MapperInfo {
    private String interfaceName;
    private String sqlContent;
    private String methodName;
    private String resultClassName;
}
