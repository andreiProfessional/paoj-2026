package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAnComparator implements Comparator<Carte> {

    @Override
    public int compare(Carte a, Carte b) {
        return Integer.compare(a.getAn(), b.getAn());
    }
}
