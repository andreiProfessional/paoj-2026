package com.pao.laboratory05.biblioteca;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {

    private Carte[] carti;

    private BibliotecaService() {
        carti = new Carte[0];
    }

    // Holder pattern (Initialization-on-demand) — thread-safe, lazy
    private static class Holder {
        private static final BibliotecaService INSTANCE = new BibliotecaService();
    }

    public static BibliotecaService getInstance() {
        return Holder.INSTANCE;
    }

    public void addCarte(Carte carte) {
        Carte[] newCarti = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, newCarti, 0, carti.length);
        newCarti[carti.length] = carte;
        carti = newCarti;
        System.out.println("Carte adăugată: " + carte.getTitlu());
    }

    public void listSortedByRating() {
        Carte[] copy = carti.clone();
        Arrays.sort(copy);  // folosește compareTo din Comparable (rating descrescător)
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void listSortedBy(Comparator<Carte> comparator) {
        Carte[] copy = carti.clone();
        Arrays.sort(copy, comparator);
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }
}
