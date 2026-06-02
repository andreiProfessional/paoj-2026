package com.pao.project.banking_app.repository;

import com.pao.project.banking_app.model.account.Account;
import com.pao.project.banking_app.model.transaction.TransactionRecord;
import com.pao.project.banking_app.model.transaction.TransactionType;
import com.pao.project.banking_app.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC repository for {@link TransactionRecord} entities.
 *
 * <p>Because {@code TransactionRecord} is a Java record (immutable), the
 * {@link #update} method is a no-op — transactions are immutable by design.
 */
public class TransactionRepository implements Repository<TransactionRecord, String> {

    private final AccountRepository accountRepo;

    public TransactionRepository(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // ─── SAVE ──────────────────────────────────────────────────────────────────
    @Override
    public void save(TransactionRecord tr) {
        String sql = """
                INSERT INTO transactions (id, type, from_iban, to_iban, amount, fee, currency, ts)
                VALUES (?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, tr.id());
            ps.setString(2, tr.type().name());
            if (tr.fromAccount() != null)
                ps.setString(3, tr.fromAccount().getIban());
            else
                ps.setNull(3, Types.VARCHAR);
            if (tr.toAccount() != null)
                ps.setString(4, tr.toAccount().getIban());
            else
                ps.setNull(4, Types.VARCHAR);
            ps.setDouble(5, tr.amount());
            ps.setDouble(6, tr.fee());
            ps.setString(7, tr.currency());
            ps.setString(8, tr.timestamp().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("TransactionRepository.save failed: " + e.getMessage(), e);
        }
    }

    // ─── FIND BY ID ────────────────────────────────────────────────────────────
    @Override
    public Optional<TransactionRecord> findById(String id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("TransactionRepository.findById failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ─── FIND ALL ──────────────────────────────────────────────────────────────
    @Override
    public List<TransactionRecord> findAll() {
        List<TransactionRecord> result = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY ts";
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("TransactionRepository.findAll failed: " + e.getMessage(), e);
        }
        return result;
    }

    /** Transactions are immutable — update is intentionally a no-op. */
    @Override
    public void update(TransactionRecord entity) {
        // no-op: TransactionRecord is immutable by design
    }

    // ─── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("TransactionRepository.delete failed: " + e.getMessage(), e);
        }
    }

    // ─── FIND BY ACCOUNT ───────────────────────────────────────────────────────
    public List<TransactionRecord> findByAccount(String iban) {
        List<TransactionRecord> result = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE from_iban = ? OR to_iban = ? ORDER BY ts";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.setString(2, iban);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("TransactionRepository.findByAccount failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── JOIN QUERY 3: transactions with account owner info ───────────────────
    /**
     * Returns every transaction with the display names of the source and destination account owners.
     */
    public List<String> findAllWithOwnerNames() {
        List<String> result = new ArrayList<>();
        String sql = """
                SELECT t.id, t.type, t.amount, t.fee, t.currency, t.ts,
                       COALESCE(uf.first_name || ' ' || uf.last_name, uf.company_name, uf.institution_name, uf.id) AS from_owner,
                       COALESCE(ut.first_name || ' ' || ut.last_name, ut.company_name, ut.institution_name, ut.id) AS to_owner
                FROM transactions t
                LEFT JOIN accounts af ON t.from_iban = af.iban
                LEFT JOIN users   uf ON af.owner_id  = uf.id
                LEFT JOIN accounts at2 ON t.to_iban  = at2.iban
                LEFT JOIN users   ut ON at2.owner_id = ut.id
                ORDER BY t.ts
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String from = rs.getString("from_owner") != null ? rs.getString("from_owner") : "-";
                String to   = rs.getString("to_owner")   != null ? rs.getString("to_owner")   : "-";
                result.add(String.format("[%s] type=%-12s amount=%10.2f %s  from='%s'  to='%s'  ts=%s",
                        rs.getString("id").substring(0, 8) + "…",
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("currency"),
                        from, to,
                        rs.getString("ts").substring(0, 19)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("TransactionRepository.findAllWithOwnerNames failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── MAPPER ────────────────────────────────────────────────────────────────
    private TransactionRecord mapRow(ResultSet rs) throws SQLException {
        String fromIban = rs.getString("from_iban");
        String toIban   = rs.getString("to_iban");

        Account from = fromIban != null
                ? accountRepo.findById(fromIban).orElse(null) : null;
        Account to   = toIban   != null
                ? accountRepo.findById(toIban).orElse(null)   : null;

        return new TransactionRecord(
                rs.getString("id"),
                from, to,
                rs.getDouble("amount"),
                rs.getDouble("fee"),
                rs.getString("currency"),
                TransactionType.valueOf(rs.getString("type")),
                LocalDateTime.parse(rs.getString("ts")));
    }
}
