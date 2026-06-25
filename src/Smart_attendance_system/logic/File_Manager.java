package Smart_attendance_system.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class File_Manager {
    // Added autoReconnect to keep the connection from timing out
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/smart_attendance?useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true";
    private static final String USER = "root";
    private static final String PASS = "root123"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            throw new SQLException(e);
        }
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("Connection Successful!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}