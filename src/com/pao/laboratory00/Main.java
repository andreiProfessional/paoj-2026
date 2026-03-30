package com.pao.laboratory00;

import java.util.Scanner;

/**
 * Rezolvați următoarele exerciții în fișierele
 * 1 MediaAritmetica.java și
 * 2 DiagonaleleMatricei.java din pachetul com.pao.loborator00.
 *
 * 1. Cititi de la tastatura un sir cu n elemente intregi.
 * Afisati sirul si media aritmetica a elementelor sirului.
 *
 * 2. Cititi de la tastatura o matrice de n ori n elemente REALE.
 * Afisati matricea in consola, apoi suma elementelor de pe diagonala principala
 * si produsul elementelor de pe diagonala secundara.
 *
 */

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Nu s-a oferit niciun argument.");
            System.out.println("Te rugam sa atasezi in linia de comanda '1' sau '2' pentru a alege aplicatia.\nExemplu de executie: java src/com/pao/laboratory00/Main.java 1");
            return;
        }

        String optiune = args[0];

        switch (optiune) {
            case "1":
                System.out.println("=> Se ruleaza Exercitiul 1 (MediaAritmetica)...");
                MediaAritmetica.main(args);
                break;
            case "2":
                System.out.println("=> Se ruleaza Exercitiul 2 (DiagonaleleMatricei)...");
                DiagonaleleMatricei.main(args);
                break;
            default:
                System.out.println("Argument invalid: '" + optiune + "'. Te rugam sa introduci valoarea 1 sau 2.");
        }
    }
}
