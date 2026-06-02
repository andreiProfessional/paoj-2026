package com.pao.project.banking_app.repository;

import com.pao.project.banking_app.model.account.Account;
import com.pao.project.banking_app.model.account.AccountStatus;
import com.pao.project.banking_app.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tracks every status transition for an {@link Account} (ACTIVE → FROZEN → CLOSED).
 * Each row represents one status change event.
 *
 * <p>This is the 4th concrete {@link Repository} implementation required by Stage II.
 */
public class AccountStatusLogRepository implements Repository<AccountStatusLogRepository.StatusEntry, Integer> {

    /** Immutable record representing one status-change event. */
    public record StatusEntry(
            int id,
            String iban,
            AccountStatus oldStatus,
            AccountStatus newStatus,
            LocalDateTime changedAt) {

        @Override
        public String toString() {
            return String.format("StatusLog[id=%d  iban=%s  %s → %s  at=%s]",
                    id, iban, oldStatus, newStatus, changedAt.toString().substring(0, 19));
        }
    }

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // ─── LOG A CHANGE (save) ───────────────────────────────────────────────────
    @Override
    public void save(StatusEntry entry) {
        String sql = "INSERT INTO account_status_log (iban, old_status, new_status, changed_at) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, entry.iban());
            ps.setString(2, entry.oldStatus().name());
            ps.setString(3, entry.newStatus().name());
            ps.setString(4, entry.changedAt().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("AccountStatusLogRepository.save failed: " + e.getMessage(), e);
        }
    }

    /** Convenience method: log a status change directly from an Account object. */
    public void log(Account account, AccountStatus newStatus) {
        save(new StatusEntry(0, account.getIban(), account.getStatus(), newStatus, LocalDateTime.now()));
    }

    // ─── FIND BY ID ────────────────────────────────────────────────────────────
    @Override
    public Optional<StatusEntry> findById(Integer id) {
        String sql = "SELECT * FROM account_status_log WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("AccountStatusLogRepository.findById failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ─── FIND ALL ──────────────────────────────────────────────────────────────
    @Override
    public List<StatusEntry> findAll() {
        List<StatusEntry> result = new ArrayList<>();
        String sql = "SELECT * FROM account_status_log ORDER BY changed_at";
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("AccountStatusLogRepository.findAll failed: " + e.getMessage(), e);
        }
        return result;
    }

    /** Status log entries are immutable — update is a no-op. */
    @Override
    public void update(StatusEntry entity) {
        // no-op: audit log entries are never modified
    }

    // ─── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM account_status_log WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("AccountStatusLogRepository.delete failed: " + e.getMessage(), e);
        }
    }

    /** Find all status changes for a specific account IBAN. */
    public List<StatusEntry> findByIban(String iban) {
        List<StatusEntry> result = new ArrayList<>();
        String sql = "SELECT * FROM account_status_log WHERE iban = ? ORDER BY changed_at";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("AccountStatusLogRepository.findByIban failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── MAPPER ────────────────────────────────────────────────────────────────
    private StatusEntry mapRow(ResultSet rs) throws SQLException {
        return new StatusEntry(
                rs.getInt("id"),
                rs.getString("iban"),
                AccountStatus.valueOf(rs.getString("old_status")),
                AccountStatus.valueOf(rs.getString("new_status")),
                LocalDateTime.parse(rs.getString("changed_at")));
    }
}
