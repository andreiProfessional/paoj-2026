package com.pao.laboratory01.comparators;

// EXERCITIU 1: cream in pachetul comparators o clasa Podcast cu durata (secunde, int) si titlu (string)
// dupa modelul AudioBook.java si Book.java, implementati:
// 1. toString — pentru afisare frumoasa
// 2. Comparable<Podcast> cu compareTo — sortare dupa titlu
// 3. un Comparator extern (PodcastLengthComparator) — sortare dupa durata
// 4. o metoda main in care cream cateva podcast-uri si le sortam in ambele moduri

public class Podcast implements Comparable<Podcast> {

    // atributele clasei
    private String title;
    private int durationInSeconds;

    // constructor cu ambele atribute
    public Podcast(String title, int durationInSeconds) {
        this.title = title;
        this.durationInSeconds = durationInSeconds;
    }

    // getter necesar pentru comparatorul extern
    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    // pentru afisare frumoasa
    @Override
    public String toString() {
        return "Podcast{" +
                "title='" + title + '\'' +
                ", durationInSeconds=" + durationInSeconds +
                '}';
    }

    // sortare naturala dupa titlu
    @Override
    public int compareTo(Podcast other) {
        if (other == null) return 1; // this > null
        if (this.title == null && other.title == null) return 0;
        if (this.title == null) return -1;
        if (other.title == null) return 1;
        return this.title.compareTo(other.title);
    }

    // Metoda main — codul final care trebuie sa functioneze dupa implementare.
    // Ruleaza-l ca sa verifici ca totul e corect!
    public static void main(String[] args) {
        // cream cateva podcast-uri
        Podcast[] podcasts = {
                new Podcast("Tech Talk", 2400),
                new Podcast("Arta Conversatiei", 3600),
                new Podcast("Mindset", 1800)
        };

        // 1. sortare naturala (compareTo) — dupa titlu
        java.util.Arrays.sort(podcasts);
        System.out.println("Sortate dupa titlu:");
        System.out.println(java.util.Arrays.toString(podcasts));

        // 2. sortare cu Comparator extern — dupa durata
        java.util.Arrays.sort(podcasts, new PodcastLengthComparator());
        System.out.println("Sortate dupa durata (crescator):");
        System.out.println(java.util.Arrays.toString(podcasts));

        // 3. sortare cu lambda — dupa durata descrescator
        java.util.Arrays.sort(podcasts,
                (p1, p2) -> Integer.compare(p2.getDurationInSeconds(), p1.getDurationInSeconds())
        );
        System.out.println("Sortate dupa durata (descrescator, lambda):");
        System.out.println(java.util.Arrays.toString(podcasts));
    }
}

// Comparator extern — sortare dupa durata (crescator)
class PodcastLengthComparator implements java.util.Comparator<Podcast> {
    @Override
    public int compare(Podcast p1, Podcast p2) {
        if (p1 == p2) return 0;
        if (p1 == null) return -1;
        if (p2 == null) return 1;
        return Integer.compare(p1.getDurationInSeconds(), p2.getDurationInSeconds());
    }
}
