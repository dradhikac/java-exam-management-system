package cli.commands;

import service.CondonationService;
import java.util.Scanner;

public class CondonationCommands {

    private CondonationService condonationService = new CondonationService();
    private Scanner sc = new Scanner(System.in);

    public void condonationMenu() {

        while (true) {
            System.out.println("\n===== Condonation Approval =====");
            System.out.println("1. Approve Condonation");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int ch = Integer.parseInt(sc.nextLine());

            if (ch == 1) approveFlow();
            else return;
        }
    }

    private void approveFlow() {

        System.out.print("Student USN: ");
        String usn = sc.nextLine();

        System.out.print("Subject Code: ");
        String subjectCode = sc.nextLine();

        System.out.print("Condonation Reason: ");
        String reason = sc.nextLine();

        condonationService.approveCondonation(usn, subjectCode, reason);
    }
}
