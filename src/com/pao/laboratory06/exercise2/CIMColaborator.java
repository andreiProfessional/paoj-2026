package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends Colaborator implements PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        this.tip = TipColaborator.CIM;
    }

    @Override
    public void citeste(Scanner in) {
        this.nume = in.next();
        this.prenume = in.next();
        this.venitBrutLunar = in.nextDouble();
        String bonusStr = in.hasNext() ? in.next() : "NU";
        this.bonus = "DA".equalsIgnoreCase(bonusStr);
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = venitBrutLunar * 12 * 0.55;
        if (bonus) {
            venitNet *= 1.1;
        }
        return venitNet;
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }
}
