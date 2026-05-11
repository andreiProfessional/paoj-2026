package com.pao.laboratory11.exercise3;

import com.pao.laboratory11.exercise1.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        List<Transaction> data = List.of(
                new Transaction(1, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Transaction(2, new BigDecimal("5500.00"), LocalDate.of(2026, 5, 3), "RU", "WEB"),
                new Transaction(3, new BigDecimal("300.00"), LocalDate.of(2026, 5, 5), "NL", "ATM"),
                new Transaction(4, new BigDecimal("5500.00"), LocalDate.of(2026, 5, 7), "NG", "CRYPTO"), // tie with tx2
                new Transaction(5, new BigDecimal("90.00"), LocalDate.of(2026, 6, 1), "RO", "POS"),
                new Transaction(6, new BigDecimal("7000.00"), LocalDate.of(2026, 6, 2), "CN", "WEB"),
                new Transaction(7, new BigDecimal("450.00"), LocalDate.of(2026, 6, 10), "BR", "APP"),
                new Transaction(8, new BigDecimal("300.00"), LocalDate.of(2026, 7, 1), "RO", "ATM"), // tie with tx3
                new Transaction(9, new BigDecimal("120.00"), LocalDate.of(2026, 7, 15), "UA", "MOBILE"),
                new Transaction(10, new BigDecimal("2000.00"), LocalDate.of(2026, 8, 1), "RO", "WEB"));

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(5));

        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("  SNAPSHOT ANALYTICS DEMO");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.printf("  Total transactions : %d%n", snap.getTransactionCount());
        System.out.printf("  Total amount       : %.2f%n", snap.getTotalAmount());
        System.out.println();

        System.out.println("── Query 1: Top-5 transactions by amount ─────────────");
        snap.getTopTransactions().forEach(tx -> System.out.printf("  [%2d] %s  country=%-3s channel=%-8s  %s%n",
                tx.getId(),
                String.format("%9.2f", tx.getAmount()),
                tx.getCountry(),
                tx.getChannel(),
                tx.getVerdict() + " score=" + tx.getScore()));
        System.out.println();

        System.out.println("── Query 2: Count by country ─────────────────────────");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("  %-3s  %d tx   total=%9.2f%n",
                        e.getKey(), e.getValue(),
                        snap.getTotalByCountry().get(e.getKey())));
        System.out.println();

        System.out.println("── Query 3: Channel ranking by total amount ──────────");
        snap.getTotalByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("  %-8s  total=%9.2f   count=%d%n",
                        e.getKey(), e.getValue(),
                        snap.getCountByChannel().get(e.getKey())));
        System.out.println();

        System.out.println("── Query 4: Flagged transactions in top-5 ────────────");
        long flaggedInTop = snap.getTopTransactions().stream()
                .filter(Transaction::isFlagged)
                .count();
        snap.getTopTransactions().stream()
                .filter(Transaction::isFlagged)
                .forEach(tx -> System.out.printf("  [%2d] FLAG  amount=%9.2f  channel=%s%n",
                        tx.getId(), tx.getAmount(), tx.getChannel()));
        System.out.printf("  (%d flagged out of top-5)%n", flaggedInTop);
        System.out.println();

        System.out.println("── Immutability check ────────────────────────────────");
        try {
            snap.getCountByCountry().put("HACK", 999L);
            System.out.println("  [FAIL] Map was mutable — this should not happen!");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [OK] countByCountry is immutable (UnsupportedOperationException thrown)");
        }
        try {
            snap.getTopTransactions().clear();
            System.out.println("  [FAIL] List was mutable — this should not happen!");
        } catch (UnsupportedOperationException e) {
            System.out.println("  [OK] topTransactions is immutable (UnsupportedOperationException thrown)");
        }
        System.out.println("══════════════════════════════════════════════════════");
    }
}
