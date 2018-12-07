package com.jiyx.test.mybatis;

import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

/**
 * 动态代理类
 * auther: jiyx
 * date: 2018/12/1.
 */
public class MyMapperProxy implements InvocationHandler {

    private MySqlSession mySqlSession;
    private String mybatisXmlName;
    private Class clazz;

    public MyMapperProxy(MySqlSession mySqlSession, String mybatisXmlName, Class clazz) {
        this.mySqlSession = mySqlSession;
        this.mybatisXmlName = mybatisXmlName;
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        XmlBuilderMapper xmlBuilderMapper = new XmlBuilderMapper();
        List<MapperInfo> mapperInfoList = xmlBuilderMapper.buildMapper(clazz, mybatisXmlName);
        if (mapperInfoList != null) {
            // 如果xml文件中的方法的命名空间与method的所有者不同，直接返回null。(应该是文件搞错了)
            if (mapperInfoList.stream().anyMatch(mapperInfo -> !method.getDeclaringClass().getName().equals(mapperInfo.getInterfaceName()))) {
                return null;
            }

            for (MapperInfo mapperInfo : mapperInfoList) {
                if (method.getName().equals(mapperInfo.getMethodName())) {
                    return mySqlSession.selectOne(mapperInfo, args);
                }
            }
        }
        return null;
    }
}
