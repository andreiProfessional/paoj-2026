package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {

    protected final String nume;
    protected final String client;
    protected final OrderState stare;

    protected Comanda(String nume, String client) {
        this.nume = nume;
        this.client = client;
        this.stare = OrderState.PLACED;
    }

    public String getNume() {
        return nume;
    }

    public String getClient() {
        return client;
    }

    public abstract String tip();

    public abstract double pretFinal();

    public abstract String descriere();

    public abstract String descriereScurta();
}
