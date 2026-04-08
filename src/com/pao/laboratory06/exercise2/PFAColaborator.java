package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends Colaborator implements PersoanaFizica {
    private double cheltuieliLunare;

    // Salariu minim brut anual 2026: 4050 lei/lună × 12 = 48600 lei/an
    private static final double SALARIU_MINIM_BRUT_ANUAL = 4050 * 12;

    public PFAColaborator() {
        this.tip = TipColaborator.PFA;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        this.cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = (venitBrutLunar - cheltuieliLunare) * 12;

        // Impozit pe venit: 10%
        double impozit = 0.10 * venitNet;

        // CASS (sănătate): 10%
        double cass;
        double prag6 = 6 * SALARIU_MINIM_BRUT_ANUAL;   // 6 × 48600 = 291600
        double prag72 = 72 * SALARIU_MINIM_BRUT_ANUAL; // 72 × 48600
        if (venitNet < prag6) {
            cass = 0.10 * prag6;
        } else if (venitNet <= prag72) {
            cass = 0.10 * venitNet;
        } else {
            cass = 0.10 * prag72;
        }

        // CAS (pensie): 25%
        double cas;
        double prag12 = 12 * SALARIU_MINIM_BRUT_ANUAL; // 12 × 48600 = 583200
        double prag24 = 24 * SALARIU_MINIM_BRUT_ANUAL; // 24 × 48600
        if (venitNet < prag12) {
            cas = 0.0;
        } else if (venitNet <= prag24) {
            cas = 0.25 * prag12;
        } else {
            cas = 0.25 * prag24;
        }

        return venitNet - impozit - cass - cas;
    }

    @Override
    public String tipContract() {
        return "PFA";
    }
}
