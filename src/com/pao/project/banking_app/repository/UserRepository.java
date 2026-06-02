package com.pao.project.banking_app.repository;

import com.pao.project.banking_app.model.user.*;
import com.pao.project.banking_app.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC repository for {@link User} entities (all subtypes stored in a single table).
 * All SQL uses PreparedStatement; all resources are closed via try-with-resources.
 */
public class UserRepository implements Repository<User, String> {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    // ─── SAVE ──────────────────────────────────────────────────────────────────
    @Override
    public void save(User user) {
        String sql = """
                INSERT INTO users (id, role, email, phone, address,
                                   first_name, last_name, ssn,
                                   company_name, urc, industry, social_capital, associate_count,
                                   institution_name)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getRole().name());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());

            if (user instanceof Person p) {
                ps.setString(6, p.getFirstName());
                ps.setString(7, p.getLastName());
                ps.setString(8, p.getSsn());
                ps.setNull(9,  Types.VARCHAR); ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR); ps.setNull(12, Types.REAL);
                ps.setNull(13, Types.INTEGER); ps.setNull(14, Types.VARCHAR);

            } else if (user instanceof LimitedLiabilityCompany llc) {
                ps.setNull(6, Types.VARCHAR); ps.setNull(7, Types.VARCHAR); ps.setNull(8, Types.VARCHAR);
                ps.setString(9, llc.getCompanyName());
                ps.setString(10, llc.getUrc());
                ps.setString(11, llc.getIndustry());
                ps.setDouble(12, llc.getSocialCapital());
                ps.setInt(13, llc.getAssociateCount());
                ps.setNull(14, Types.VARCHAR);

            } else if (user instanceof Company c) {
                ps.setNull(6, Types.VARCHAR); ps.setNull(7, Types.VARCHAR); ps.setNull(8, Types.VARCHAR);
                ps.setString(9, c.getCompanyName());
                ps.setString(10, c.getUrc());
                ps.setString(11, c.getIndustry());
                ps.setNull(12, Types.REAL); ps.setNull(13, Types.INTEGER); ps.setNull(14, Types.VARCHAR);

            } else if (user instanceof Institution inst) {
                ps.setNull(6, Types.VARCHAR); ps.setNull(7, Types.VARCHAR); ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR); ps.setNull(10, Types.VARCHAR); ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.REAL); ps.setNull(13, Types.INTEGER);
                ps.setString(14, inst.getInstitutionName());

            } else {
                for (int i = 6; i <= 14; i++) ps.setNull(i, Types.VARCHAR);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.save failed: " + e.getMessage(), e);
        }
    }

    // ─── FIND BY ID ────────────────────────────────────────────────────────────
    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.findById failed: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ─── FIND ALL ──────────────────────────────────────────────────────────────
    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.findAll failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── UPDATE ────────────────────────────────────────────────────────────────
    @Override
    public void update(User user) {
        String sql = "UPDATE users SET email=?, phone=?, address=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.update failed: " + e.getMessage(), e);
        }
    }

    // ─── DELETE ────────────────────────────────────────────────────────────────
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.delete failed: " + e.getMessage(), e);
        }
    }

    // ─── JOIN QUERY 1: users with their account count ─────────────────────────
    /**
     * Returns a summary of every user showing how many accounts they hold.
     */
    public List<String> findUsersWithAccountCount() {
        List<String> result = new ArrayList<>();
        String sql = """
                SELECT u.id, COALESCE(u.first_name || ' ' || u.last_name, u.company_name, u.institution_name, u.id) AS display_name,
                       u.role,
                       COUNT(a.iban) AS account_count
                FROM users u
                LEFT JOIN accounts a ON a.owner_id = u.id
                GROUP BY u.id
                ORDER BY account_count DESC
                """;
        try (PreparedStatement ps = conn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(String.format("User[%s / %s] role=%s  accounts=%d",
                        rs.getString("id").substring(0, 8) + "…",
                        rs.getString("display_name"),
                        rs.getString("role"),
                        rs.getInt("account_count")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("UserRepository.findUsersWithAccountCount failed: " + e.getMessage(), e);
        }
        return result;
    }

    // ─── MAPPER ────────────────────────────────────────────────────────────────
    private User mapRow(ResultSet rs) throws SQLException {
        String role    = rs.getString("role");
        String email   = rs.getString("email");
        String phone   = rs.getString("phone");
        String address = rs.getString("address");

        return switch (role) {
            case "PERS" -> {
                Person p = new Person(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("ssn"),
                        email, phone, address);
                yield p;
            }
            case "LLC" -> {
                LimitedLiabilityCompany llc = new LimitedLiabilityCompany(
                        rs.getString("company_name"),
                        rs.getString("urc"),
                        rs.getString("industry"),
                        rs.getDouble("social_capital"),
                        rs.getInt("associate_count"),
                        email, phone, address);
                yield llc;
            }
            default -> throw new IllegalStateException("Unknown user role: " + role);
        };
    }
}
