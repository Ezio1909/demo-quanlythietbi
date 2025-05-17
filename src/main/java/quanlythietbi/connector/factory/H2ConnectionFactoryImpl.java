package quanlythietbi.connector.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import quanlythietbi.connector.DatabaseInitializer;

public class H2ConnectionFactoryImpl implements IConnectionFactory {

    private static final String urlString = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL";
    private static final String user = "sa";
    private static final String password = "";
    private static boolean initialized = false;

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(urlString, user, password);
        if (!initialized) {
            synchronized (H2ConnectionFactoryImpl.class) {
                if (!initialized) {
                    DatabaseInitializer.initializeDatabase(conn);
                    initialized = true;
                }
            }
        }
        return conn;
    }

}