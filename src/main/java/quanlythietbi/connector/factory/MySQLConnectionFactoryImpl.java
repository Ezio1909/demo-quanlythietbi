package quanlythietbi.connector.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionFactoryImpl implements IConnectionFactory {
    private static final String urlString = "jdbc:mysql://localhost:3306/devicedb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String user = "devuser";
    private static final String password = "devpass";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(urlString, user, password);
    }
} 