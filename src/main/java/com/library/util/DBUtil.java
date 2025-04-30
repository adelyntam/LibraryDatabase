package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    private static final String DB_CONFIG_FILE = "db.properties";
    private static Properties properties = new Properties();

    // Load DB config
    static {
        try {
            // Load driver first
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Then load properties
            try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE)) {
                if (input == null) {
                    throw new RuntimeException("Failed to load " + DB_CONFIG_FILE);
                }
                properties.load(input);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Initialization failed", e);
        }
    }

    // Get new db conn
    public static Connection getConnection() throws SQLException {
        String baseUrl = properties.getProperty("db.url");
        String url = baseUrl + "?useSSL=false" +
                "&allowPublicKeyRetrieval=true" +
                "&serverTimezone=UTC" +
                "&useLegacyDatetimeCode=false";

        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        System.out.println("Connecting to: " + url);

        return DriverManager.getConnection(url, user, password);
    }

    // Close resources
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Overloaded close
    public static void close(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Overloaded close
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Close all
    public static void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(conn);
    }
}
