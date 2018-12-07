package com.jiyx.test.mybatis;

import com.jiyx.test.common.util.ConvertUtils;
import com.jiyx.test.spring.ApplicationContext;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * auther: jiyx
 * date: 2018/12/2.
 */
public class MyExecutor {
    public <T> T query(MapperInfo mapperInfo, Object[] paramters) {
        // 处理sql
        String preSql = mapperInfo.getSqlContent();
        String regex = "#\\{.*?}";
        String sql = null;
        int orderPre = 0;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(preSql);
        while (matcher.find()) {
            orderPre++;
        }
        sql = matcher.replaceAll("?");

        // 执行并返回结果
        return (T) executeSql(orderPre, sql, paramters, mapperInfo);
    }

    // 执行sql
    private Object executeSql(int orderPre, String sql, Object[] paramters, MapperInfo mapperInfo) {
        JDBCUtils jdbcUtils = ApplicationContext.getBean(JDBCUtils.class);
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= orderPre; i++) {
                preparedStatement.setObject(i, paramters[i - 1]);
            }
            resultSet = preparedStatement.executeQuery();

            return packageResult(resultSet, mapperInfo);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
        return null;
    }

    /**
     * 封装结果
     * @param resultSet
     * @param mapperInfo
     * @return
     */
    private Object packageResult(ResultSet resultSet, MapperInfo mapperInfo) {
        Object result = null;
        try {
            String resultClassName = mapperInfo.getResultClassName();
            Class clazz = Class.forName(resultClassName);
            Field[] fields = clazz.getDeclaredFields();
            result = clazz.newInstance();
            while (resultSet.next()) {
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(result, ConvertUtils.convert(fields[i].getType().getSimpleName(), resultSet.getString(i + 1)));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
