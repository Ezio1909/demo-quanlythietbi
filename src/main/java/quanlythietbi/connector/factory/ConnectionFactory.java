package quanlythietbi.connector.factory;

import java.sql.Connection;
import java.sql.SQLException;

import quanlythietbi.enums.DBType;

public class ConnectionFactory implements IConnectionFactory {

    private final DBType dbType;
    private String host, port, db, user, password;

    public ConnectionFactory(DBType dbType) {
        this.dbType = dbType;
    }

    public ConnectionFactory(DBType dbType, String host, String port, String db, String user, String password) {
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        IConnectionFactory factory = null;
        switch (dbType) {
            case H2 -> factory = new H2ConnectionFactoryImpl();
            case SQLITE -> factory = new SQLiteConnectionFactoryImpl();
            case MYSQL -> factory = new MySQLConnectionFactoryImpl(host, port, db, user, password);
        }
        return factory.getConnection();
    }

}
