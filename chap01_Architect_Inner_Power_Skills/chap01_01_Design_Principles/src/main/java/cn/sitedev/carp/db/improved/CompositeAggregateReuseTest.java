package cn.sitedev.carp.db.improved;


public class CompositeAggregateReuseTest {
    public static void main(String[] args) {
        DBConnection connection = new MySQLConnection();
        ProductDao productDao = new ProductDao();
        productDao.setDbConnection(connection);
        productDao.addProduct();
    }
}
