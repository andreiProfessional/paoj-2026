package com.pao.laboratory06.exercise3;

/**
 * Extinde Persoana cu un salariu lunar.
 */
public abstract class Angajat extends Persoana {
    protected double salariu;

    public Angajat(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon);
        this.salariu = salariu;
    }

    public double getSalariu() { return salariu; }
}
