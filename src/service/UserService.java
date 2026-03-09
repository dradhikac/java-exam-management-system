package service;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import java.util.List;

public class UserService {

    private UserDao dao = new UserDao();

    public void createUser(String username, String password, String fullName, String role, String dept) {
        String hashed = PasswordUtil.hashPassword(password);
        User u = new User(0, username, hashed, fullName, role, dept);
        dao.createUser(u);
    }

    public void listUsers() {
        List<User> users = dao.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("\n===== USER LIST =====");
        for (User u : users) {
            System.out.println(
                u.getId() + " | " +
                u.getUsername() + " | " +
                u.getFullName() + " | " +
                u.getRole() + " | " +
                (u.getDepartment() == null ? "N/A" : u.getDepartment())
            );
        }
        System.out.println("=====================");
    }

    public User getUserById(int id) {
        List<User> users = dao.getAllUsers();
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public void updateUser(User u, String newPassword) {
        if (!newPassword.trim().isEmpty()) {
            u = new User(u.getId(), u.getUsername(), PasswordUtil.hashPassword(newPassword),
                         u.getFullName(), u.getRole(), u.getDepartment());
        }
        dao.updateUser(u);
    }

    public void deleteUser(int id) {
        dao.deleteUser(id);
    }
}
