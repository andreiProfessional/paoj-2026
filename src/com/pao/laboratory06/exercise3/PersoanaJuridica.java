package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

/**
 * Persoana juridica (ex: firma) care implementeaza PlataOnlineSMS.
 * Pastreaza o lista cu toate SMS-urile trimise.
 */
public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private double sold;
    private final List<String> smsTrimise;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
        this.sold = sold;
        this.smsTrimise = new ArrayList<>();
    }

    public List<String> getSmsTrimise() {
        return smsTrimise;
    }

    // --- PlataOnline ---

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User-ul nu poate fi null sau gol.");
        }
        if (parola == null || parola.isBlank()) {
            throw new IllegalArgumentException("Parola nu poate fi null sau goala.");
        }
        System.out.println("[PJ] " + this + " autentificata cu succes.");
    }

    @Override
    public double consultareSold() {
        System.out.printf("[PJ] %s - sold: %.2f lei%n", this, sold);
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (sold < suma) {
            System.out.printf("[PJ] %s - sold insuficient pentru plata de %.2f lei.%n", this, suma);
            return false;
        }
        sold -= suma;
        System.out.printf("[PJ] %s - plata de %.2f lei efectuata. Sold ramas: %.2f lei.%n", this, suma, sold);
        return true;
    }

    // --- PlataOnlineSMS ---

    /**
     * Trimite un SMS. Returneaza false daca:
     * - mesajul este null sau gol
     * - telefonul este null sau gol (nu exista nr. de telefon)
     * Altfel, adauga mesajul in lista si returneaza true.
     */
    @Override
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isBlank()) {
            System.out.println("[PJ] " + this + " - SMS esuat: mesaj null sau gol.");
            return false;
        }
        if (telefon == null || telefon.isBlank()) {
            System.out.println("[PJ] " + this + " - SMS esuat: nu exista numar de telefon.");
            return false;
        }
        smsTrimise.add(mesaj);
        System.out.println("[PJ] " + this + " - SMS trimis la " + telefon + ": \"" + mesaj + "\"");
        return true;
    }

    @Override
    public String toString() {
        return "PersoanaJuridica(" + nume + " " + prenume + ")";
    }
}
