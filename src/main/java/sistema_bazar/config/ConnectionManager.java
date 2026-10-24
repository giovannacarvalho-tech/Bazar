package sistema_bazar.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sistema_bazar";

    private static final String USER = "bazar_app";

    private static final String PASSWORD = "BazarDev2026";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

