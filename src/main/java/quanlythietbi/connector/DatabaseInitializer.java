package quanlythietbi.connector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void initializeDatabase(Connection connection) {
        try {
            // Read schema.sql from resources
            String schema;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql")
                    ))) {
                schema = reader.lines().collect(Collectors.joining("\n"));
            }

            // Execute schema
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(schema);
                logger.info("Database schema initialized successfully");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize database schema", e);
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }
} 