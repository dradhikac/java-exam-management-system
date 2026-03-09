package auth;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import java.util.Scanner;

public class AuthService {

    private UserDao userDao = new UserDao();
    private User loggedInUser;

    public User login() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        User user = userDao.getUserByUsername(u);

        if (user == null) {
            System.out.println("User not found.");
            return null;
        }

        // Compare hashed passwords
        if (!PasswordUtil.verifyPassword(p, user.getPassword())) {
            System.out.println("Incorrect password.");
            return null;
        }

        System.out.println("Login successful. Welcome " + user.getFullName() + " (" + user.getRole() + ")");
        loggedInUser = user;
        return user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}
