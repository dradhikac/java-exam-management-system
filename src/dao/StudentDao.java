package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Student;

public class StudentDao {

    // INSERT student
    public void insertStudent(Student s) {
        String sql = "INSERT INTO students(full_name, usn, temp_roll_no, semester, department) VALUES(?,?,?,?,?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getFullName());
            stmt.setString(2, s.getUsn());
            stmt.setString(3, s.getTempRoll());
            stmt.setInt(4, s.getSemester());
            stmt.setString(5, s.getDepartment());

            stmt.executeUpdate();
            System.out.println("Student saved successfully!");

        } catch (Exception e) {
            System.out.println("Insert Error: " + e.getMessage());
        }
    }

    // UPDATE student
    public void updateStudent(Student s) {
        String sql = "UPDATE students SET full_name=?, usn=?, temp_roll_no=?, semester=?, department=? WHERE id=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getFullName());
            stmt.setString(2, s.getUsn());
            stmt.setString(3, s.getTempRoll());
            stmt.setInt(4, s.getSemester());
            stmt.setString(5, s.getDepartment());
            stmt.setInt(6, s.getId());

            stmt.executeUpdate();
            System.out.println("Student updated successfully!");

        } catch (Exception e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    // LIST students
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY id ASC";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("usn"),
                    rs.getString("temp_roll_no"),
                    rs.getInt("semester"),
                    rs.getString("department"), false
                );
                list.add(s);
            }

        } catch (Exception e) {
            System.out.println("Fetch Error: " + e.getMessage());
        }

        return list;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("usn"),
                    rs.getString("temp_roll_no"),
                    rs.getInt("semester"),
                    rs.getString("department"), false
                );
            }

        } catch (Exception e) {
            System.out.println("Fetch Error: " + e.getMessage());
        }
        return null;
    }


    // SEARCH by USN
    public Student getByUSN(String usn) {
        String sql = "SELECT * FROM students WHERE usn=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("usn"),
                    rs.getString("temp_roll_no"),
                    rs.getInt("semester"),
                    rs.getString("department"),
                    rs.getBoolean("admit_card_generated")

                );
            }

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
        return null;
    }

    // SEARCH by Temp Roll
    public Student getByTempRoll(String temp) {
        String sql = "SELECT * FROM students WHERE temp_roll_no=?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, temp);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("usn"),
                    rs.getString("temp_roll_no"),
                    rs.getInt("semester"),
                    rs.getString("department"), false
                );
            }

        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
        return null;
    }

    // SEARCH by name (partial)
    public List<Student> searchByName(String name) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE full_name LIKE ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("usn"),
                    rs.getString("temp_roll_no"),
                    rs.getInt("semester"),
                    rs.getString("department"), false
                ));
            }

        } catch (Exception e) {
            System.out.println("Name Search Error: " + e.getMessage());
        }

        return list;
    }

    public Integer getStudentIdByUSNOrTemp(String value) {

    String sql = "SELECT id FROM students WHERE usn = ? OR temp_roll_no = ?";

    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, value);
        stmt.setString(2, value);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }

    } catch (Exception e) {
        System.out.println("Student Lookup Error: " + e.getMessage());
    }

    return null;
}
public int getStudentIdByUSN(String usn) {
    String sql = "SELECT id FROM students WHERE usn=?";
    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, usn);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("id");

    } catch (Exception e) {
        System.out.println("Student lookup error");
    }
    return -1;
}

public void markAdmitCardGenerated(int studentId) {

    String sql = "UPDATE students SET admit_card_generated = TRUE WHERE id=?";

    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println("Admit card freeze error: " + e.getMessage());
    }
}


}
