package connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String url = "jdbc:mysql://localhost:3306/twitterdb";
    private static final String user = "root";
    private static final String password = "";
    public static java.sql.Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
