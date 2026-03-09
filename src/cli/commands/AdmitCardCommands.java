package cli.commands;

import service.AdmitCardService;
import java.util.Scanner;

public class AdmitCardCommands {

    private AdmitCardService admitCardService = new AdmitCardService();
    private Scanner sc = new Scanner(System.in);

    public void admitCardMenu() {

        while (true) {
            System.out.println("\n===== Admit Card Module =====");
            System.out.println("1. Generate Admit Card (PDF)");
            System.out.println("2. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (choice) {
                case 1 -> generateAdmitCardFlow();
                case 2 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void generateAdmitCardFlow() {

        System.out.print("Enter Student USN: ");
        String usn = sc.nextLine().trim();

        admitCardService.generateAdmitCard(usn);
    }
}
