package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/student_system_database");
        config.setUsername("root");
        config.setPassword("Aa123456");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10); // Số kết nối tối đa trong pool (tùy chỉnh theo nhu cầu)
        config.setMinimumIdle(5);      // Số kết nối tối thiểu giữ trong pool
        config.setIdleTimeout(30000);  // Thời gian tối đa giữ kết nối không sử dụng (30 giây)
        config.setConnectionTimeout(30000); // Thời gian chờ để lấy kết nối (30 giây)

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}