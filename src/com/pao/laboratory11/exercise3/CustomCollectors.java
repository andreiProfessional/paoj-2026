package com.pao.laboratory11.exercise3;

import com.pao.laboratory11.exercise1.Transaction;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CustomCollectors {

    private CustomCollectors() {
    }

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            BigDecimal totalAmount = BigDecimal.ZERO;
            long count = 0;
            final Map<String, Long> countByCountry = new HashMap<>();
            final Map<String, Long> countByChannel = new HashMap<>();
            final Map<String, BigDecimal> totalByCountry = new HashMap<>();
            final Map<String, BigDecimal> totalByChannel = new HashMap<>();
            final List<Transaction> all = new ArrayList<>();

            void accumulate(Transaction tx) {
                totalAmount = totalAmount.add(tx.getAmount());
                count++;
                countByCountry.merge(tx.getCountry(), 1L, Long::sum);
                countByChannel.merge(tx.getChannel(), 1L, Long::sum);
                totalByCountry.merge(tx.getCountry(), tx.getAmount(), BigDecimal::add);
                totalByChannel.merge(tx.getChannel(), tx.getAmount(), BigDecimal::add);
                all.add(tx);
            }

            Agg combine(Agg other) {
                other.all.forEach(this::accumulate);
                return this;
            }

            Snapshot finish() {
                List<Transaction> top = all.stream()
                        .sorted(Comparator.comparing(Transaction::getAmount).reversed()
                                .thenComparingInt(Transaction::getId))
                        .limit(topN)
                        .collect(Collectors.toList());

                return new Snapshot(totalAmount, count,
                        countByCountry, countByChannel,
                        totalByCountry, totalByChannel,
                        top);
            }
        }

        return Collector.of(
                Agg::new,
                Agg::accumulate,
                Agg::combine,
                Agg::finish,
                Collector.Characteristics.UNORDERED);
    }
}
