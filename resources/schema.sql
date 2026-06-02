-- Banking App — Schema SQL (SQLite)
-- Run this script to (re)create the database schema from scratch.

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS account_status_log;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS users;

-- ─── Users ────────────────────────────────────────────────────────────────────
CREATE TABLE users (
    id          TEXT    PRIMARY KEY,
    role        TEXT    NOT NULL,          -- PERS | LLC | CORP | INST | AUTH
    email       TEXT    NOT NULL,
    phone       TEXT,
    address     TEXT,
    -- Natural person fields
    first_name  TEXT,
    last_name   TEXT,
    ssn         TEXT,
    -- Company / Institution fields
    company_name    TEXT,
    urc             TEXT,
    industry        TEXT,
    social_capital  REAL,
    associate_count INTEGER,
    institution_name TEXT
);

-- ─── Accounts ─────────────────────────────────────────────────────────────────
CREATE TABLE accounts (
    iban            TEXT    PRIMARY KEY,
    account_type    TEXT    NOT NULL,      -- CHECKING | SAVINGS | CREDIT
    owner_id        TEXT    NOT NULL,
    currency        TEXT    NOT NULL,
    balance         REAL    NOT NULL DEFAULT 0.0,
    status          TEXT    NOT NULL DEFAULT 'ACTIVE',
    opened_at       TEXT    NOT NULL,
    -- CheckingAccount extras
    overdraft_limit     REAL,
    -- SavingsAccount extras
    interest_rate       REAL,
    maturity_date       TEXT,
    -- CreditAccount extras
    credit_limit            REAL,
    credit_interest_rate    REAL,
    billing_due_day         INTEGER,

    FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- ─── Transactions ─────────────────────────────────────────────────────────────
CREATE TABLE transactions (
    id              TEXT    PRIMARY KEY,
    type            TEXT    NOT NULL,      -- DEPOSIT | WITHDRAWAL | TRANSFER | PAYMENT
    from_iban       TEXT,
    to_iban         TEXT,
    amount          REAL    NOT NULL,
    fee             REAL    NOT NULL DEFAULT 0.0,
    currency        TEXT    NOT NULL,
    ts              TEXT    NOT NULL,

    FOREIGN KEY (from_iban) REFERENCES accounts(iban),
    FOREIGN KEY (to_iban)   REFERENCES accounts(iban)
);

-- ─── Account Status Log ────────────────────────────────────────────────────────
-- Tracks every status change (ACTIVE → FROZEN → CLOSED) on an account.
CREATE TABLE account_status_log (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    iban        TEXT    NOT NULL,
    old_status  TEXT    NOT NULL,
    new_status  TEXT    NOT NULL,
    changed_at  TEXT    NOT NULL,

    FOREIGN KEY (iban) REFERENCES accounts(iban)
);
