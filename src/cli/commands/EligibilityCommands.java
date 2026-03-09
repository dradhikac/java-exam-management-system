package cli.commands;

import service.EligibilityService;
import java.util.Scanner;

public class EligibilityCommands {

    private EligibilityService eligibilityService = new EligibilityService();
    private Scanner sc = new Scanner(System.in);

    public void eligibilityMenu() {

        while (true) {
            System.out.println("\n===== Eligibility Engine =====");
            System.out.println("1. Compute Eligibility");
            System.out.println("2. Export Eligible Students (CSV)");
            System.out.println("3. Export Not Eligible Students (CSV)");
            System.out.println("4. Export All Eligibility (CSV)");
            System.out.println("5. Export Eligibility by Subject (CSV)");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
                continue;
            }

            switch (ch) {
                case 1 -> eligibilityService.computeEligibility();
                case 2 -> exportCSV(true, false);
                case 3 -> exportCSV(false, true);
                case 4 -> exportCSV(false, false);
                case 5 -> exportBySubject();
                case 6 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private void exportCSV(boolean eligibleOnly, boolean nonEligibleOnly) {

        System.out.print("Enter output CSV file path: ");
        String path = sc.nextLine().trim();


        eligibilityService.exportEligibilityToCSV(
                path, eligibleOnly, nonEligibleOnly
        );
    }

    private void exportBySubject() {

        System.out.print("Enter Subject Code: ");
        String subjectCode = sc.nextLine().trim();

        System.out.print("Enter output CSV file path: ");
        String path = sc.nextLine();

        eligibilityService.exportEligibilityBySubjectToCSV(subjectCode, path);
    }
}
