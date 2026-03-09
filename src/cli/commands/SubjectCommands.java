package cli.commands;

import service.SubjectService;
import java.util.Scanner;

public class SubjectCommands {

    private SubjectService subjectService = new SubjectService();
    private Scanner sc = new Scanner(System.in);

    public void subjectMenu() {

        while (true) {
            System.out.println("\n===== Subject Management =====");
            System.out.println("1. Add Subject");
            System.out.println("2. List Subjects");
            System.out.println("3. Import Subjects from CSV");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Enter a valid number!");
                continue;
            }

            switch (ch) {
                case 1 -> addSubjectFlow();
                case 2 -> subjectService.listSubjects();
                case 3 -> importCSVFlow();
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- ADD SUBJECT ----------------
    private void addSubjectFlow() {

        System.out.print("Subject Code: ");
        String code = sc.nextLine();

        System.out.print("Subject Name: ");
        String name = sc.nextLine();

        System.out.print("Semester (number): ");
        int semester = Integer.parseInt(sc.nextLine());

        System.out.print("Category (BSC / IPCC / PCC): ");
        String category = sc.nextLine().toUpperCase();

        subjectService.addSubject(code, name, semester, category);
    }

    // ---------------- CSV IMPORT ----------------
    private void importCSVFlow() {

        System.out.print("Enter CSV file path: ");
        String path = sc.nextLine();

        subjectService.importSubjectsFromCSV(path);
    }
}
