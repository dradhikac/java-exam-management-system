package cli.commands;

import service.StudentService;
import java.util.List;
import java.util.Scanner;
import model.Student;

public class StudentCommands {

    private final StudentService studentService = new StudentService();
    private final Scanner sc = new Scanner(System.in);

    public void studentMenu() {
        while (true) {

            System.out.println("\n=== Student Menu ===");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Update Student");
            System.out.println("4. Import Students from CSV");
            System.out.println("5. Search Student");
            System.out.println("6. Export Students to CSV");
            System.out.println("7. Back");
            System.out.print("Enter choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number!");
                continue;
            }

            switch (choice) {
                case 1: addStudentFlow(); break;
                case 2: studentService.listStudents(); break;
                case 3: updateStudentFlow(); break;
                case 4: importCSVFlow(); break;
                case 5: searchStudentFlow(); break;
                case 6: exportCSVFlow(); break;
                case 7: return; // go back
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ================================================================
    // ADD STUDENT
    // ================================================================
    private void addStudentFlow() {

        System.out.print("Full Name: ");
        String name = sc.nextLine();

        System.out.print("USN (or press Enter to skip): ");
        String usn = sc.nextLine();
        if (usn.trim().isEmpty()) usn = null;

        System.out.print("Temporary Roll No (or press Enter to skip): ");
        String temp = sc.nextLine();
        if (temp.trim().isEmpty()) temp = null;

        int sem = -1;
        while (true) {
            System.out.print("Semester: ");
            try {
                sem = Integer.parseInt(sc.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer for semester.");
            }
        }

        System.out.print("Department Code (CSE/ECE/ISE/etc): ");
        String dept = sc.nextLine();

        // -------------------------
        // DEBUG LOGS
        // -------------------------
        System.out.println("\n[DEBUG] Collected Add Student Input:");
        System.out.println("Name = " + name);
        System.out.println("USN = " + usn);
        System.out.println("Temp Roll = " + temp);
        System.out.println("Semester = " + sem);
        System.out.println("Department = " + dept);
        System.out.println("[DEBUG] Calling studentService.addStudent()");
        // -------------------------

        studentService.addStudent(name, usn, temp, sem, dept);

        System.out.println("[DEBUG] addStudentFlow() completed.");
    }

    // ================================================================
    // UPDATE STUDENT
    // ================================================================
    private void updateStudentFlow() {

        System.out.print("Enter Student ID to update: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        Student existing = studentService.getStudentById(id);

        if (existing == null) {
            System.out.println("No student found with ID: " + id);
            return;
        }

        System.out.println("\n=== Current Details ===");
        printStudent(existing);

        System.out.println("\nEnter new details (leave blank to keep old value):");

        System.out.print("New Name: ");
        String name = sc.nextLine();
        if (name.trim().isEmpty()) name = existing.getFullName();

        System.out.print("New USN: ");
        String usn = sc.nextLine();
        if (usn.trim().isEmpty()) usn = existing.getUsn();

        System.out.print("New Temp Roll: ");
        String temp = sc.nextLine();
        if (temp.trim().isEmpty()) temp = existing.getTempRoll();

        System.out.print("New Semester: ");
        String semInput = sc.nextLine();
        int sem;
        if (semInput.trim().isEmpty()) {
            sem = existing.getSemester();
        } else {
            try {
                sem = Integer.parseInt(semInput.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid semester input. Keeping existing value.");
                sem = existing.getSemester();
            }
        }

        System.out.print("New Department: ");
        String dept = sc.nextLine();
        if (dept.trim().isEmpty()) dept = existing.getDepartment();

        Student updated = new Student(
                id,
                name,
                usn,
                temp,
                sem,
                dept, false
        );

        System.out.println("[DEBUG] Calling studentService.updateStudent()");
        studentService.updateStudent(updated);
    }

    // ================================================================
    // IMPORT CSV
    // ================================================================
    private void importCSVFlow() {
        System.out.print("Enter path to Students.csv file: ");
        String path = sc.nextLine();

        System.out.println("[DEBUG] Importing CSV from: " + path);
        studentService.importStudentsFromCSV(path);
    }

    // ================================================================
    // SEARCH STUDENT
    // ================================================================
    private void searchStudentFlow() {

        System.out.println("\nSearch by:");
        System.out.println("1. USN");
        System.out.println("2. Temp Roll No");
        System.out.println("3. Name");
        System.out.print("Enter choice: ");

        int c;
        try {
            c = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        switch (c) {

            case 1:
                System.out.print("Enter USN: ");
                String u = sc.nextLine();
                Student s1 = studentService.getByUSN(u);
                if (s1 != null) printStudent(s1);
                else System.out.println("No student found!");
                break;

            case 2:
                System.out.print("Enter Temp Roll: ");
                String r = sc.nextLine();
                Student s2 = studentService.getByTempRoll(r);
                if (s2 != null) printStudent(s2);
                else System.out.println("No student found!");
                break;

            case 3:
                System.out.print("Enter name (partial ok): ");
                String n = sc.nextLine();
                List<Student> list = studentService.searchByName(n);

                if (list.isEmpty()) {
                    System.out.println("No matches found.");
                } else {
                    for (Student st : list) {
                        printStudent(st);
                    }
                }
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // ================================================================
    // PRINT STUDENT
    // ================================================================
    private void printStudent(Student s) {
        System.out.println("\nID: " + s.getId());
        System.out.println("Name: " + s.getFullName());
        System.out.println("USN: " + s.getUsn());
        System.out.println("Temp Roll: " + s.getTempRoll());
        System.out.println("Semester: " + s.getSemester());
        System.out.println("Department: " + s.getDepartment());
        System.out.println("-----------------------------------");
    }

    // ================================================================
    // EXPORT CSV
    // ================================================================
    private void exportCSVFlow() {
        System.out.print("Enter file path to export CSV (e.g., C:/students.csv): ");
        String path = sc.nextLine();

        System.out.println("[DEBUG] Exporting CSV to: " + path);
        studentService.exportStudentsToCSV(path);
    }
}
