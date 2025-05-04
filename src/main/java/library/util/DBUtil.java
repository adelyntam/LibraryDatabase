package library.util;

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

    // Get new DB conn using manager
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
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
