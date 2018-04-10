package cn.jrc.dao;

import cn.jrc.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas.Jin on 2018/3/29.
 */
public class TaskDao extends BaseDao {
    public static Logger LOGGER = LoggerFactory.getLogger(TaskDao.class);

    public boolean insert(Task task) {
        connection = getConnection();
        String sql = "INSERT INTO task(url,state) VALUES(?,?)";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setString(1, task.getUrl());
            psmt.setInt(2, task.getState());
            psmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection, psmt, resultSet);
        }
    }

    public void update(String url, int state) {
        connection = getConnection();
        String sql = "UPDATE task SET state = ? WHERE url=?";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setInt(1, state);
            psmt.setString(2, url);
            psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, psmt, resultSet);
        }
    }

    public List<String> getUrlsByState(int state) {
        connection = getConnection();
        List<String> urls = new ArrayList<>();
        String sql = "SELECT url FROM task WHERE state = ?";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setInt(1, state);
            resultSet = psmt.executeQuery();
            while (resultSet.next()) {
                urls.add(resultSet.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, psmt, resultSet);
        }
        return urls;
    }


    public boolean exists(String url) {
        connection = getConnection();
        String sql = "SELECT count(*) FROM task WHERE url=?";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setString(1, url);
            resultSet = psmt.executeQuery();
            while (resultSet.next()) {
                int count = resultSet.getInt("count(*)");
                if (count != 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection, psmt, resultSet);
        }
        return false;
    }
}
