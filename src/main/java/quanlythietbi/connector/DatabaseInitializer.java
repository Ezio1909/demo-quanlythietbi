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
    private static final String SCHEMA_FILE_H2 = "schema-h2.sql";
    private static final String SCHEMA_FILE_MYSQL = "schema.sql";
    private static volatile boolean initialized = false;
    private static final Object lock = new Object();

    public static void initializeH2Database(Connection connection) throws SQLException {
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
                    try (Statement stmt = connection.createStatement()) {
                        String schema = new BufferedReader(
                            new InputStreamReader(DatabaseInitializer.class.getClassLoader().getResourceAsStream(SCHEMA_FILE_H2))
                        ).lines().collect(Collectors.joining("\n"));
                        for (String sql : schema.split(";")) {
                            if (!sql.trim().isEmpty()) {
                                stmt.execute(sql);
                            }
                        }
                        initialized = true;
                        logger.info("H2 database schema initialized successfully");
                    } catch (Exception e) {
                        throw new SQLException("Failed to initialize H2 database", e);
                    }
                }
            }
        }
    }

    public static void initializeMySQLDatabase(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(DatabaseInitializer.class.getClassLoader().getResourceAsStream(SCHEMA_FILE_MYSQL))
            );
            String delimiter = ";";
            StringBuilder sqlBlock = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("DELIMITER ")) {
                    delimiter = trimmed.substring("DELIMITER ".length());
                    continue;
                }
                sqlBlock.append(line).append("\n");
                if (sqlBlock.toString().endsWith(delimiter + "\n") || sqlBlock.toString().endsWith(delimiter + "\r\n")) {
                    int endIdx = sqlBlock.length() - delimiter.length() - 1; // -1 for \n
                    if (sqlBlock.toString().endsWith("\r\n")) {
                        endIdx--; // handle Windows line endings
                    }
                    String sql = sqlBlock.substring(0, endIdx).trim();
                    if (!sql.isEmpty()) {
                        stmt.execute(sql);
                    }
                    sqlBlock.setLength(0);
                }
            }
            // Execute any remaining block
            String sql = sqlBlock.toString().trim();
            if (!sql.isEmpty()) {
                stmt.execute(sql);
            }
            logger.info("MySQL database schema initialized successfully");
        } catch (Exception e) {
            throw new SQLException("Failed to initialize MySQL database", e);
        }
    }
} 