package cli.commands;

import service.ExamScheduleService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class ExamScheduleCommands {

    private final ExamScheduleService service = new ExamScheduleService();
    private final Scanner sc = new Scanner(System.in);

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    public void menu() {

        while (true) {
            System.out.println("\n===== Exam Schedule Module =====");
            System.out.println("1. Add Exam Schedule (Manual)");
            System.out.println("2. Import Exam Schedule (CSV)");
            System.out.println("3. Export Exam Schedule (CSV)");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("❌ Invalid choice");
                continue;
            }

            switch (choice) {
                case 1 -> addManual();
                case 2 -> importCSV();
                case 3 -> exportCSV();
                case 4 -> { return; }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }

    // ==============================
    // MANUAL EXAM SCHEDULE ENTRY
    // ==============================
    private void addManual() {

        System.out.print("Subject Code: ");
        String code = sc.nextLine().trim();

        System.out.print("Exam Session (Jan-Feb 2026): ");
        String session = sc.nextLine().trim();

        System.out.print("Exam Date (YYYY-MM-DD): ");
        LocalDate date;
        try {
            date = LocalDate.parse(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("❌ Invalid date format");
            return;
        }

        System.out.print("Start Time (hh:mm AM/PM): ");
        LocalTime start;
        try {
            start = LocalTime.parse(sc.nextLine().trim().toUpperCase(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid start time. Example: 9:30 AM");
            return;
        }

        System.out.print("End Time (hh:mm AM/PM): ");
        LocalTime end;
        try {
            end = LocalTime.parse(sc.nextLine().trim().toUpperCase(), TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid end time. Example: 12:30 PM");
            return;
        }

        if (end.isBefore(start) || end.equals(start)) {
            System.out.println("❌ End time must be after start time");
            return;
        }

        service.setSchedule(code, session, date, start, end);
    }

    // ==============================
    // IMPORT FROM CSV
    // ==============================
    private void importCSV() {
        System.out.print("Enter CSV file path: ");
        String path = sc.nextLine().trim();
        service.importFromCSV(path);
    }

    // ==============================
    // EXPORT TO CSV
    // ==============================
    private void exportCSV() {
        System.out.print("Enter Exam Session: ");
        String session = sc.nextLine().trim();

        System.out.print("Enter output CSV file path: ");
        String path = sc.nextLine().trim();

        service.exportToCSV(session, path);
    }
}
