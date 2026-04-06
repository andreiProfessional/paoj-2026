package com.pao.laboratory03.exercise.service;

import com.pao.laboratory03.exercise.exception.StudentNotFoundException;
import com.pao.laboratory03.exercise.model.Student;
import com.pao.laboratory03.exercise.model.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {

    private static StudentService instance;
    private final List<Student> students;

    private StudentService() {
        students = new ArrayList<>();
    }

    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }

    // a) Adaugă student — aruncă RuntimeException dacă numele există deja
    public void addStudent(String name, int age) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                throw new RuntimeException("Studentul '" + name + "' există deja în listă");
            }
        }
        students.add(new Student(name, age));
    }

    // b) Caută după nume — aruncă StudentNotFoundException dacă nu găsește
    public Student findByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        throw new StudentNotFoundException("Studentul '" + name + "' nu a fost găsit");
    }

    // c) Adaugă notă unui student
    public void addGrade(String studentName, Subject subject, double grade) {
        findByName(studentName).addGrade(subject, grade);
    }

    // d) Afișează toți studenții cu notele lor
    public void printAllStudents() {
        if (students.isEmpty()) {
            System.out.println("Nu există studenți înregistrați.");
            return;
        }
        int i = 1;
        for (Student s : students) {
            System.out.println(i++ + ". " + s);
            for (Map.Entry<Subject, Double> entry : s.getGrades().entrySet()) {
                System.out.printf("   %s = %.1f%n", entry.getKey().name(), entry.getValue());
            }
        }
    }

    // e) Top studenți sortați descrescător după medie
    public void printTopStudents() {
        System.out.println("=== Top studenți ===");
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingDouble(Student::getAverage).reversed());
        int i = 1;
        for (Student s : sorted) {
            System.out.printf("%d. %s — media: %.2f%n", i++, s.getName(), s.getAverage());
        }
    }

    // f) Media pe fiecare materie
    public Map<Subject, Double> getAveragePerSubject() {
        Map<Subject, Double> result = new HashMap<>();
        for (Subject subject : Subject.values()) {
            double sum = 0;
            int count = 0;
            for (Student s : students) {
                Double grade = s.getGrades().get(subject);
                if (grade != null) {
                    sum += grade;
                    count++;
                }
            }
            if (count > 0) {
                result.put(subject, sum / count);
            }
        }
        return result;
    }
}
