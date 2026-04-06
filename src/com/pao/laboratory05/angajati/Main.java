package com.pao.laboratory05.angajati;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AngajatService service = AngajatService.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            String optiune = scanner.nextLine().trim();

            switch (optiune) {
                case "1":
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine().trim();

                    System.out.print("Departament (nume): ");
                    String numeDept = scanner.nextLine().trim();

                    System.out.print("Departament (locatie): ");
                    String locatieDept = scanner.nextLine().trim();

                    System.out.print("Salariu: ");
                    double salariu = Double.parseDouble(scanner.nextLine().trim());

                    Departament departament = new Departament(numeDept, locatieDept);
                    Angajat angajat = new Angajat(nume, departament, salariu);
                    service.addAngajat(angajat);
                    break;

                case "2":
                    service.listBySalary();
                    break;

                case "3":
                    System.out.print("Departament: ");
                    String cautDept = scanner.nextLine().trim();
                    service.findByDepartament(cautDept);
                    break;

                case "0":
                    System.out.println("La revedere!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opțiune invalidă. Încearcă din nou.");
            }
        }
    }
}
