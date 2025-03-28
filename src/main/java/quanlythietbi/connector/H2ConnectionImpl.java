package quanlythietbi.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2ConnectionImpl implements IConnectionManager {

    private static final String urlString = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String user = "sa";
    private static final String password = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(urlString, user, password);
    }

}