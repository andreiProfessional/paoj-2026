package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Transaction> txList = new ArrayList<>(n);
        List<String> accounts = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            BigDecimal amount = new BigDecimal(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);
            String country = parts[3];
            String channel = parts[4];
            String accountId = parts[5];

            txList.add(new Transaction(id, amount, date, country, channel));
            accounts.add(accountId);
        }

        Map<YearMonth, List<Integer>> byMonth = new HashMap<>();
        Map<String, List<Integer>> byAccount = new HashMap<>();
        Map<String, Long> channelCount = txList.stream()
                .collect(Collectors.groupingBy(Transaction::getChannel, Collectors.counting()));
        for (int i = 0; i < txList.size(); i++) {
            YearMonth ym = YearMonth.from(txList.get(i).getDate());
            byMonth.computeIfAbsent(ym, k -> new ArrayList<>()).add(i);
            byAccount.computeIfAbsent(accounts.get(i), k -> new ArrayList<>()).add(i);
        }

        int q = Integer.parseInt(sc.nextLine().trim());
        for (int i = 0; i < q; i++) {
            String line = sc.nextLine().trim();
            if (line.startsWith("REPORT_MONTH ")) {
                YearMonth ym = YearMonth.parse(line.substring(13).trim());
                List<Integer> idxs = byMonth.getOrDefault(ym, Collections.emptyList());
                BigDecimal total = idxs.stream()
                        .map(idx -> txList.get(idx).getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                sb.append("MONTH ").append(ym)
                        .append(" total=").append(String.format("%.2f", total))
                        .append(" count=").append(idxs.size()).append('\n');
            } else if (line.startsWith("REPORT_ACCOUNT ")) {
                String acct = line.substring(15).trim();
                List<Integer> idxs = byAccount.getOrDefault(acct, Collections.emptyList());
                BigDecimal total = idxs.stream()
                        .map(idx -> txList.get(idx).getAmount())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                sb.append("ACCOUNT ").append(acct)
                        .append(" total=").append(String.format("%.2f", total))
                        .append(" count=").append(idxs.size()).append('\n');
            } else if (line.startsWith("TOP_CHANNELS ")) {
                int k = Integer.parseInt(line.substring(13).trim());
                channelCount.entrySet().stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey()))
                        .limit(k)
                        .forEach(e -> sb.append(e.getKey()).append(' ').append(e.getValue()).append('\n'));
            } else {
                sb.append("ERR UNKNOWN_COMMAND\n");
            }
        }

        System.out.print(sb);
    }
}
