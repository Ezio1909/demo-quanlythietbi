package quanlythietbi.connector;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface IConnectionTask <T> {

    T doTask(Connection conn) throws SQLException;

}
