package cli.commands;

import seed.DefaultSubjectsLoader;
import java.util.Scanner;

public class DefaultSubjectCommands {

    private Scanner sc = new Scanner(System.in);
    private DefaultSubjectsLoader loader = new DefaultSubjectsLoader();

    public void loadMenu() {

        System.out.println("\nLoad default 8 subjects per semester?");
        System.out.print("Type YES to confirm: ");

        String c = sc.nextLine().toUpperCase();

        if (!c.equals("YES")) {
            System.out.println("Cancelled.");
            return;
        }

        loader.loadDefaults();

        System.out.println("Default subjects loaded successfully!");
    }
}
