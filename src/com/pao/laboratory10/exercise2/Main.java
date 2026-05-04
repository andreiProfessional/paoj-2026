package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Tranzactie> lista = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            TipTranzactie tip = TipTranzactie.valueOf(parts[3]);
            lista.add(new Tranzactie(id, suma, data, tip));
        }

        Comparator<Tranzactie> bySuma = Comparator.comparingDouble(Tranzactie::getSuma);

        while (scanner.hasNextLine()) {
            String linie = scanner.nextLine().trim();
            if (linie.isEmpty())
                continue;

            String[] parts = linie.split("\\s+");
            String comanda = parts[0];

            switch (comanda) {
                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();
                    for (Tranzactie t : lista) {
                        ids.add(t.getId());
                    }
                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                }
                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> raport = new TreeMap<>();
                    for (Tranzactie t : lista) {
                        String luna = t.getData().substring(0, 7);
                        raport.putIfAbsent(luna, new double[] { 0.0, 0.0 });
                        if (t.getTip() == TipTranzactie.CREDIT) {
                            raport.get(luna)[0] += t.getSuma();
                        } else {
                            raport.get(luna)[1] += t.getSuma();
                        }
                    }
                    for (Map.Entry<String, double[]> entry : raport.entrySet()) {
                        System.out.printf("%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                entry.getKey(), entry.getValue()[0], entry.getValue()[1]);
                    }
                    break;
                }
                case "TOP": {
                    int topN = Integer.parseInt(parts[1]);
                    List<Tranzactie> copie = new ArrayList<>(lista);
                    copie.sort(bySuma.reversed());
                    System.out.println("Top " + topN + ":");
                    for (Tranzactie t : copie.subList(0, topN)) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_ASC": {
                    Collections.sort(lista, bySuma);
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SORT_DESC": {
                    Collections.sort(lista, bySuma.reversed());
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "REVERSE": {
                    Collections.reverse(lista);
                    for (Tranzactie t : lista) {
                        System.out.println(t);
                    }
                    break;
                }
                case "MIN_MAX": {
                    System.out.println("MIN: " + Collections.min(lista, bySuma));
                    System.out.println("MAX: " + Collections.max(lista, bySuma));
                    break;
                }
                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : lista) {
                            lista.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }
                    break;
                }
                default:
                    break;
            }
        }

        scanner.close();
    }
}
