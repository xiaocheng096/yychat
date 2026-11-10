package com.ycychat.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 实验20：用户登录验证的数据库代码封装。
 * 把数据库连接和登录校验集中到本类中，服务器只需调用 loginValidate 即可。
 */
public class DBUtil {

    public static String db_url = "jdbc:mysql://127.0.0.1:3306/yychat2022s?useUnicode=true&characterEncoding=utf-8"
            + "&serverTimezone=Asia/Shanghai&useSSL=false";
    public static String db_username = "root";
    public static String db_password = "";

    /** 静态 Connection 对象，类加载时建立一次连接 */
    public static Connection conn = getConnection();

    /**
     * 获取数据库连接。
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(db_url, db_username, db_password);
            System.out.println("数据库连接成功。");
        } catch (ClassNotFoundException e) {
            System.out.println("未找到 MySQL 驱动，登录将使用演示密码 123456 进行校验。");
        } catch (SQLException e) {
            System.out.println("数据库连接失败（" + e.getMessage() + "），登录将使用演示密码 123456 进行校验。");
        }
        return connection;
    }

    /**
     * 在 user 表中校验用户名和密码。
     *
     * @param userName 用户名
     * @param password 密码
     * @return 查到记录返回 true，否则返回 false
     */
    public static boolean loginValidate(String userName, String password) {
        // 没有可用的数据库时，保留课堂演示用的固定密码，保证程序仍可运行
        if (conn == null) {
            return "123456".equals(password);
        }

        boolean loginSuccess = false;
        String user_query_str = "select * from user where username=? and password=?";
        try (PreparedStatement psmt = conn.prepareStatement(user_query_str)) {
            psmt.setString(1, userName);
            psmt.setString(2, password);
            try (ResultSet rs = psmt.executeQuery()) {
                loginSuccess = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginSuccess;
    }
}