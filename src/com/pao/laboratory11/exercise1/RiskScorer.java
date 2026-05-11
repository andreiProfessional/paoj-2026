package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class RiskScorer {

    private RiskScorer() {
    }

    public static int computeScore(Transaction tx) {
        return computeAmountScore(tx.getAmount())
                + computeCountryScore(tx.getCountry())
                + computeChannelScore(tx.getChannel())
                + computeBonus(tx);
    }

    static int computeAmountScore(BigDecimal amount) {
        int buckets = amount.divide(new BigDecimal("1000"), 0, RoundingMode.FLOOR).intValue();
        return Math.min(50, buckets * 10);
    }

    static int computeCountryScore(String country) {
        return Rules.RISKY_COUNTRIES.contains(country) ? 15 : 0;
    }

    static int computeChannelScore(String channel) {
        return switch (channel) {
            case "WEB" -> 45;
            case "APP" -> 40;
            case "CRYPTO" -> 45;
            case "POS" -> 25;
            case "ATM" -> 20;
            default -> 0;
        };
    }

    static int computeBonus(Transaction tx) {
        boolean highAmount = tx.getAmount().compareTo(Rules.AMOUNT_THRESHOLD) >= 0;
        boolean isCrypto = "CRYPTO".equals(tx.getChannel());
        return (highAmount && isCrypto) ? 5 : 0;
    }
}
