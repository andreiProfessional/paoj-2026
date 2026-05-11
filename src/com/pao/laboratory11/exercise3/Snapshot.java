package com.pao.laboratory11.exercise3;

import com.pao.laboratory11.exercise1.Transaction;

import java.math.BigDecimal;
import java.util.*;

public final class Snapshot {

    private final BigDecimal totalAmount;
    private final long transactionCount;
    private final Map<String, Long> countByCountry;
    private final Map<String, Long> countByChannel;
    private final Map<String, BigDecimal> totalByCountry;
    private final Map<String, BigDecimal> totalByChannel;
    private final List<Transaction> topTransactions;

    Snapshot(
            BigDecimal totalAmount,
            long transactionCount,
            Map<String, Long> countByCountry,
            Map<String, Long> countByChannel,
            Map<String, BigDecimal> totalByCountry,
            Map<String, BigDecimal> totalByChannel,
            List<Transaction> topTransactions) {
        this.totalAmount = totalAmount;
        this.transactionCount = transactionCount;
        this.countByCountry = Collections.unmodifiableMap(new HashMap<>(countByCountry));
        this.countByChannel = Collections.unmodifiableMap(new HashMap<>(countByChannel));
        this.totalByCountry = Collections.unmodifiableMap(new HashMap<>(totalByCountry));
        this.totalByChannel = Collections.unmodifiableMap(new HashMap<>(totalByChannel));
        this.topTransactions = List.copyOf(topTransactions);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public long getTransactionCount() {
        return transactionCount;
    }

    public Map<String, Long> getCountByCountry() {
        return countByCountry;
    }

    public Map<String, Long> getCountByChannel() {
        return countByChannel;
    }

    public Map<String, BigDecimal> getTotalByCountry() {
        return totalByCountry;
    }

    public Map<String, BigDecimal> getTotalByChannel() {
        return totalByChannel;
    }

    public List<Transaction> getTopTransactions() {
        return topTransactions;
    }
}
