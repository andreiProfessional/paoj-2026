package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {

    private Angajat[] angajati;

    private AngajatService() {
        angajati = new Angajat[0];
    }

    private static class Holder {
        private static final AngajatService INSTANCE = new AngajatService();
    }

    public static AngajatService getInstance() {
        return Holder.INSTANCE;
    }

    public void addAngajat(Angajat a) {
        Angajat[] newAngajati = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, newAngajati, 0, angajati.length);
        newAngajati[angajati.length] = a;
        angajati = newAngajati;
        System.out.println("Angajat adăugat: " + a.getNume());
    }

    public void printAll() {
        for (Angajat a : angajati) {
            System.out.println(a);
        }
    }

    public void listBySalary() {
        System.out.println("--- Angajați după salariu (descrescător) ---");
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);  // folosește compareTo (salariu descrescător)
        for (int i = 0; i < copy.length; i++) {
            System.out.println((i + 1) + ". " + copy[i]);
        }
    }

    public void findByDepartament(String numeDept) {
        System.out.println("--- Angajați din " + numeDept + " ---");
        boolean found = false;
        for (Angajat a : angajati) {
            if (a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Niciun angajat în departamentul: " + numeDept);
        }
    }
}
