package dao;

import model.User;
import util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("department")
                );
            }
        } catch (Exception e) {
            System.out.println("User fetch error: " + e.getMessage());
        }
        return null;
    }

    // CREATE USER
    public void createUser(User u) {
        String sql = "INSERT INTO users (username, password, full_name, role, department) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword()); // already hashed
            stmt.setString(3, u.getFullName());
            stmt.setString(4, u.getRole());
            stmt.setString(5, u.getDepartment());

            stmt.executeUpdate();
            System.out.println("User created successfully!");

        } catch (Exception e) {
            System.out.println("User create error: " + e.getMessage());
        }
    }

    // LIST ALL USERS
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id ASC";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("department")
                ));
            }

        } catch (Exception e) {
            System.out.println("User list error: " + e.getMessage());
        }
        return users;
    }

    // UPDATE USER
    public void updateUser(User u) {
        String sql = "UPDATE users SET username=?, password=?, full_name=?, role=?, department=? WHERE id=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getFullName());
            stmt.setString(4, u.getRole());
            stmt.setString(5, u.getDepartment());
            stmt.setInt(6, u.getId());

            stmt.executeUpdate();
            System.out.println("User updated successfully!");

        } catch (Exception e) {
            System.out.println("User update error: " + e.getMessage());
        }
    }

    // DELETE USER
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("User deleted successfully!");

        } catch (Exception e) {
            System.out.println("User delete error: " + e.getMessage());
        }
    }
    public User getUserById(int id) {
    String sql = "SELECT * FROM users WHERE id=?";
    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
             return new User(
                 rs.getInt("id"),
                 rs.getString("username"),
                 rs.getString("password"),
                 rs.getString("full_name"),
                 rs.getString("role"),
                 rs.getString("department")
             );
        }

    } catch (Exception e) {
        System.out.println("Fetch error: " + e.getMessage());
    }
    return null;
}

}
