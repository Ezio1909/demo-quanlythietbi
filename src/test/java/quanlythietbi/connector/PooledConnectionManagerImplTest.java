package quanlythietbi.connector;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quanlythietbi.enums.DBType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PooledConnectionManagerImplTest {

    private static final Logger logger = LoggerFactory.getLogger(PooledConnectionManagerImplTest.class);
    private static final IConnectionManager connectionManager = new PooledConnectionManagerImpl(DBType.H2, 16);

    @Test
    public void testInitConnectionManagerSuccess() {
        logger.info("PooledConnectionManager: {}", connectionManager);
        assertNotNull(connectionManager);
    }

    @Test
    public void testDoTaskSuccess() throws SQLException {
        ResultSet result = connectionManager.doTask(conn -> {
            PreparedStatement stm = conn.prepareStatement("SELECT 1");
            return stm.executeQuery();
        });
        assertNotNull(result);
        logger.info("ResultSet: {}", result.first());
    }
}
