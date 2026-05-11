package com.pao.laboratory11.exercise1;

import java.util.Comparator;

public final class TransactionComparator {

    private TransactionComparator() {
    }

    public static final Comparator<Transaction> INSTANCE = Comparator.comparingInt(Transaction::getScore).reversed()
            .thenComparing((a, b) -> b.getAmount().compareTo(a.getAmount()))
            .thenComparing(Transaction::getDate)
            .thenComparingInt(Transaction::getId);
}
