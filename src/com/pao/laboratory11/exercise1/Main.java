package com.pao.laboratory11.exercise1;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Transaction> list = new ArrayList<>(n);
        Map<Integer, Transaction> byId = new HashMap<>(n * 2);

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] parts = line.split("\\s+");
            int id = Integer.parseInt(parts[0]);
            BigDecimal amount = new BigDecimal(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);
            String country = parts[3];
            String channel = parts[4];
            Transaction tx = new Transaction(id, amount, date, country, channel);
            list.add(tx);
            byId.put(id, tx);
        }

        List<Transaction> sorted = list.stream()
                .sorted(TransactionComparator.INSTANCE)
                .collect(Collectors.toList());
        List<Transaction> flagged = sorted.stream()
                .filter(Transaction::isFlagged)
                .collect(Collectors.toList());

        int q = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < q; i++) {
            String line = sc.nextLine().trim();
            if (line.startsWith("CHECK ")) {
                int id = Integer.parseInt(line.substring(6).trim());
                Transaction tx = byId.get(id);
                if (tx == null) {
                    sb.append("CHECK ").append(id).append(" => NOT_FOUND\n");
                } else {
                    sb.append("CHECK ").append(id)
                            .append(" => ").append(tx.getVerdict())
                            .append(" score=").append(tx.getScore()).append('\n');
                }
            } else if (line.equals("LIST_FLAGGED")) {
                if (flagged.isEmpty()) {
                    sb.append("NONE\n");
                } else {
                    for (Transaction tx : flagged) {
                        sb.append('[').append(tx.getId()).append("] FLAG score=")
                                .append(tx.getScore()).append('\n');
                    }
                }
            } else if (line.startsWith("TOP_RISK ")) {
                int k = Integer.parseInt(line.substring(9).trim());
                int limit = Math.min(k, sorted.size());
                for (int j = 0; j < limit; j++) {
                    Transaction tx = sorted.get(j);
                    sb.append('[').append(tx.getId()).append("] ")
                            .append(tx.getVerdict()).append(" score=")
                            .append(tx.getScore()).append('\n');
                }
            } else {
                sb.append("ERR UNKNOWN_COMMAND\n");
            }
        }

        System.out.print(sb);
    }
}
