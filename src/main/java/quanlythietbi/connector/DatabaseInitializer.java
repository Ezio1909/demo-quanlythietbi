package quanlythietbi.connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String SCHEMA_FILE = "schema-h2.sql";
    private static volatile boolean initialized = false;
    private static final Object lock = new Object();

    public static void initializeDatabase(Connection connection) throws SQLException {
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
                    try (Statement stmt = connection.createStatement()) {
                        String schema = new BufferedReader(
                            new InputStreamReader(DatabaseInitializer.class.getClassLoader().getResourceAsStream(SCHEMA_FILE))
                        ).lines().collect(Collectors.joining("\n"));
                        
                        for (String sql : schema.split(";")) {
                            if (!sql.trim().isEmpty()) {
                                stmt.execute(sql);
                            }
                        }
                        initialized = true;
                        logger.info("Database schema initialized successfully");
                    } catch (Exception e) {
                        throw new SQLException("Failed to initialize database", e);
                    }
                }
            }
        }
    }
} 