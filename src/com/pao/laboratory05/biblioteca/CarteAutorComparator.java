package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAutorComparator implements Comparator<Carte> {

    @Override
    public int compare(Carte a, Carte b) {
        return a.getAutor().compareTo(b.getAutor());
    }
}
