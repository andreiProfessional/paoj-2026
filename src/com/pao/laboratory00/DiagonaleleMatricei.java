package com.pao.laboratory00;

import java.util.Scanner;

/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 * si produsul elementelor de pe diagonala secundara.
 *
 */

public class DiagonaleleMatricei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduceti dimensiunea matricei n (n x n): ");
        int n = scanner.nextInt();

        double[][] matrice = new double[n][n];

        System.out.println("Introduceti elementele reale ale matricei:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrice[i][j] = scanner.nextDouble();
            }
        }

        // 1. Afisati matricea in consola
        System.out.println("\nMatricea:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrice[i][j] + "\t");
            }
            System.out.println();
        }

        // 2. Diagonala principala (suma) si secundara (produs)
        double sumaPrincipala = 0;
        double produsSecundara = 1;

        if (n == 0) {
            produsSecundara = 0;
        }

        for (int i = 0; i < n; i++) {
            sumaPrincipala += matrice[i][i];
            produsSecundara *= matrice[i][n - 1 - i];
        }

        System.out.println("\nSuma pe diagonala principala: " + sumaPrincipala);
        System.out.println("Produsul pe diagonala secundara: " + produsSecundara);

        scanner.close();
    }
}
