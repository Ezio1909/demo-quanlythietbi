package quanlythietbi.connector.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionFactoryImpl implements IConnectionFactory {
    private final String url;
    private final String user;
    private final String password;

    public MySQLConnectionFactoryImpl(String host, String port, String db, String user, String password) {
        this.url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", host, port, db);
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
} 