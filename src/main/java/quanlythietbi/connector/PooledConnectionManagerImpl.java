package quanlythietbi.connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quanlythietbi.connector.factory.ConnectionFactory;
import quanlythietbi.connector.factory.IConnectionFactory;
import quanlythietbi.enums.DBType;

/**
 * Add pooled connection manager so client can execute multiple queries at the same time. <br>
 * Each query cycle is handled by one connection at a time
 * */
public class PooledConnectionManagerImpl implements IConnectionManager, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(PooledConnectionManagerImpl.class);
    private static final ExecutorService executors = Executors.newFixedThreadPool(8);
    private static BlockingQueue<Connection> connectionPool;
    private static IConnectionFactory connectionFactory;

    public PooledConnectionManagerImpl(DBType dbType, int poolSize) {
        connectionPool = new LinkedBlockingQueue<>(poolSize);
        connectionFactory = new ConnectionFactory(dbType);

        CompletableFuture<?>[] futures = new CompletableFuture[poolSize];
        for (int i = 0; i < poolSize; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    Connection connection = connectionFactory.getConnection();
                    boolean isSuccessful = connectionPool.offer(connection);
                    if (isSuccessful) {
                        logger.info("Added connection: {}", connection);
                    }
                } catch (SQLException e) {
                    logger.error(e.getMessage(), e);
                }
            }, executors);
        }

        CompletableFuture.allOf(futures).join();
        logger.info("All {} connections initialized (pool size: {}).", poolSize, poolSize);
    }

    @Override
    public <T> T doTask(IConnectionTask<T> task) {
        Connection conn = null;
        try {
            conn = connectionPool.take();
            if (conn == null) {
                throw new SQLException("Connection is null");
            }
            return task.doTask(conn);
        } catch (InterruptedException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                returnConnection(conn);
            }
        }
    }

    private void returnConnection(Connection conn) {
        if (conn != null) {
            connectionPool.offer(conn);
        }
    }

    @Override
    public void close() {
        logger.info("Closing connection {}...", connectionPool);
    }
}