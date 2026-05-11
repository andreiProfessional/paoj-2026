package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Transaction {
    private final int id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String country;
    private final String channel;
    private final int score;
    private final boolean flagged;

    public Transaction(int id, BigDecimal amount, LocalDate date,
            String country, String channel) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
        this.score = RiskScorer.computeScore(this);
        this.flagged = Rules.FLAGGED_RULE.test(this);
    }

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getChannel() {
        return channel;
    }

    public int getScore() {
        return score;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public String getVerdict() {
        return flagged ? "FLAG" : "ALLOW";
    }
}
