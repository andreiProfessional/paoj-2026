package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.util.Set;
import java.util.function.Predicate;

public final class Rules {

        public static final BigDecimal AMOUNT_THRESHOLD = new BigDecimal("5000.00");
        public static final Set<String> RISKY_COUNTRIES = Set.of("NG", "RU", "UA", "CN", "BR", "KP");
        public static final Set<String> SUSPICIOUS_CHANNELS = Set.of("CRYPTO");

        public static final Predicate<Transaction> amountOverThreshold = tx -> tx.getAmount()
                        .compareTo(AMOUNT_THRESHOLD) >= 0;
        public static final Predicate<Transaction> countryInRisk = tx -> RISKY_COUNTRIES.contains(tx.getCountry());
        public static final Predicate<Transaction> channelSuspicious = tx -> SUSPICIOUS_CHANNELS
                        .contains(tx.getChannel());

        public static final Predicate<Transaction> FLAGGED_RULE = amountOverThreshold.or(channelSuspicious);

        private Rules() {
        }
}
