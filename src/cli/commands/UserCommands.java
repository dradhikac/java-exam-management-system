package cli.commands;

import model.User;
import service.UserService;
import java.util.Scanner;

public class UserCommands {

    private UserService userService = new UserService();
    private Scanner sc = new Scanner(System.in);

    public void userMenu() {

        while (true) {
            System.out.println("\n===== ADMIN USER MANAGEMENT =====");
            System.out.println("1. Create User");
            System.out.println("2. List Users");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Back");

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1: createUserFlow(); break;
                case 2: userService.listUsers(); break;
                case 3: updateUserFlow(); break;
                case 4: deleteUserFlow(); break;
                case 5: return;
                default: System.out.println("Invalid!"); 
            }
        }
    }

    private void createUserFlow() {
        System.out.print("Username: ");
        String uname = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Full Name: ");
        String fname = sc.nextLine();

        System.out.print("Role (ADMIN/HOD/PROFESSOR/EXAM_STAFF): ");
        String role = sc.nextLine().toUpperCase();

        System.out.print("Department (or blank): ");
        String dept = sc.nextLine();
        if (dept.trim().isEmpty()) dept = null;

        userService.createUser(uname, pass, fname, role, dept);
    }

    private void updateUserFlow() {
        System.out.print("Enter User ID to update: ");
        int id = Integer.parseInt(sc.nextLine());

        User u = userService.getUserById(id);
        if (u == null) {
            System.out.println("User not found!");
            return;
        }

        System.out.println("Leave blank to keep old values.");

        System.out.print("New Username (" + u.getUsername() + "): ");
        String uname = sc.nextLine();
        if (!uname.trim().isEmpty()) u = new User(u.getId(), uname, u.getPassword(), u.getFullName(), u.getRole(), u.getDepartment());

        System.out.print("New Password (leave blank to keep same): ");
        String pass = sc.nextLine();

        System.out.print("New Full Name (" + u.getFullName() + "): ");
        String fname = sc.nextLine();
        if (!fname.trim().isEmpty()) u = new User(u.getId(), u.getUsername(), u.getPassword(), fname, u.getRole(), u.getDepartment());

        System.out.print("New Role (" + u.getRole() + "): ");
        String role = sc.nextLine().toUpperCase();
        if (!role.trim().isEmpty()) u = new User(u.getId(), u.getUsername(), u.getPassword(), u.getFullName(), role, u.getDepartment());

        System.out.print("New Department (" + u.getDepartment() + "): ");
        String dept = sc.nextLine();
        if (!dept.trim().isEmpty()) u = new User(u.getId(), u.getUsername(), u.getPassword(), u.getFullName(), u.getRole(), dept);

        userService.updateUser(u, pass);
    }

    private void deleteUserFlow() {
        System.out.print("Enter User ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        userService.deleteUser(id);
    }
}
