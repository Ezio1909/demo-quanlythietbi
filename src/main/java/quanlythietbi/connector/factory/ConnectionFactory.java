package quanlythietbi.connector.factory;

import java.sql.Connection;
import java.sql.SQLException;

import quanlythietbi.enums.DBType;

public class ConnectionFactory implements IConnectionFactory {

    private final DBType dbType;

    public ConnectionFactory(DBType dbType) {
        this.dbType = dbType;
    }

    @Override
    public Connection getConnection() throws SQLException {
        IConnectionFactory factory = null;
        switch (dbType) {
            case H2 -> factory = new H2ConnectionFactoryImpl();
            case SQLITE -> factory = new SQLiteConnectionFactoryImpl();
            case MYSQL -> factory = new MySQLConnectionFactoryImpl();
        }
        return factory.getConnection();
    }

}
