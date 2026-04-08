package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends Colaborator implements PersoanaJuridica {
    private double cheltuieliLunare;

    public SRLColaborator() {
        this.tip = TipColaborator.SRL;
    }

    @Override
    public void citeste(Scanner in) {
        // SRL name can be two words: e.g. "SRLTech SRL"
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        // Impozit pe profit 16% → rămân 84%
        return (venitBrutLunar - cheltuieliLunare) * 12 * 0.84;
    }

    @Override
    public String tipContract() {
        return "SRL";
    }
}
