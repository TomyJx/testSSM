package com.jiyx.test.mybatis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * auther: jiyx
 * date: 2018/12/1.
 */
@Data
@Slf4j
public class JDBCUtils {

    private String url = "jdbc:mysql://localhost:3306/test";
    private String driver = "com.mysql.jdbc.Driver";
    private String username = "root";
    private String password = "root";

    public JDBCUtils() {
    }

    /**
     * 获取连接
     * @return
     */
    public Connection getConnection() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("error occur init jdbc driver");
        }

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 释放资源
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void closeResource(Connection connection, Statement statement, ResultSet resultSet) {
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }

    private static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        statement = null;
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        resultSet = null;
    }

    private static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection = null;
    }

}
