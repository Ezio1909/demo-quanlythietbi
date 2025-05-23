package quanlythietbi.connector.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import quanlythietbi.connector.DatabaseInitializer;

public class H2ConnectionFactoryImpl implements IConnectionFactory {
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static volatile boolean initialized = false;

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!initialized) {
            synchronized (H2ConnectionFactoryImpl.class) {
                if (!initialized) {
                    DatabaseInitializer.initializeH2Database(conn);
                    initialized = true;
                }
            }
        }
        return conn;
    }
}