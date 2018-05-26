package cn.jrc.dao;

import cn.jrc.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lucas.Jin on 2018/3/29.
 */
@Repository
public class TaskDao extends BaseDao {
    public boolean insert(Task task) {
        connection = getConnection();
        String sql = "INSERT INTO task(url,state,update_time) VALUES(?,?,?)";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setString(1, task.getUrl());
            psmt.setInt(2, task.getState());
            psmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            psmt.execute();
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            close(connection, psmt, resultSet);
        }
    }

    public void update(String url, int state) {
        connection = getConnection();
        String sql = "UPDATE task SET state = ?,update_time=? WHERE url=?";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setInt(1, state);
            psmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            psmt.setString(3, url);
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
        String sql = "SELECT url FROM task WHERE state = ? LIMIT 500";
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

    /**
     * return urls where update in param:date
     * @param date format:yyyy-MM-dd
     * @return urls where update in param:date
     */
    public List<String> getUrlsByDate(String date) {
        connection = getConnection();
        List<String> urls = new ArrayList<>();
        String sql = "SELECT url FROM task WHERE date(update_time)=? LIMIT 500";
        try {
            psmt = connection.prepareStatement(sql);
            psmt.setString(1, date);
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


}
