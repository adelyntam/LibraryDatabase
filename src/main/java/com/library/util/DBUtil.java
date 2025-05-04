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
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL driver not on classpath", e);
        }

        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Failed to load " + DB_CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error reading DB config", e);
        }
    }

    // Get new db conn
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String usr = properties.getProperty("db.user");
        String pw = properties.getProperty("db.password");
        return DriverManager.getConnection(url, usr, pw);
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
