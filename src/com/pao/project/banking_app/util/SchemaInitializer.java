package com.pao.project.banking_app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Utility class that reads and executes {@code schema.sql} against the
 * configured database, dropping and re-creating all tables.
 */
public class SchemaInitializer {

    private SchemaInitializer() {
    }

    public static void initialize() {
        String sql;
        try (InputStream in = SchemaInitializer.class.getClassLoader()
                .getResourceAsStream("schema.sql")) {
            if (in == null) {
                throw new RuntimeException("schema.sql not found on classpath.");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read schema.sql: " + e.getMessage(), e);
        }

        // Strip single-line comments (lines starting with --) before splitting
        String[] rawLines = sql.split("\n");
        StringBuilder cleaned = new StringBuilder();
        for (String line : rawLines) {
            String trimmedLine = line.strip();
            // skip pure comment lines
            if (trimmedLine.startsWith("--")) continue;
            // remove inline trailing comments
            int commentIdx = line.indexOf("--");
            if (commentIdx >= 0) {
                line = line.substring(0, commentIdx);
            }
            cleaned.append(line).append("\n");
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        // SQLite supports only one statement per execute() call; split on ";"
        String[] statements = cleaned.toString().split(";");
        try {
            for (String stmt : statements) {
                String trimmed = stmt.strip();
                if (!trimmed.isEmpty()) {
                    try (Statement s = conn.createStatement()) {
                        s.execute(trimmed);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Schema initialization failed: " + e.getMessage(), e);
        }
    }
}
