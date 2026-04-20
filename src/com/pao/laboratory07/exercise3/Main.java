package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] t = sc.nextLine().trim().split(" ");
            switch (t[0]) {
                case "STANDARD" -> {
                    String nume = t[1];
                    double pret = Double.parseDouble(t[2]);
                    String client = t[3];
                    comenzi.add(new ComandaStandard(nume, pret, client));
                }
                case "DISCOUNTED" -> {
                    String nume = t[1];
                    double pret = Double.parseDouble(t[2]);
                    int discount = Integer.parseInt(t[3]);
                    String client = t[4];
                    comenzi.add(new ComandaRedusa(nume, pret, discount, client));
                }
                case "GIFT" -> {
                    String nume = t[1];
                    String client = t[2];
                    comenzi.add(new ComandaGratuita(nume, client));
                }
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }
        System.out.println();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(" ", 2);
            switch (parts[0]) {
                case "STATS" -> {
                    System.out.println("--- STATS ---");
                    Map<String, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    Comanda::tip,
                                    Collectors.averagingDouble(Comanda::pretFinal)));
                    for (String tip : List.of("STANDARD", "DISCOUNTED", "GIFT")) {
                        if (medii.containsKey(tip)) {
                            System.out.printf("%s: medie = %.2f lei%n", tip, medii.get(tip));
                        }
                    }
                }
                case "FILTER" -> {
                    double threshold = Double.parseDouble(parts[1]);
                    System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);
                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold)
                            .forEach(c -> System.out.println(c.descriereScurta()));
                }
                case "SORT" -> {
                    System.out.println("--- SORT (by client, then by pret) ---");
                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient)
                                    .thenComparingDouble(Comanda::pretFinal))
                            .forEach(c -> System.out.println(c.descriereScurta()));
                }
                case "SPECIAL" -> {
                    System.out.println("--- SPECIAL (discount > 15%) ---");
                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                            .forEach(c -> System.out.println(((ComandaRedusa) c).descriereSpeciala()));
                }
                case "QUIT" -> {
                    return;
                }
            }
        }
    }
}
