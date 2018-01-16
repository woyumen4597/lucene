package cn.jrc.db;

import java.sql.*;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/15 16:25
 */
public class TestDB {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","woyumen4597");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        ResultSet res = null;

        try {
            statement = connection.createStatement();
            res = statement.executeQuery("select * from help_category");
            while(res.next()){
                String name = res.getString("name");
                System.out.println(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
