package cn.sitedev.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PoolTest {
    public static void main(String[] args) throws SQLException {
        Pool pool = DBConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from user");
        ResultSet rs = ps.executeQuery();
        // 遍历结果集
        while (rs.next()) {
            // columnIndex从1开始
            String host = rs.getString(1);
            String user = rs.getString(2);
            System.out.println("host: " + host + ", user: " + user);
        }
        pool.freeConnection(connection);
        pool.release();
    }
}
