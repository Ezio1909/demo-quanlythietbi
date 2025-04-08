package quanlythietbi.connector;

import java.sql.SQLException;

public interface IConnectionManager {

    <T> T doTask(IConnectionTask<T> task) throws SQLException;

}
