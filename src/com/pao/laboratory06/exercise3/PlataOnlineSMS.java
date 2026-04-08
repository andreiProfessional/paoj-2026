package com.pao.laboratory06.exercise3;

/**
 * Extinde PlataOnline cu capabilitate de confirmare prin SMS.
 */
public interface PlataOnlineSMS extends PlataOnline {
    /**
     * Trimite un SMS de confirmare.
     * Returneaza false daca clientul nu are numar de telefon valid sau daca mesajul e null/gol.
     * Daca este apelata pe o entitate fara capabilitate SMS, arunca UnsupportedOperationException.
     */
    boolean trimiteSMS(String mesaj);
}
