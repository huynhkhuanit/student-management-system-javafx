package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class database {
    private static final String URL = "jdbc:mysql://localhost:3306/student_system_database";
    private static final String USER = "root";
    private static final String PASSWORD = "Aa123456";

    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(URL, USER, PASSWORD);
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Đóng kết nối
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
