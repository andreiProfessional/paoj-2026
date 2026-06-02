package com.pao.project.banking_app.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton that provides a single reusable JDBC {@link Connection}.
 * Configuration is read from {@code resources/db.properties}.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("Cannot find db.properties on the classpath.");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties: " + e.getMessage(), e);
        }

        String url      = props.getProperty("db.url");
        String user     = props.getProperty("db.user", "");
        String password = props.getProperty("db.password", "");

        try {
            connection = DriverManager.getConnection(url, user, password);
            // Enable SQLite foreign-key enforcement (disabled by default in SQLite).
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot open database connection: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    /** Call once on application shutdown to release the connection. */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }
}
