package cli.commands;

import service.FacultyAssignmentService;
import java.util.Scanner;

public class FacultyAssignmentCommands {

    private FacultyAssignmentService service = new FacultyAssignmentService();
    private Scanner sc = new Scanner(System.in);

    public void assignmentMenu() {

        while (true) {

            System.out.println("\n===== Faculty–Subject Assignment =====");
            System.out.println("1. Assign Faculty to Subject");
            System.out.println("2. List All Assignments");
            System.out.println("3. Remove Assignment");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1: assignFlow(); break;
                case 2: service.listAssignments(); break;
                case 3: removeFlow(); break;
                case 4: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private void assignFlow() {
        System.out.print("Enter Professor User ID: ");
        int userId = Integer.parseInt(sc.nextLine());

        System.out.print("Enter Subject ID: ");
        int subjectId = Integer.parseInt(sc.nextLine());

        service.assignFaculty(userId, subjectId);
    }

    private void removeFlow() {
        System.out.print("Enter Assignment ID to remove: ");
        int id = Integer.parseInt(sc.nextLine());

        service.removeAssignment(id);
    }
}
