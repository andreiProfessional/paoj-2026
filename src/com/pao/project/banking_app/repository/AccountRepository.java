package com.pao.project.banking_app.repository;

import com.pao.project.banking_app.model.account.*;
import com.pao.project.banking_app.model.user.User;
import com.pao.project.banking_app.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC repository for {@link Account} entities.
 * All subtypes (CheckingAccount, SavingsAccount, CreditAccount) are stored
 * in a single "accounts" table using a discriminator column {@code account_type}.
 */
public class AccountRepository implements Repository<Account, String> {

    private final UserRepository userRepo;

    public AccountRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // ─── SAVE ──────────────────────────────────────────────────────────────────
    @Override
    public void save(Account account) {
        String sql = """
                INSERT INTO accounts (iban, account_type, owner_id, currency, balance, status, opened_at,
                                      overdraft_limit, interest_rate, maturity_date,
                                      credit_limit, credit_interest_rate, billing_due_day)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, account.getIban());
            ps.setString(2, account.getAccountType().name());
            ps.setString(3, account.getOwner().getId());
            ps.setString(4, account.getCurrency());
            ps.setDouble(5, account.getBalance());
            ps.setString(6, account.getStatus().name());
            ps.setString(7, account.getOpenedAt().toString());

            if (account instanceof CheckingAccount ca) {
                ps.setDouble(8, ca.getOverdraftLimit());
                ps.setNull(9, Types.REAL); ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.REAL); ps.setNull(12, Types.REAL); ps.setNull(13, Types.INTEGER);

            } else if (account instanceof SavingsAccount sa) {
                ps.setNull(8, Types.REAL);
                ps.setDouble(9, sa.getInterestRate());
                ps.setString(10, sa.getMaturityDate() != null ? sa.getMaturityDate().toString() : null);
                ps.setNull(11, Types.REAL); ps.setNull(12, Types.REAL); ps.setNull(13, Types.INTEGER);

            } else if (account instanceof CreditAccount cra) {
                ps.setNull(8, Types.REAL); ps.setNull(9, Types.REAL); ps.setNull(10, Types.VARCHAR);
                ps.setDouble(11, cra.getCreditLimit());
                ps.setDouble(12, cra.getInterestRate());
                ps.setInt(13, cra.getBillingDueDayOfMonth());

            } else {
                for (int i = 8; i <= 13; i++) ps.setNull(i, Types.NULL);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("AccountRepository.save failed: " + e.getMessage(), e);
        }
    }

    // ─── FIND BY ID ────────────────────────────────────────────────────────────
    @Override
    public Optional<Account> findById(String iban) {
        String sql = "SELECT * FROM accounts WHERE iban = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, iban);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("AccountRepository.findById failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ─── FIND ALL ──────────────────────────────────────────────────────────────
    @Override
    public List<Account> findAll() {
        List<Account> result = new ArrayList<>();
        String sql = "SELECT * FROM accounts";
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("AccountRepository.findAll failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── UPDATE (balance + status) ─────────────────────────────────────────────
    @Override
    public void update(Account account) {
        String sql = "UPDATE accounts SET balance=?, status=?, currency=? WHERE iban=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setDouble(1, account.getBalance());
            ps.setString(2, account.getStatus().name());
            ps.setString(3, account.getCurrency());
            ps.setString(4, account.getIban());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("AccountRepository.update failed: " + e.getMessage(), e);
        }
    }

    // ─── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM accounts WHERE iban = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, iban);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("AccountRepository.delete failed: " + e.getMessage(), e);
        }
    }

    // ─── JOIN QUERY 2: accounts with owner display name ───────────────────────
    /**
     * Returns every account together with its owner's display name.
     */
    public List<String> findAllWithOwnerName() {
        List<String> result = new ArrayList<>();
        String sql = """
                SELECT a.iban, a.account_type, a.balance, a.currency, a.status,
                       COALESCE(u.first_name || ' ' || u.last_name, u.company_name, u.institution_name, u.id) AS owner_name
                FROM accounts a
                JOIN users u ON a.owner_id = u.id
                ORDER BY a.iban
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(String.format("Account[%s] type=%-10s balance=%10.2f %s  status=%-7s owner='%s'",
                        rs.getString("iban"),
                        rs.getString("account_type"),
                        rs.getDouble("balance"),
                        rs.getString("currency"),
                        rs.getString("status"),
                        rs.getString("owner_name")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("AccountRepository.findAllWithOwnerName failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── MAPPER ────────────────────────────────────────────────────────────────
    private Account mapRow(ResultSet rs) throws SQLException {
        String iban  = rs.getString("iban");
        String type  = rs.getString("account_type");
        String ownerId = rs.getString("owner_id");
        String currency = rs.getString("currency");
        double balance  = rs.getDouble("balance");
        AccountStatus status = AccountStatus.valueOf(rs.getString("status"));
        LocalDate openedAt = LocalDate.parse(rs.getString("opened_at"));

        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found for account " + iban));

        Account account = switch (type) {
            case "CHECKING" -> new CheckingAccount(iban, owner, currency, balance,
                    rs.getDouble("overdraft_limit"));
            case "SAVINGS"  -> new SavingsAccount(iban, owner, currency, balance,
                    rs.getDouble("interest_rate"),
                    rs.getString("maturity_date") != null
                            ? LocalDate.parse(rs.getString("maturity_date")) : null);
            case "CREDIT"   -> new CreditAccount(iban, owner, currency,
                    rs.getDouble("credit_limit"),
                    rs.getDouble("credit_interest_rate"),
                    rs.getInt("billing_due_day"));
            default -> throw new IllegalStateException("Unknown account type: " + type);
        };
        account.setStatus(status);
        return account;
    }
}
