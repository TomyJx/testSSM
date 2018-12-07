package com.jiyx.test.mybatis;

import java.lang.reflect.Proxy;

/**
 * auther: jiyx
 * date: 2018/12/1.
 */
public class MySqlSession {
    public <T> T selectOne(MapperInfo mapperInfo, Object[] paramters) {
        MyExecutor myExecutor = new MyExecutor();
        return myExecutor.query(mapperInfo, paramters);
    }

    /**
     * 根据mapper接口和xml文件，获取mapper的动态代理
     * @param clazz
     * @param mybatisXmlName
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class clazz, String mybatisXmlName) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MyMapperProxy(this, mybatisXmlName, clazz));
    }
}
