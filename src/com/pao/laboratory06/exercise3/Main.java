package com.pao.laboratory06.exercise3;

import java.util.Arrays;

/**
 * Demonstratie pentru platforma de plati online.
 * Acopera: sortare, polimorfism, interfete, SMS, constante financiare, edge cases.
 */
public class Main {
    public static void main(String[] args) {

        System.out.println("=== 1. Constante financiare (enum) ===");
        // Afisarea constantelor din enum prin getter
        for (ConstanteFinanciare c : ConstanteFinanciare.values()) {
            System.out.printf("  %s = %.2f%n", c, c.getValoare());
        }
        System.out.println();

        // -------------------------------------------------------------------
        System.out.println("=== 2. Creare ingineri + sortare ===");

        Inginer[] ingineri = {
            new Inginer("Popescu", "Ion",    "0721000001", 8000, 15000),
            new Inginer("Andrei",  "Maria",  "0721000002", 12000, 20000),
            new Inginer("Zaharia", "Vlad",   null,         9500,  8000),
            new Inginer("Barbu",   "Ana",    "0721000004", 6500,  5000),
        };

        // Sortare naturala (Comparable): dupa nume alfabetic
        Arrays.sort(ingineri);
        System.out.println("Sortare naturala (dupa nume):");
        for (Inginer i : ingineri) System.out.println("  " + i);

        System.out.println();

        // Sortare cu ComparatorInginerSalariu: descrescator dupa salariu
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println("Sortare cu Comparator (descrescator dupa salariu):");
        for (Inginer i : ingineri) System.out.println("  " + i);

        System.out.println();

        // -------------------------------------------------------------------
        System.out.println("=== 3. Acces la Inginer prin referinta PlataOnline ===");
        // Prin referinta de tip interfata, avem acces DOAR la metodele din PlataOnline
        PlataOnline clientOnline = new Inginer("Ionescu", "Petre", "0731999999", 7000, 3000);
        clientOnline.autentificare("petre.ionescu", "parola123");
        clientOnline.consultareSold();
        boolean platit = clientOnline.efectuarePlata(1200);
        System.out.println("  Plata 1200 lei reusita: " + platit);
        clientOnline.consultareSold();
        // Nu putem apela metode specifice Inginer (ex: getSalariu()) prin referinta PlataOnline
        // clientOnline.getSalariu(); // eroare de compilare
        System.out.println();

        // -------------------------------------------------------------------
        System.out.println("=== 4. PersoanaJuridica prin referinta PlataOnlineSMS ===");

        // Persoana juridica cu telefon valid
        PlataOnlineSMS firma1 = new PersoanaJuridica("TechCorp", "SRL", "0740123456", 50000);
        firma1.autentificare("techcorp", "secret99");
        firma1.consultareSold();
        firma1.efectuarePlata(10000);
        boolean sms1 = firma1.trimiteSMS("Confirmare plata 10000 lei.");
        boolean sms2 = firma1.trimiteSMS("Sold actualizat: 40000 lei.");
        System.out.println("  SMS1 trimis: " + sms1 + " | SMS2 trimis: " + sms2);

        // Afisare SMS-uri inregistrate (cast necesar pentru lista interna)
        System.out.println("  SMS-uri trimise de " + firma1 + ":");
        ((PersoanaJuridica) firma1).getSmsTrimise()
                .forEach(m -> System.out.println("    - " + m));

        System.out.println();

        // -------------------------------------------------------------------
        System.out.println("=== 5. Edge cases ===");

        // 5a. PersoanaJuridica fara telefon — SMS returneaza false
        PlataOnlineSMS firmaFaraTel = new PersoanaJuridica("MicroFirm", "SRL", null, 20000);
        boolean smsNok = firmaFaraTel.trimiteSMS("Test fara telefon");
        System.out.println("  SMS fara telefon reusit: " + smsNok); // false

        // 5b. SMS cu mesaj null sau gol — returneaza false
        PlataOnlineSMS firma2 = new PersoanaJuridica("GlobalTrade", "SRL", "0740000000", 30000);
        boolean smsNull = firma2.trimiteSMS(null);
        boolean smsGol  = firma2.trimiteSMS("   ");
        System.out.println("  SMS mesaj null reusit: " + smsNull + " | SMS mesaj gol reusit: " + smsGol);

        // 5c. Autentificare cu user null — arunca IllegalArgumentException
        System.out.print("  Autentificare cu user null: ");
        try {
            clientOnline.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException prins: " + e.getMessage());
        }

        // 5d. Autentificare cu parola goala — arunca IllegalArgumentException
        System.out.print("  Autentificare cu parola goala: ");
        try {
            clientOnline.autentificare("user", "");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException prins: " + e.getMessage());
        }

        // 5e. Plata cu sold insuficient
        PlataOnline saracacios = new Inginer("Doe", "John", "0700000000", 4000, 100);
        saracacios.autentificare("john.doe", "pass");
        boolean plataNok = saracacios.efectuarePlata(5000);
        System.out.println("  Plata 5000 lei (sold=100): " + plataNok); // false

        // 5f. trimiteSMS apelata pe un obiect fara capabilitate SMS (ex: Inginer)
        //     Inginer implementeaza PlataOnline, nu PlataOnlineSMS.
        //     Daca incercam cast fortat, primim ClassCastException la runtime:
        System.out.print("  Cast Inginer -> PlataOnlineSMS: ");
        try {
            PlataOnlineSMS impostor = (PlataOnlineSMS) clientOnline; // clientOnline e Inginer
            impostor.trimiteSMS("nu ar trebui sa mearga");
        } catch (ClassCastException e) {
            System.out.println("ClassCastException prins (Inginer nu e PlataOnlineSMS).");
        }

        System.out.println();
        System.out.println("=== Demo complet. ===");
    }
}
