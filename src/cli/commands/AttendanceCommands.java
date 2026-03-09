package cli.commands;

import service.AttendanceService;
import service.SubjectService;
import model.Subject;

import java.util.Scanner;

public class AttendanceCommands {

    private AttendanceService attendanceService = new AttendanceService();
    private SubjectService subjectService = new SubjectService();
    private Scanner sc = new Scanner(System.in);

    public void attendanceMenu() {

        while (true) {
            System.out.println("\n===== Attendance Module =====");
            System.out.println("1. Enter Attendance");
            System.out.println("2. Import Attendance CSV");
            System.out.println("3. Export Attendance by Subject (CSV)");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int ch = Integer.parseInt(sc.nextLine());

            if (ch == 1) enterAttendanceFlow();
            else if (ch == 2) importCSVFlow();
            else if (ch == 3) exportBySubject();
            else return;
        }
    }

    private void enterAttendanceFlow() {

        System.out.print("Student USN: ");
        String usn = sc.nextLine();

        System.out.print("Subject Code: ");
        String subjectCode = sc.nextLine();

        Subject subject = subjectService.getByCode(subjectCode);
        String category = subject.getCategoryCode();

        if (category.equals("IPCC")) {

            System.out.print("Theory Attendance %: ");
            double theory = Double.parseDouble(sc.nextLine());

            System.out.print("Lab Attendance %: ");
            double lab = Double.parseDouble(sc.nextLine());

            attendanceService.enterAttendance(usn, subjectCode, theory, lab, null);

        } else {

            System.out.print("Attendance %: ");
            double percent = Double.parseDouble(sc.nextLine());

            attendanceService.enterAttendance(usn, subjectCode, null, null, percent);
        }
    }

    private void importCSVFlow() {

    System.out.print("Enter Attendance CSV file path: ");
    String path = sc.nextLine();

    attendanceService.importAttendanceFromCSV(path);
}

private void exportBySubject() {

    System.out.print("Enter Subject Code: ");
    String subjectCode = sc.nextLine();

    System.out.print("Enter output CSV file path: ");
    String path = sc.nextLine();

    attendanceService.exportAttendanceBySubject(subjectCode, path);
}

}
