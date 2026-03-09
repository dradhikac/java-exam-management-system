package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    private static final String URL  =
            "jdbc:mysql://localhost:3306/dsatm_exam_system"
          + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASS = "Radhika11@2006";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // force load MySQL driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver load failed", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            // Log full stack trace so you can see exact cause
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unable to connect to database", e);
        }
    }
}
