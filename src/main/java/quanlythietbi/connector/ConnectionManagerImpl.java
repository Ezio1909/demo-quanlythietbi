package quanlythietbi.connector;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.connector.factory.ConnectionFactory;
import quanlythietbi.connector.factory.IConnectionFactory;
import quanlythietbi.enums.DBType;

/**
 * Provide a singleton thread-safe connection provider. Once the connection is created, it must be reused by other threads
 * so that we avoid the database connection pool exhaustion <br><br>
 *
 * Nam's note: <br>
 * To achieve our design's goal, we applied two new things here: (1) double-checked lock and (2) volatile variable <br><br>
 *
 * (1) To accomplish the singleton pattern, we let each thread check if there is already existed connection and only create new connection
 * where there is none by using synchronize for the block that init new Connection. <br>
 * Now, there is an edge case of multiple threads reach this synchronized block at the same time! For example, consider case thread A and thread B
 * both reach synchronized block at time t=0. When this happens (without checking existing connection again), even after thread A created connection,
 * thread B will repeat create another connection!<br>
 * So in short, synchronize helps us order the sequence of execution between thread A and thread B (occur at same time) BUT we still need to handle
 * check existing logic correctly. Any subsequent threads now do not need to reach this synchronized block since the connection is already existed! <br><br>
 *
 * (2) for the singletonConnection variable, we use volatile instead of final because final does not GUARANTEE the fully object construction!
 * In terms of Java Memory Model, every time an objection is constructed, here is what happens: <br>
 * 	1.	Allocate memory for the object <br>
 * 	2.	Initialize the object <br>
 * 	3.	Assign the reference to singletonConnection <br>
 * 	Without volatile, the compiler could reorder steps 2 and 3, meaning: <br>
 * 	 - singletonConnection is set before the object is fully initialized <br>
 * 	 - Another thread reads the reference and accesses an object that's half-baked → NullPointerException <br>
 * volatile prevents this by enforcing happens-before rules: <br>
 * Writes <b>before</b> the volatile assignment (e.g. object construction) happen <b>before</b> any other thread can read it
 * */
public class ConnectionManagerImpl implements IConnectionFactory, AutoCloseable {

    private final DBType dbType;
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManagerImpl.class);
    private static volatile Connection singletonConnection;

    public ConnectionManagerImpl(DBType dbType) {
        this.dbType = dbType;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (isConnected()) {
            return singletonConnection;
        }
        synchronized (ConnectionManagerImpl.class) {
            if (isConnected()) {
                return singletonConnection;
            }
            IConnectionFactory factory = new ConnectionFactory(dbType);
            singletonConnection = factory.getConnection();
            return singletonConnection;
        }
    }

    @Override
    public void close() throws SQLException {
        logger.info("Closing connection {}...", singletonConnection);
        if (singletonConnection != null && !singletonConnection.isClosed()) {
            singletonConnection.close();
        }
    }

    private boolean isConnected() throws SQLException {
        return singletonConnection != null && !singletonConnection.isClosed();
    }
}