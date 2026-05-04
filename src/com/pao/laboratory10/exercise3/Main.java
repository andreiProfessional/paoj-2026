package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.*;

public class Main {

    public static void main(String[] args) {

        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1, 1500.00, "2024-01-10", TipTranzactie.CREDIT),
                new Tranzactie(2, 320.50, "2024-01-15", TipTranzactie.DEBIT),
                new Tranzactie(3, 850.00, "2024-01-28", TipTranzactie.DEBIT),
                new Tranzactie(4, 2200.00, "2024-02-03", TipTranzactie.CREDIT),
                new Tranzactie(5, 150.75, "2024-02-14", TipTranzactie.DEBIT),
                new Tranzactie(6, 430.00, "2024-02-20", TipTranzactie.CREDIT),
                new Tranzactie(7, 3000.00, "2024-03-05", TipTranzactie.CREDIT),
                new Tranzactie(8, 975.25, "2024-03-12", TipTranzactie.DEBIT),
                new Tranzactie(9, 260.00, "2024-03-19", TipTranzactie.DEBIT),
                new Tranzactie(10, 1800.00, "2024-03-27", TipTranzactie.CREDIT),
                new Tranzactie(11, 540.00, "2024-04-08", TipTranzactie.DEBIT),
                new Tranzactie(12, 1100.00, "2024-04-22", TipTranzactie.CREDIT));

        Map<Integer, String> contSursa = new HashMap<>();
        contSursa.put(1, "RO10AAAA");
        contSursa.put(2, "RO20BBBB");
        contSursa.put(3, "RO10AAAA");
        contSursa.put(4, "RO30CCCC");
        contSursa.put(5, "RO20BBBB");
        contSursa.put(6, "RO10AAAA");
        contSursa.put(7, "RO40DDDD");
        contSursa.put(8, "RO30CCCC");
        contSursa.put(9, "RO20BBBB");
        contSursa.put(10, "RO40DDDD");
        contSursa.put(11, "RO10AAAA");
        contSursa.put(12, "RO30CCCC");

        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println("\n=== 2. Total DEBIT procesat ===");
        double totalDebit = tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.DEBIT)
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", totalDebit);

        System.out.println("\n=== 3. Suma totala per luna ===");
        Map<String, Double> sumaLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)));
        sumaLuna.forEach((luna, suma) -> System.out.printf("%s: %.2f RON%n", luna, suma));

        System.out.println("\n=== 4. Top 3 tranzactii (suma descrescatoare) ===");
        System.out.println("Top 3:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(t -> contSursa.get(t.getId()))
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        System.out.println("\n=== 6. Suma medie per tranzactie ===");
        OptionalDouble medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average();
        System.out.printf("Suma medie: %.2f RON%n", medie.orElse(0.0));

        System.out.println("\n=== 7. Extras de cont sumar lunar ===");
        Map<String, List<Tranzactie>> grupeLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()));
        grupeLuna.forEach((luna, lista) -> {
            double total = lista.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lista.size(), total);
        });
    }
}
