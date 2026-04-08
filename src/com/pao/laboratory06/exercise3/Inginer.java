package com.pao.laboratory06.exercise3;

/**
 * Inginer extinde Angajat si implementeaza PlataOnline si Comparable<Inginer>.
 * Ordinea naturala este dupa nume (alfabetic).
 */
public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
        this.autentificat = false;
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
        // Simulare autentificare simpla
        this.autentificat = true;
        System.out.println("[Inginer] " + this + " autentificat cu succes.");
    }

    @Override
    public double consultareSold() {
        System.out.printf("[Inginer] %s - sold: %.2f lei%n", this, sold);
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (sold < suma) {
            System.out.printf("[Inginer] %s - sold insuficient pentru plata de %.2f lei.%n", this, suma);
            return false;
        }
        sold -= suma;
        System.out.printf("[Inginer] %s - plata de %.2f lei efectuata. Sold ramas: %.2f lei.%n", this, suma, sold);
        return true;
    }

    // --- Comparable: ordine naturala dupa nume (alfabetic) ---

    @Override
    public int compareTo(Inginer other) {
        return this.nume.compareTo(other.nume);
    }

    @Override
    public String toString() {
        return "Inginer(" + nume + " " + prenume + ", salariu=" + salariu + ")";
    }
}
