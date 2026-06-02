package com.pao.project.banking_app;

import com.pao.project.banking_app.exception.AccountFrozenException;
import com.pao.project.banking_app.exception.InsufficientFundsException;
import com.pao.project.banking_app.exception.UserNotFoundException;
import com.pao.project.banking_app.model.account.*;
import com.pao.project.banking_app.model.transaction.TransactionRecord;
import com.pao.project.banking_app.model.user.*;
import com.pao.project.banking_app.repository.*;
import com.pao.project.banking_app.service.AccountService;
import com.pao.project.banking_app.service.AuditService;
import com.pao.project.banking_app.service.TransactionService;
import com.pao.project.banking_app.service.UserService;
import com.pao.project.banking_app.util.DatabaseConnection;
import com.pao.project.banking_app.util.SchemaInitializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // ─── In-memory services (Etapa I) ─────────────────────────────────────────
    private static final UserService userService = UserService.getInstance();
    private static final AccountService accountService = AccountService.getInstance();
    private static final TransactionService transactionService = TransactionService.getInstance();

    // ─── JDBC repositories (Etapa II) ─────────────────────────────────────────
    private static final UserRepository userRepo = new UserRepository();
    private static final AccountRepository accountRepo = new AccountRepository(userRepo);
    private static final TransactionRepository txRepo = new TransactionRepository(accountRepo);
    private static final AccountStatusLogRepository statusLogRepo = new AccountStatusLogRepository();

    // ─── Audit service (Etapa II) ─────────────────────────────────────────────
    private static final AuditService audit = AuditService.getInstance();

    // ─── Domain objects ───────────────────────────────────────────────────────
    private static Person alice;
    private static LimitedLiabilityCompany techCorp;
    private static CheckingAccount aliceChecking;
    private static SavingsAccount aliceSavings;
    private static CheckingAccount corpChecking;
    private static CreditAccount aliceCredit;

    public static void main(String[] args) {
        banner("Banking App — Full System Demo (Etapa I + II)");

        // ── Etapa II: initialise DB ────────────────────────────────────────────
        section("DB Init — Schema initialisation");
        SchemaInitializer.initialize();
        System.out.println("Schema initialised successfully.");

        // ── Etapa I actions (with audit + DB persistence) ─────────────────────
        action1_registerUsers();
        action2_openAccounts();
        action3_deposit();
        action4_withdraw();
        action5_transfer();
        action6_transactionHistory();
        action7_listAccountsByUser();
        action8_freezeCloseAccount();
        action9_searchUser();
        action10_removeUser();

        // ── Etapa II: advanced queries (JOIN) ─────────────────────────────────
        section("JOIN Query 1 — Users with account count");
        userRepo.findUsersWithAccountCount().forEach(System.out::println);

        section("JOIN Query 2 — Accounts with owner name");
        accountRepo.findAllWithOwnerName().forEach(System.out::println);

        section("JOIN Query 3 — Transactions with owner names");
        txRepo.findAllWithOwnerNames().forEach(System.out::println);

        // ── Status log (4th repository) ───────────────────────────────────────
        section("Account Status Log (4th repository) — all status changes");
        statusLogRepo.findAll().forEach(System.out::println);

        // ── Close DB connection cleanly ───────────────────────────────────────
        DatabaseConnection.getInstance().close();
        System.out.println("\nApplication finished. Check audit.csv for the action log.");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 1 — Register new users
    // ──────────────────────────────────────────────────────────────────────────
    private static void action1_registerUsers() {
        section("Action 1 — Register new users");
        audit.log("register_user");

        alice = new Person(
                "Alice", "Popescu", "1900101123456",
                "alice@example.com", "+40700000001", "Str. Lalelelor 1, Bucharest");

        techCorp = new LimitedLiabilityCompany(
                "TechCorp SRL", "J40/1234/2020", "IT Services",
                20000.0, 2,
                "office@techcorp.ro", "+40211234567", "Bd. Unirii 10, Bucharest");

        // In-memory
        userService.addUser(alice);
        userService.addUser(techCorp);

        // Persist to DB
        userRepo.save(alice);
        userRepo.save(techCorp);

        System.out.println("Registered: " + alice);
        System.out.println("Registered: " + techCorp);
        System.out.println("Total users (in-memory): " + userService.listAll().size());
        System.out.println("Total users (DB): " + userRepo.findAll().size());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 2 — Open accounts
    // ──────────────────────────────────────────────────────────────────────────
    private static void action2_openAccounts() {
        section("Action 2 — Open accounts");
        audit.log("open_account");

        aliceChecking = new CheckingAccount(
                "RO49RNCB0082044540100001", alice, "RON", 1000.0, 500.0);
        aliceSavings = new SavingsAccount(
                "RO49RNCB0082044540100002", alice, "RON",
                5000.0, 4.5, LocalDate.now().plusYears(1));
        aliceCredit = new CreditAccount(
                "RO49RNCB0082044540100003", alice, "RON",
                10000.0, 18.9, 25);
        corpChecking = new CheckingAccount(
                "RO49RNCB0082044540200001", techCorp, "RON", 50000.0);

        // In-memory
        accountService.openAccount(aliceChecking);
        accountService.openAccount(aliceSavings);
        accountService.openAccount(aliceCredit);
        accountService.openAccount(corpChecking);

        // Persist to DB
        accountRepo.save(aliceChecking);
        accountRepo.save(aliceSavings);
        accountRepo.save(aliceCredit);
        accountRepo.save(corpChecking);

        System.out.println("Opened: " + aliceChecking);
        System.out.println("Opened: " + aliceSavings);
        System.out.println("Opened: " + aliceCredit);
        System.out.println("Opened: " + corpChecking);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 3 — Deposit money
    // ──────────────────────────────────────────────────────────────────────────
    private static void action3_deposit() {
        section("Action 3 — Deposit money");
        audit.log("deposit");

        TransactionRecord t1 = accountService.deposit(aliceChecking, 2000.0, 0.0);
        TransactionRecord t2 = accountService.deposit(corpChecking, 15000.0, 0.0);

        txRepo.save(t1);
        txRepo.save(t2);
        // Sync updated balances back to DB
        accountRepo.update(aliceChecking);
        accountRepo.update(corpChecking);

        System.out.println("Deposited 2000 RON into Alice's checking: " + t1);
        System.out.println("Alice's checking balance: " + aliceChecking.getBalance() + " RON");
        System.out.println("Deposited 15000 RON into TechCorp checking: " + t2);
        System.out.println("TechCorp balance: " + corpChecking.getBalance() + " RON");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 4 — Withdraw money
    // ──────────────────────────────────────────────────────────────────────────
    private static void action4_withdraw() {
        section("Action 4 — Withdraw money");
        audit.log("withdraw");

        TransactionRecord t = accountService.withdraw(aliceChecking, 500.0, 2.5);
        txRepo.save(t);
        accountRepo.update(aliceChecking);

        System.out.println("Withdrew 500 RON from Alice's checking: " + t);
        System.out.println("Balance after withdrawal: " + aliceChecking.getBalance() + " RON");

        System.out.println("\n[Testing InsufficientFundsException]");
        try {
            accountService.withdraw(aliceChecking, 99999.0, 0.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 5 — Transfer between accounts (JDBC explicit transaction)
    // ──────────────────────────────────────────────────────────────────────────
    private static void action5_transfer() {
        section("Action 5 — Transfer between accounts (JDBC transaction)");
        audit.log("transfer");

        System.out.printf("Before transfer — Alice checking: %.2f RON, TechCorp: %.2f RON%n",
                aliceChecking.getBalance(), corpChecking.getBalance());

        // ── In-memory transfer ────────────────────────────────────────────────
        TransactionRecord tr = accountService.transfer(aliceChecking, corpChecking, 300.0, 1.5);

        // ── JDBC explicit transaction: save tx + update both account balances ──
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            try {
                // 1. Persist the transaction record
                String sqlTx = """
                        INSERT INTO transactions (id, type, from_iban, to_iban, amount, fee, currency, ts)
                        VALUES (?,?,?,?,?,?,?,?)
                        """;
                try (PreparedStatement ps = conn.prepareStatement(sqlTx)) {
                    ps.setString(1, tr.id());
                    ps.setString(2, tr.type().name());
                    ps.setString(3, tr.fromAccount().getIban());
                    ps.setString(4, tr.toAccount().getIban());
                    ps.setDouble(5, tr.amount());
                    ps.setDouble(6, tr.fee());
                    ps.setString(7, tr.currency());
                    ps.setString(8, tr.timestamp().toString());
                    ps.executeUpdate();
                }

                // 2. Update sender balance
                String sqlFrom = "UPDATE accounts SET balance = ? WHERE iban = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlFrom)) {
                    ps.setDouble(1, aliceChecking.getBalance());
                    ps.setString(2, aliceChecking.getIban());
                    ps.executeUpdate();
                }

                // 3. Update receiver balance
                String sqlTo = "UPDATE accounts SET balance = ? WHERE iban = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlTo)) {
                    ps.setDouble(1, corpChecking.getBalance());
                    ps.setString(2, corpChecking.getIban());
                    ps.executeUpdate();
                }

                conn.commit();
                System.out.println("JDBC transaction committed successfully.");
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Transfer JDBC transaction rolled back: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not manage autoCommit: " + e.getMessage(), e);
        }

        System.out.println("Transfer: " + tr);
        System.out.printf("After transfer  — Alice checking: %.2f RON, TechCorp: %.2f RON%n",
                aliceChecking.getBalance(), corpChecking.getBalance());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 6 — Transaction history
    // ──────────────────────────────────────────────────────────────────────────
    private static void action6_transactionHistory() {
        section("Action 6 — Transaction history");
        audit.log("view_transaction_history");

        // In-memory
        List<TransactionRecord> history = transactionService.findByAccount(aliceChecking);
        System.out.println("In-memory history for Alice's checking (" + history.size() + " records):");
        history.forEach(t -> System.out.println("  " + t));

        // From DB
        List<TransactionRecord> dbHistory = txRepo.findByAccount(aliceChecking.getIban());
        System.out.println("DB history for Alice's checking (" + dbHistory.size() + " records):");
        dbHistory.forEach(t -> System.out.println("  " + t));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 7 — List all accounts for a user
    // ──────────────────────────────────────────────────────────────────────────
    private static void action7_listAccountsByUser() {
        section("Action 7 — List all accounts for a user");
        audit.log("list_accounts_by_user");

        List<Account> aliceAccounts = accountService.findByOwner(alice.getId());
        System.out.println("Alice's accounts (" + aliceAccounts.size() + "):");
        aliceAccounts.forEach(a -> System.out.println("  " + a));

        List<Account> corpAccounts = accountService.findByOwner(techCorp.getId());
        System.out.println("TechCorp's accounts (" + corpAccounts.size() + "):");
        corpAccounts.forEach(a -> System.out.println("  " + a));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 8 — Freeze and close accounts
    // ──────────────────────────────────────────────────────────────────────────
    private static void action8_freezeCloseAccount() {
        section("Action 8 — Freeze and close accounts");
        audit.log("freeze_close_account");

        // Log the status change BEFORE applying it (captures the old status)
        statusLogRepo.log(aliceCredit, AccountStatus.FROZEN);
        accountService.freezeAccount(aliceCredit.getIban());
        accountRepo.update(aliceCredit);
        System.out.println("Froze Alice's credit account. Status: " + aliceCredit.getStatus());

        System.out.println("\n[Testing AccountFrozenException]");
        try {
            accountService.deposit(aliceCredit, 100.0, 0.0);
        } catch (AccountFrozenException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        statusLogRepo.log(aliceCredit, AccountStatus.CLOSED);
        accountService.closeAccount(aliceCredit.getIban());
        accountRepo.update(aliceCredit);
        System.out.println("Closed Alice's credit account. Status: " + aliceCredit.getStatus());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 9 — Search users
    // ──────────────────────────────────────────────────────────────────────────
    private static void action9_searchUser() {
        section("Action 9 — Search users");
        audit.log("search_user");

        User found = userService.findById(alice.getId());
        System.out.println("Found by ID: " + found);

        List<User> byName = userService.findByName("popescu");
        System.out.println("Search 'popescu' → " + byName.size() + " result(s):");
        byName.forEach(u -> System.out.println("  " + u));

        List<User> byCorpName = userService.findByName("tech");
        System.out.println("Search 'tech' → " + byCorpName.size() + " result(s):");
        byCorpName.forEach(u -> System.out.println("  " + u));

        System.out.println("\n[Testing UserNotFoundException]");
        try {
            userService.findById("non-existent-id");
        } catch (UserNotFoundException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Action 10 — Remove a user from the system
    // ──────────────────────────────────────────────────────────────────────────
    private static void action10_removeUser() {
        section("Action 10 — Remove a user from the system");
        audit.log("remove_user");

        System.out.println("Removing TechCorp and all its accounts...");
        List<Account> corpAccounts = new ArrayList<>(accountService.findByOwner(techCorp.getId()));
        for (Account a : corpAccounts) {
            // Delete transactions that reference this account (FK constraint)
            List<TransactionRecord> txs = txRepo.findByAccount(a.getIban());
            for (TransactionRecord tx : txs) {
                txRepo.delete(tx.id());
            }
            accountRepo.delete(a.getIban());   // DB first (FK constraint)
            accountService.removeAccount(a.getIban());
            System.out.println("  Removed account: " + a.getIban());
        }

        userRepo.delete(techCorp.getId());
        userService.removeUser(techCorp.getId());
        System.out.println("Removed user: " + techCorp.getCompanyName());

        System.out.println("Remaining users (in-memory): " + userService.listAll().size());
        System.out.println("Remaining accounts (in-memory): " + accountService.listAll().size());
        System.out.println("Remaining users (DB): " + userRepo.findAll().size());
        System.out.println("Remaining accounts (DB): " + accountRepo.findAll().size());
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────
    private static void banner(String title) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.printf("║  %-48s║%n", title);
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("──────────────────────────────────────────────────");
        System.out.println("  " + title);
        System.out.println("──────────────────────────────────────────────────");
    }
}
