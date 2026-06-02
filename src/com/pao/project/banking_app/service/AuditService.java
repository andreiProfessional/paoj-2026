package com.pao.project.banking_app.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe audit service that appends every action to {@code audit.csv}.
 *
 * <p>Format of each row: {@code action_name,timestamp}
 *
 * <p>The file is opened in <em>append</em> mode so that existing entries are
 * preserved across application restarts.
 *
 * <p>Thread safety is achieved via {@link ReentrantLock} on the write method.
 */
public class AuditService {

    private static final String AUDIT_FILE = "audit.csv";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static AuditService instance;
    private final ReentrantLock lock = new ReentrantLock();

    private AuditService() {
    }

    public static AuditService getInstance() {
        if (instance == null) {
            synchronized (AuditService.class) {
                if (instance == null) {
                    instance = new AuditService();
                }
            }
        }
        return instance;
    }

    /**
     * Logs {@code actionName} with the current timestamp to {@code audit.csv}.
     *
     * @param actionName short snake_case name of the action, e.g. {@code "register_user"}
     */
    public void log(String actionName) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        lock.lock();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.write(actionName + "," + timestamp);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("[AuditService] Failed to write audit log: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
