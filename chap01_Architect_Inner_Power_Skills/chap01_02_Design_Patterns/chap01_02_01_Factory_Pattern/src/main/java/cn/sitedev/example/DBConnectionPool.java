package cn.sitedev.example;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * 数据库连接池管理类
 */
public class DBConnectionPool extends Pool {
    // 正在使用的连接数
    private int checkedOut;

    // 存放产生的连接对象容器
    private Vector<Connection> freeConnections = new Vector<>();

    // 密码
    private String password = null;

    // 连接字符串
    private String url = null;

    // 用户名
    private String userName = null;

    // 空闲连接数
    private static int num = 0;

    // 当前可用的连接数
    private static int numActive = 0;

    // 连接池实例对象
    private static DBConnectionPool pool = null;

    // 产生数据库连接池
    public static synchronized DBConnectionPool getInstance() {
        if (pool == null) {
            pool = new DBConnectionPool();
        }
        return pool;
    }

    // 获得一个数据库连接池的实例
    private DBConnectionPool() {
        try {
            init();
            // 初始化normalConnect个连接
            for (int i = 0; i < this.normalConnect; i++) {
                Connection connection = this.newConnection();
                if (connection != null) {
                    // 往容器中添加一个连接对象
                    this.freeConnections.addElement(connection);
                    // 记录总连接数
                    num++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 初始化
    private void init() throws IOException {
        InputStream inputStream = DBConnectionPool.class.getClassLoader().getResourceAsStream(propertiesName);
        Properties properties = new Properties();
        properties.load(inputStream);
        this.userName = properties.getProperty("userName");
        this.password = properties.getProperty("password");
        this.driverName = properties.getProperty("driverName");
        this.url = properties.getProperty("url");
        this.maxConnect = Integer.parseInt(properties.getProperty("maxConnect"));
        this.normalConnect = Integer.parseInt(properties.getProperty("normalConnect"));
    }

    // 创建一个新连接
    private Connection newConnection() {
        Connection connection = null;
        try {
            if (this.userName == null || this.password == null) {
                // 用户名, 密码为空
                connection = DriverManager.getConnection(url);
            } else {
                connection = DriverManager.getConnection(url, userName, password);
            }
            System.out.println("连接池创建一个新的连接");
        } catch (SQLException e) {
            System.out.println("无法创建这个url的连接: " + url);
        }
        return connection;
    }

    // 建立连接池
    @Override
    public void createPool() {
        pool = new DBConnectionPool();
        if (pool != null) {
            System.out.println("创建连接池成功");
        } else {
            System.out.println("创建连接池失败");
        }
    }

    // 获取一个可用连接(单例模式)
    @Override
    public Connection getConnection() {
        Connection connection = null;
        // 还有空闲的连接
        if (this.freeConnections.size() > 0) {
            num++;
            connection = this.freeConnections.firstElement();
            this.freeConnections.removeElementAt(0);
            try {
                if (connection.isClosed()) {
                    System.out.println("从连接池中删除一个无效连接");
                    connection = getConnection();
                }
            } catch (SQLException e) {
                System.out.println("从连接池中删除一个无效连接");
                connection = getConnection();
            }
        } else if (this.maxConnect == 0 || this.checkedOut < this.maxConnect) {
            // 没有空闲连接且当前连接小于最大允许值, 最大值为0 则不限制
            connection = this.newConnection();
        }
        if (connection != null) {
            // 当前连接数+1
            this.checkedOut++;
        }
        numActive++;
        return connection;
    }

    // 获取一个可用连接(单例模式), 并加上等待时间限制, 时间单位为ms
    @Override
    public Connection getConnection(long timeout) {
        synchronized (this) {
            long startTime = System.currentTimeMillis();
            Connection connection = null;
            while ((connection = this.getConnection()) == null) {
                try {
                    // 线程等待
                    wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (System.currentTimeMillis() - startTime >= timeout) {
                    // 如果超时, 则返回
                    return null;
                }
            }
            return connection;
        }
    }

    // 如果不再使用某个连接对象时, 可调用此方法将对象释放到连接池
    @Override
    public void freeConnection(Connection connection) {
        synchronized (this) {
            this.freeConnections.add(connection);
            num++;
            this.checkedOut++;
            numActive++;
            // 解锁
            notifyAll();
        }
    }

    @Override
    public int getnum() {
        return num;
    }

    @Override
    public int getnumActive() {
        return numActive;
    }

    // 关闭所有连接
    @Override
    protected synchronized void release() {
        try {
            // 将当前连接赋值到枚举中
            Enumeration<Connection> allConnections = this.freeConnections.elements();
            // 使用循环关闭所有连接
            while (allConnections.hasMoreElements()) {
                // 如果此枚举对象至少还有一个可提供的元素, 则返回此枚举的下一个元素
                Connection connection = allConnections.nextElement();
                try {
                    connection.close();
                    num--;
                } catch (SQLException e) {
                    System.out.println("无法关闭连接池中的连接, 原因: " + e.getMessage());
                }
            }
            this.freeConnections.removeAllElements();
            numActive = 0;
        } finally {
            super.release();
        }
    }
}
