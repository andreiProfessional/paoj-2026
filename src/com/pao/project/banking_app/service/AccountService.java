package com.pao.project.banking_app.service;

import com.pao.project.banking_app.exception.AccountFrozenException;
import com.pao.project.banking_app.exception.AccountNotFoundException;
import com.pao.project.banking_app.exception.InsufficientFundsException;
import com.pao.project.banking_app.model.account.*;
import com.pao.project.banking_app.model.transaction.TransactionRecord;

import java.util.*;

public class AccountService {

    private static AccountService instance;

    private final TreeMap<String, Account> accountsByIban = new TreeMap<>();
    private final Map<String, List<Account>> accountsByUserId = new HashMap<>();

    private AccountService() {
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public void openAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        if (accountsByIban.containsKey(account.getIban())) {
            throw new IllegalArgumentException("Account with IBAN '" + account.getIban() + "' already exists.");
        }
        accountsByIban.put(account.getIban(), account);
        accountsByUserId
                .computeIfAbsent(account.getOwner().getId(), k -> new ArrayList<>())
                .add(account);
    }

    public void removeAccount(String iban) {
        Account account = findByIban(iban); // throws if absent
        accountsByIban.remove(iban);
        List<Account> ownerAccounts = accountsByUserId.get(account.getOwner().getId());
        if (ownerAccounts != null) {
            ownerAccounts.remove(account);
        }
    }

    public Account findByIban(String iban) {
        if (iban == null || iban.isBlank()) {
            throw new IllegalArgumentException("IBAN cannot be null or blank.");
        }
        Account account = accountsByIban.get(iban);
        if (account == null) {
            throw new AccountNotFoundException(iban);
        }
        return account;
    }

    public List<Account> findByOwner(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be null or blank.");
        }
        List<Account> result = accountsByUserId.getOrDefault(userId, Collections.emptyList());
        return Collections.unmodifiableList(result);
    }

    public List<Account> listAll() {
        return Collections.unmodifiableList(new ArrayList<>(accountsByIban.values()));
    }

    public void freezeAccount(String iban) {
        findByIban(iban).setStatus(AccountStatus.FROZEN);
    }

    public void closeAccount(String iban) {
        findByIban(iban).setStatus(AccountStatus.CLOSED);
    }

    public TransactionRecord deposit(Account account, double amount, double fee) {
        validateNotNull(account);
        validateActive(account);

        account.credit(amount);

        TransactionRecord tr = TransactionRecord.deposit(account, amount, fee, account.getCurrency());
        TransactionService.getInstance().record(tr);
        return tr;
    }

    public TransactionRecord withdraw(Account account, double amount, double fee) {
        validateNotNull(account);
        validateActive(account);
        validateSufficientFunds(account, amount + fee);

        account.debit(amount + fee);

        TransactionRecord tr = TransactionRecord.withdrawal(account, amount, fee, account.getCurrency());
        TransactionService.getInstance().record(tr);
        return tr;
    }

    public TransactionRecord transfer(Account from, Account to, double amount, double fee) {
        validateNotNull(from);
        validateNotNull(to);
        validateActive(from);
        validateActive(to);
        validateSufficientFunds(from, amount + fee);

        from.debit(amount + fee);
        to.credit(amount);

        TransactionRecord tr = TransactionRecord.transfer(from, to, amount, fee, from.getCurrency());
        TransactionService.getInstance().record(tr);
        return tr;
    }

    public TransactionRecord payment(Account from, Account to, double amount, double fee) {
        validateNotNull(from);
        validateNotNull(to);
        validateActive(from);
        validateActive(to);
        validateSufficientFunds(from, amount + fee);

        from.debit(amount + fee);
        to.credit(amount);

        TransactionRecord tr = TransactionRecord.payment(from, to, amount, fee, from.getCurrency());
        TransactionService.getInstance().record(tr);
        return tr;
    }

    private void validateNotNull(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
    }

    private void validateActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountFrozenException(account.getIban(), account.getStatus());
        }
    }

    private void validateSufficientFunds(Account account, double required) {
        double available;
        if (account instanceof CheckingAccount ca) {
            available = ca.getAvailableBalance();
        } else if (account instanceof CreditAccount ca) {
            available = ca.getAvailableCredit();
        } else {
            available = account.getBalance();
        }
        if (available < required) {
            throw new InsufficientFundsException(account.getIban(), required, available);
        }
    }
}
