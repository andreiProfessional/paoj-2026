package com.pao.laboratory00;

import java.util.Scanner;

/**
 * Exercitiul 1
 *
 * Cititi de la tastatura un sir cu n elemente intregi.
 *
 * 1. Afisati elementele sirului in doua modalitati.
 * 2. Afisati media aritmetica a elementelor sirului.
 *
 */

public class MediaAritmetica {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introduceti numarul de elemente (n): ");
        int n = scanner.nextInt();

        int[] sir = new int[n];
        System.out.println("Introduceti cele " + n + " elemente:");
        for (int i = 0; i < n; i++) {
            sir[i] = scanner.nextInt();
        }

        // 1. Afisare in doua modalitati
        System.out.print("Modalitatea 1 (for clasic): ");
        for (int i = 0; i < sir.length; i++) {
            System.out.print(sir[i] + " ");
        }
        System.out.println();

        System.out.print("Modalitatea 2 (for-each): ");
        for (int element : sir) {
            System.out.print(element + " ");
        }
        System.out.println();

        // 2. Media aritmetica
        double suma = 0;
        for (int element : sir) {
            suma += element;
        }

        if (n > 0) {
            double media = suma / n;
            System.out.println("Media aritmetica a elementelor este: " + media);
        } else {
            System.out.println("Sirul nu are elemente, nu se poate calcula media.");
        }

        scanner.close();
    }
}
