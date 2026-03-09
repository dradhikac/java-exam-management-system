package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternalDao {

    // ================= HELPER RECORD =================
    public record StudentSubjectPair(int studentId, int subjectId) {}

    // ================= UPSERT INTERNAL MARKS =================
    public void upsertInternal(
            int studentId,
            int subjectId,
            Integer cieTheory,
            Integer cieLab,
            Integer cieTotal,
            boolean eligible,
            String reason
    ) {

        String sql = """
            INSERT INTO internal_assessments
            (student_id, subject_id, cie_theory, cie_lab, cie_total,
             eligible_for_see, eligibility_reason)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                cie_theory = VALUES(cie_theory),
                cie_lab = VALUES(cie_lab),
                cie_total = VALUES(cie_total),
                eligible_for_see = VALUES(eligible_for_see),
                eligibility_reason = VALUES(eligibility_reason)
        """;

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);

            if (cieTheory != null) ps.setInt(3, cieTheory);
            else ps.setNull(3, Types.INTEGER);

            if (cieLab != null) ps.setInt(4, cieLab);
            else ps.setNull(4, Types.INTEGER);

            if (cieTotal != null) ps.setInt(5, cieTotal);
            else ps.setNull(5, Types.INTEGER);

            ps.setBoolean(6, eligible);
            ps.setString(7, reason);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Internal DAO Error: " + e.getMessage());
        }
    }

    // ================= LIST INTERNALS =================
    public void listAll() {

        String sql = """
            SELECT s.usn, sub.subject_code,
                   ia.cie_theory, ia.cie_lab,
                   ia.cie_total, ia.eligible_for_see
            FROM internal_assessments ia
            JOIN students s ON ia.student_id = s.id
            JOIN subjects sub ON ia.subject_id = sub.id
        """;

        try (Connection conn = DBConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nUSN | Subject | Theory | Lab | Total | Eligible");

            while (rs.next()) {
                System.out.printf(
                    "%s | %s | %s | %s | %s | %s%n",
                    rs.getString("usn"),
                    rs.getString("subject_code"),
                    rs.getObject("cie_theory"),
                    rs.getObject("cie_lab"),
                    rs.getObject("cie_total"),
                    rs.getBoolean("eligible_for_see") ? "YES" : "NO"
                );
            }

        } catch (Exception e) {
            System.out.println("List Internals Error: " + e.getMessage());
        }
    }

    // ================= MARK FETCHERS =================
    public Integer getTheoryMarks(int studentId, int subjectId) {
        return getSingleInt(
            "SELECT cie_theory FROM internal_assessments WHERE student_id=? AND subject_id=?",
            studentId, subjectId, "cie_theory"
        );
    }

    public Integer getLabMarks(int studentId, int subjectId) {
        return getSingleInt(
            "SELECT cie_lab FROM internal_assessments WHERE student_id=? AND subject_id=?",
            studentId, subjectId, "cie_lab"
        );
    }

    public Integer getTotalMarks(int studentId, int subjectId) {
        return getSingleInt(
            "SELECT cie_total FROM internal_assessments WHERE student_id=? AND subject_id=?",
            studentId, subjectId, "cie_total"
        );
    }

    private Integer getSingleInt(String sql, int studentId, int subjectId, String col) {
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, subjectId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Object val = rs.getObject(col);
                return val == null ? null : rs.getInt(col);
            }

        } catch (Exception e) {
            System.out.println(col + " fetch error: " + e.getMessage());
        }
        return null;
    }

    // ================= UPDATE ELIGIBILITY =================
    public void updateEligibility(
            int studentId,
            int subjectId,
            boolean eligible,
            String reason
    ) {

        String sql = """
            UPDATE internal_assessments
            SET eligible_for_see = ?, eligibility_reason = ?
            WHERE student_id = ? AND subject_id = ?
        """;

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, eligible);
            ps.setString(2, reason);
            ps.setInt(3, studentId);
            ps.setInt(4, subjectId);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Eligibility update error: " + e.getMessage());
        }
    }

    // ================= BULK ELIGIBILITY =================
    public List<StudentSubjectPair> getAllStudentSubjectPairs() {

        List<StudentSubjectPair> list = new ArrayList<>();
        String sql = "SELECT student_id, subject_id FROM internal_assessments";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new StudentSubjectPair(
                        rs.getInt("student_id"),
                        rs.getInt("subject_id")
                ));
            }

        } catch (Exception e) {
            System.out.println("Fetch student-subject pairs error: " + e.getMessage());
        }

        return list;
    }

    // ================= CSV EXPORT (ALL / ELIGIBLE / NOT ELIGIBLE) =================
    public ResultSet getEligibilityData(boolean eligibleOnly, boolean nonEligibleOnly)
            throws Exception {

        String baseQuery = """
            SELECT
                st.full_name AS student_name,
                st.usn AS usn,
                sub.subject_code AS subject_code,
                sub.subject_name AS subject_name,
                att.attendance_percent AS attendance_percentage,
                ia.eligible_for_see,
                ia.eligibility_reason
            FROM internal_assessments ia
            JOIN students st ON ia.student_id = st.id
            JOIN subjects sub ON ia.subject_id = sub.id
            LEFT JOIN attendance att
              ON ia.student_id = att.student_id
             AND ia.subject_id = att.subject_id
        """;

        String whereClause = "";

        if (eligibleOnly) {
            whereClause = " WHERE ia.eligible_for_see = TRUE";
        } else if (nonEligibleOnly) {
            whereClause = " WHERE ia.eligible_for_see = FALSE";
        }

        Connection conn = DBConnectionManager.getConnection();
        Statement stmt = conn.createStatement();

        return stmt.executeQuery(baseQuery + whereClause);
    }

    // ================= SUBJECT-WISE CSV EXPORT =================
    public ResultSet getEligibilityDataBySubject(String subjectCode)
            throws Exception {

        String sql = """
            SELECT
                st.full_name AS student_name,
                st.usn AS usn,
                sub.subject_code AS subject_code,
                sub.subject_name AS subject_name,
                att.attendance_percent AS attendance_percentage,
                ia.eligible_for_see,
                ia.eligibility_reason
            FROM internal_assessments ia
            JOIN students st ON ia.student_id = st.id
            JOIN subjects sub ON ia.subject_id = sub.id
            LEFT JOIN attendance att
              ON ia.student_id = att.student_id
             AND ia.subject_id = att.subject_id
            WHERE sub.subject_code = ?
        """;

        Connection conn = DBConnectionManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, subjectCode);

        return ps.executeQuery();
    }

    public boolean isCieEligible(int studentId, int subjectId) {

    String sql = """
        SELECT cie_theory, cie_lab, cie_total
        FROM internal_assessments
        WHERE student_id = ? AND subject_id = ?
    """;

    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, subjectId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            Integer theory = (Integer) rs.getObject("cie_theory");
            Integer lab    = (Integer) rs.getObject("cie_lab");
            Integer total  = (Integer) rs.getObject("cie_total");

            // 🔹 IPCC case → theory + lab exist
            if (theory != null && lab != null) {
                return theory >= 10 && lab >= 10;
            }

            // 🔹 BSC / PCC / AEC case → only total
            if (total != null) {
                return total >= 20;
            }
        }

    } catch (Exception e) {
        System.out.println("CIE eligibility check error: " + e.getMessage());
    }

    return false; // default: not eligible
}

public boolean getEligibilityStatus(int studentId, int subjectId) {

    String sql = """
        SELECT eligible_for_see
        FROM internal_assessments
        WHERE student_id = ? AND subject_id = ?
    """;

    try (Connection conn = DBConnectionManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, subjectId);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("eligible_for_see");
        }

    } catch (Exception e) {
        System.out.println("Eligibility status fetch error");
    }

    return false;
}

}
