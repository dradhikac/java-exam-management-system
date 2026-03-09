package cli;

import model.User;
import cli.commands.StudentCommands;
import cli.commands.SubjectCommands;
import cli.commands.UserCommands;
import cli.commands.AdmitCardCommands;
import cli.commands.AttendanceCommands;
import cli.commands.EligibilityCommands;
import cli.commands.ExamScheduleCommands;
import cli.commands.FacultyAssignmentCommands;
import cli.commands.InternalCommands;

import java.util.Scanner;

public class MenuRenderer {

    public void showMenu(User user) {

        switch (user.getRole()) {

            case "ADMIN":
                adminMenu();
                break;

            case "HOD":
                hodMenu();
                break;

            case "PROFESSOR":
                professorMenu();
                break;

            case "EXAM_STAFF":
                examStaffMenu();
                break;

            default:
                System.out.println("Unknown role!");
        }
    }

    private void adminMenu() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===== ADMIN MENU =====");
                System.out.println("1. Student Management");
                System.out.println("2. Subject Management");
                System.out.println("3. Faculty Assignments");
                System.out.println("4. Internal Marks");
                System.out.println("5. Attendance");
                System.out.println("6. Eligibility Engine");
                System.out.println("7. Admit Cards Generation");
                System.out.println("8. User Management");
                System.out.println("9. Exam Schedule (Set Date & Time)");
                System.out.println("10. Logout");
                System.out.print("Enter choice: ");

                int c = Integer.parseInt(sc.nextLine());
                switch (c) {
                    case 1: new StudentCommands().studentMenu(); break;
                    case 2: new SubjectCommands().subjectMenu(); break;
                    case 3: new FacultyAssignmentCommands().assignmentMenu(); break;
                    case 4: new InternalCommands().internalMenu(); break;
                    case 5: new AttendanceCommands().attendanceMenu(); break;
                    case 6: new EligibilityCommands().eligibilityMenu(); break;
                    case 7: new AdmitCardCommands().admitCardMenu(); break;
                    case 8: new UserCommands().userMenu(); break;
                    case 9: new ExamScheduleCommands().menu(); break;
                    case 10: return;
                    default: System.out.println("Invalid!");
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void hodMenu() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===== HOD MENU =====");
                System.out.println("1. Student Management");
                System.out.println("2. Faculty Assignments");
                System.out.println("3. Attendance");
                System.out.println("4. Logout");

                int c = Integer.parseInt(sc.nextLine());
                switch (c) {
                    case 1: new StudentCommands().studentMenu(); break;
                    case 2: new FacultyAssignmentCommands().assignmentMenu(); break;
                    case 3: new AttendanceCommands().attendanceMenu(); break;
                    case 4: return;
                    default: System.out.println("Invalid!");
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void professorMenu() {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===== PROFESSOR MENU =====");
                System.out.println("1. Internal Marks Entry");
                System.out.println("2. Attendance");
                System.out.println("3. Logout");

                int c = Integer.parseInt(sc.nextLine());
                switch (c) {
                    case 1: new InternalCommands().internalMenu(); break;
                    case 2: new AttendanceCommands().attendanceMenu(); break;
                    case 3: return;
                    default: System.out.println("Invalid!");
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch blocks
            e.printStackTrace();
        }
    }

    private void examStaffMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== EXAM STAFF MENU =====");
            System.out.println("1. Subject Management");
            System.out.println("2. Internal Marks (view)");
            System.out.println("3. Exam Schedule (Set Date & Time)");
            System.out.println("4. Admit Cards Generation");
            System.out.println("5. Logout");

            int c = Integer.parseInt(sc.nextLine());
            switch (c) {
                case 1: new SubjectCommands().subjectMenu(); break;
                case 2: new InternalCommands().internalMenu(); break;
                case 3: new ExamScheduleCommands().menu(); break;
                case 4: new AdmitCardCommands().admitCardMenu(); break;
                case 5: return;
                default: System.out.println("Invalid!");
            }
        }
    }

    
}
