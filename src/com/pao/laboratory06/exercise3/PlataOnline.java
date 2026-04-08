package com.pao.laboratory06.exercise3;

/**
 * Interfata de baza pentru operatii de plata online.
 */
public interface PlataOnline {
    /**
     * Autentifica utilizatorul. Arunca IllegalArgumentException daca user sau parola sunt null/goale.
     */
    void autentificare(String user, String parola);

    /**
     * Returneaza soldul curent al contului.
     */
    double consultareSold();

    /**
     * Efectueaza o plata. Returneaza true daca plata a reusit, false altfel.
     */
    boolean efectuarePlata(double suma);
}
