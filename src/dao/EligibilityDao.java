package dao;

import java.sql.*;

public class EligibilityDao {

    public void upsertEligibility(int studentId, int subjectId, int semester,
                              String status, String reason) {

    String sql = """
        INSERT INTO eligibility_status
        (student_id, subject_id, semester, status, reason)
        VALUES (?, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            status = VALUES(status),
            reason = VALUES(reason)
    """;

    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, subjectId);
        ps.setInt(3, semester);
        ps.setString(4, status);
        ps.setString(5, reason);

        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println("Eligibility save error: " + e.getMessage());
    }
}


}


