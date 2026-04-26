package com.pao.project.banking_app;

import com.pao.project.banking_app.exception.AccountFrozenException;
import com.pao.project.banking_app.exception.InsufficientFundsException;
import com.pao.project.banking_app.exception.UserNotFoundException;
import com.pao.project.banking_app.model.account.*;
import com.pao.project.banking_app.model.transaction.TransactionRecord;
import com.pao.project.banking_app.model.user.*;
import com.pao.project.banking_app.service.AccountService;
import com.pao.project.banking_app.service.TransactionService;
import com.pao.project.banking_app.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final UserService userService = UserService.getInstance();
    private static final AccountService accountService = AccountService.getInstance();
    private static final TransactionService transactionService = TransactionService.getInstance();

    private static Person alice;
    private static LimitedLiabilityCompany techCorp;
    private static CheckingAccount aliceChecking;
    private static SavingsAccount aliceSavings;
    private static CheckingAccount corpChecking;
    private static CreditAccount aliceCredit;

    public static void main(String[] args) {
        banner("Banking App — Full System Demo");

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
    }

    private static void action1_registerUsers() {
        section("Action 1 — Register new users");

        alice = new Person(
                "Alice", "Popescu", "1900101123456",
                "alice@example.com", "+40700000001", "Str. Lalelelor 1, Bucharest");

        techCorp = new LimitedLiabilityCompany(
                "TechCorp SRL", "J40/1234/2020", "IT Services",
                20000.0, 2,
                "office@techcorp.ro", "+40211234567", "Bd. Unirii 10, Bucharest");

        userService.addUser(alice);
        userService.addUser(techCorp);

        System.out.println("Registered: " + alice);
        System.out.println("Registered: " + techCorp);
        System.out.println("Total users: " + userService.listAll().size());
    }

    private static void action2_openAccounts() {
        section("Action 2 — Open accounts");

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

        accountService.openAccount(aliceChecking);
        accountService.openAccount(aliceSavings);
        accountService.openAccount(aliceCredit);
        accountService.openAccount(corpChecking);

        System.out.println("Opened: " + aliceChecking);
        System.out.println("Opened: " + aliceSavings);
        System.out.println("Opened: " + aliceCredit);
        System.out.println("Opened: " + corpChecking);
    }

    private static void action3_deposit() {
        section("Action 3 — Deposit money");

        TransactionRecord t1 = accountService.deposit(aliceChecking, 2000.0, 0.0);
        TransactionRecord t2 = accountService.deposit(corpChecking, 15000.0, 0.0);

        System.out.println("Deposited 2000 RON into Alice's checking: " + t1);
        System.out.println("Alice's checking balance: " + aliceChecking.getBalance() + " RON");

        System.out.println("Deposited 15000 RON into TechCorp checking: " + t2);
        System.out.println("TechCorp balance: " + corpChecking.getBalance() + " RON");
    }

    private static void action4_withdraw() {
        section("Action 4 — Withdraw money");

        TransactionRecord t = accountService.withdraw(aliceChecking, 500.0, 2.5);
        System.out.println("Withdrew 500 RON from Alice's checking: " + t);
        System.out.println("Balance after withdrawal: " + aliceChecking.getBalance() + " RON");

        System.out.println("\n[Testing InsufficientFundsException]");
        try {
            accountService.withdraw(aliceChecking, 99999.0, 0.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }
    }

    private static void action5_transfer() {
        section("Action 5 — Transfer between accounts");

        System.out.printf("Before transfer — Alice checking: %.2f RON, TechCorp: %.2f RON%n",
                aliceChecking.getBalance(), corpChecking.getBalance());

        TransactionRecord t = accountService.transfer(aliceChecking, corpChecking, 300.0, 1.5);
        System.out.println("Transfer: " + t);

        System.out.printf("After transfer  — Alice checking: %.2f RON, TechCorp: %.2f RON%n",
                aliceChecking.getBalance(), corpChecking.getBalance());
    }

    private static void action6_transactionHistory() {
        section("Action 6 — Transaction history");

        List<TransactionRecord> history = transactionService.findByAccount(aliceChecking);
        System.out.println("Transaction history for Alice's checking account (" + history.size() + " records):");
        for (TransactionRecord t : history) {
            System.out.println("  " + t);
        }
    }

    private static void action7_listAccountsByUser() {
        section("Action 7 — List all accounts for a user");

        List<Account> aliceAccounts = accountService.findByOwner(alice.getId());
        System.out.println("Alice's accounts (" + aliceAccounts.size() + "):");
        for (Account a : aliceAccounts) {
            System.out.println("  " + a);
        }

        List<Account> corpAccounts = accountService.findByOwner(techCorp.getId());
        System.out.println("TechCorp's accounts (" + corpAccounts.size() + "):");
        for (Account a : corpAccounts) {
            System.out.println("  " + a);
        }
    }

    private static void action8_freezeCloseAccount() {
        section("Action 8 — Freeze and close accounts");

        accountService.freezeAccount(aliceCredit.getIban());
        System.out.println("Froze Alice's credit account. Status: " + aliceCredit.getStatus());

        System.out.println("\n[Testing AccountFrozenException]");
        try {
            accountService.deposit(aliceCredit, 100.0, 0.0);
        } catch (AccountFrozenException e) {
            System.out.println("Caught expected exception: " + e.getMessage());
        }

        accountService.closeAccount(aliceCredit.getIban());
        System.out.println("Closed Alice's credit account. Status: " + aliceCredit.getStatus());
    }

    private static void action9_searchUser() {
        section("Action 9 — Search users");
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

    private static void action10_removeUser() {
        section("Action 10 — Remove a user from the system");

        System.out.println("Removing TechCorp and all its accounts...");
        List<Account> corpAccounts = new ArrayList<>(accountService.findByOwner(techCorp.getId()));
        for (Account a : corpAccounts) {
            accountService.removeAccount(a.getIban());
            System.out.println("  Removed account: " + a.getIban());
        }

        userService.removeUser(techCorp.getId());
        System.out.println("Removed user: " + techCorp.getCompanyName());

        System.out.println("Remaining users: " + userService.listAll().size());
        System.out.println("Remaining accounts: " + accountService.listAll().size());
    }

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
