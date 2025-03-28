package quanlythietbi.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionImpl implements IConnectionManager {

    private static final String urlString = "jdbc:sqlite:device_manager.db";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(urlString);
    }

}
