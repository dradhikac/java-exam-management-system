package dao;

import model.FacultyAssignment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultyAssignmentDao {

    public void insertAssignment(FacultyAssignment fa) {
        String sql = "INSERT INTO faculty_assignments (professor_id, subject_id, semester, section) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fa.getProfessorId());
            stmt.setInt(2, fa.getSubjectId());
            stmt.setInt(3, fa.getSemester());
            stmt.setString(4, fa.getSection());

            stmt.executeUpdate();
            System.out.println("Faculty assigned to subject successfully!");

        } catch (Exception e) {
            System.out.println("Error inserting assignment: " + e.getMessage());
        }
    }

    public List<FacultyAssignment> getAllAssignments() {
        List<FacultyAssignment> list = new ArrayList<>();

        String sql = "SELECT * FROM faculty_assignments ORDER BY id ASC";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                FacultyAssignment fa = new FacultyAssignment(
                    rs.getInt("id"),
                    rs.getInt("professor_id"),
                    rs.getInt("subject_id"),
                    rs.getInt("semester"),
                    rs.getString("section")
                );
                list.add(fa);
            }

        } catch (Exception e) {
            System.out.println("Error fetching assignments: " + e.getMessage());
        }

        return list;
    }
}
