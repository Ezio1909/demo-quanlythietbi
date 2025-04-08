package quanlythietbi.connector.factory;

import quanlythietbi.enums.DBType;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory implements IConnectionFactory {

    private final DBType dbType;

    public ConnectionFactory(DBType dbType) {
        this.dbType = dbType;
    }

    public Connection getConnection() throws SQLException {
        IConnectionFactory factory = null;
        switch (dbType) {
            case H2 -> factory = new H2ConnectionFactoryImpl();
            case SQLITE -> factory = new SQLiteConnectionFactoryImpl();
        }
        return factory.getConnection();
    }

}
