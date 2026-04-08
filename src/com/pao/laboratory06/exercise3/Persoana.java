package com.pao.laboratory06.exercise3;

/**
 * Clasa abstracta de baza pentru orice persoana din sistem.
 * telefon poate fi null sau gol (persoana fara nr. de telefon inregistrat).
 */
public abstract class Persoana {
    protected String nume;
    protected String prenume;
    protected String telefon; // poate fi null sau gol

    public Persoana(String nume, String prenume, String telefon) {
        this.nume = nume;
        this.prenume = prenume;
        this.telefon = telefon;
    }

    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public String getTelefon() { return telefon; }

    @Override
    public String toString() {
        return nume + " " + prenume;
    }
}
