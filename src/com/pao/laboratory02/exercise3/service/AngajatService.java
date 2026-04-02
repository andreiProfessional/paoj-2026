package com.pao.laboratory02.exercise3.service;

import com.pao.laboratory02.exercise3.model.Angajat;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Completează cele 3 metode.
 * Folosește ArrayList — nu mai e nevoie de redimensionare manuală.
 */
public class AngajatService {
    private List<Angajat> angajati;

    public AngajatService() {
        this.angajati = new ArrayList<>();
    }

    public void addAngajat(Angajat a) {
        angajati.add(a);
        System.out.println("Angajat adăugat: " + a.getName());
    }

    public void listAll() {
        if (angajati.isEmpty()) {
            System.out.println("Nu exista angajati!");
            return;
        }
        for (int i = 0; i < angajati.size(); i++) {
            System.out.println((i + 1) + ". " + angajati.get(i));
        }
    }

    public double totalSalarii() {
        double sumaSalarii = 0;
        for (Angajat a : angajati) {
            sumaSalarii += a.salariuTotal();
        }
        return sumaSalarii;
    }
}
