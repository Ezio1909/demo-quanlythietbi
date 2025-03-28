package quanlythietbi.connector;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.enums.DBType;

public class ConnectionManagerImplTest {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManagerImplTest.class);
    private static final IConnectionManager connectionManager = new ConnectionManagerImpl(DBType.H2);

    @Test
    public void testInitConnectionManagerSuccess() {
        logger.info("connectionManager: {}", connectionManager);
        assertNotNull(connectionManager);
    }

    @Test
    public void testDiffManagerSameConnection() {
        IConnectionManager anotherManager = new ConnectionManagerImpl(DBType.H2);
        logger.info("anotherManager: {}", anotherManager);
        assertNotEquals(connectionManager, anotherManager);
        Connection conn = null;
        Connection anotherConn = null;
        try {
            conn = connectionManager.getConnection();
            anotherConn = connectionManager.getConnection();
        } catch (SQLException e) {
            logger.info("Exception when get connection: {}", e.getMessage());
        }
        logger.info("conn: {}", conn);
        logger.info("anotherConn: {}", anotherConn);
        assertNotNull(conn);
        assertNotNull(anotherConn);
        assertEquals(conn, anotherConn);
    }

}
