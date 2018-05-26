package cn.jrc.dao;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Lucas.Jin on 2018/3/28.
 */
public class BaseDao {
    private static String url = null;
    private static String driverClass = null;
    private static String userName = null;
    private static String password = null;
    Connection connection = null;
    PreparedStatement psmt = null;
    ResultSet resultSet = null;

    static {
        try {
            Properties properties = new Properties();
            InputStream in = BaseDao.class.getResourceAsStream("/db.properties");
            properties.load(in);
            url = properties.getProperty("url");
            driverClass = properties.getProperty("driverClass");
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
            Class.forName(driverClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs == null) {
        } else {
            try {
                rs.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
                throw new RuntimeException(e1);
            }
        }
        if (stmt != null) try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqe) {
                sqe.printStackTrace();
                throw new RuntimeException(sqe);
            }
        }
    }
}
