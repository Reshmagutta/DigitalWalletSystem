package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/digital_wallet";
        String username = "root";
        String password = "Reshma1404";

        return DriverManager.getConnection(url, username, password);
    }
}