package com.jiyx.test.common.constant;

import com.jiyx.test.spring.FileSystemXmlApplicationContext;

/**
 * auther: jiyx
 * date: 2018/11/22.
 */
public class FileNameConstant {
    public static final String PATH = FileSystemXmlApplicationContext.class.getResource("/").getPath();
    public static final String contextConfigLocation = "application.xml";
    public static final String springMVCConfigLocation = "springmvc.xml";
    public static final String mybatisConfigLocation = "mapper/";

}
