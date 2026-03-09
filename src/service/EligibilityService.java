package service;

import dao.InternalDao;
import dao.AttendanceDao;

public class EligibilityService {

    private InternalDao internalDao = new InternalDao();
    private AttendanceDao attendanceDao = new AttendanceDao();

    // =====================================================
    // EVALUATE ELIGIBILITY FOR ONE STUDENT + SUBJECT
    // =====================================================
    public void evaluateEligibility(int studentId, int subjectId) {

        // ---------------- 1️⃣ CIE CHECK ----------------
        boolean cieEligible = internalDao.isCieEligible(studentId, subjectId);

        // ---------------- 2️⃣ ATTENDANCE CHECK ----------------
        AttendanceDao.AttendanceCondonationStatus att =
                attendanceDao.getCondonationStatus(studentId, subjectId);

        boolean attendanceEligible = false;

        if (att != null) {

            // IPCC attendance logic
            if ("IPCC".equalsIgnoreCase(att.categoryCode())) {

                Double theory = att.theoryPercent();
                Double lab = att.labPercent();

                if (theory != null && lab != null) {
                    attendanceEligible = theory >= 75 && lab >= 75;
                }

            }
            // BSC / PCC / AEC attendance logic
            else {

                Double percent = att.overallPercent();

                if (percent != null) {
                    attendanceEligible = percent >= 75;
                }
            }

            // ❗ Condonation ONLY if attendance failed
            if (!attendanceEligible && att.condonationApproved()) {
                attendanceEligible = true;
            }
        }

        // ---------------- 3️⃣ FINAL DECISION ----------------

        // Case 1: Both failed
        if (!cieEligible && !attendanceEligible) {
            internalDao.updateEligibility(
                studentId,
                subjectId,
                false,
                "CIE + Attendance not satisfied"
            );
            return;
        }

        // Case 2: CIE failed only
        if (!cieEligible) {
            internalDao.updateEligibility(
                studentId,
                subjectId,
                false,
                "CIE criteria not satisfied"
            );
            return;
        }

        // Case 3: Attendance failed only
        if (!attendanceEligible) {
            internalDao.updateEligibility(
                studentId,
                subjectId,
                false,
                "Attendance criteria not satisfied"
            );
            return;
        }

        // Case 4: Both passed
        internalDao.updateEligibility(
            studentId,
            subjectId,
            true,
            "ELIGIBLE"
        );
    }

    // =====================================================
    // BULK COMPUTE ELIGIBILITY (ADMIN OPTION)
    // =====================================================
    public void computeEligibility() {

        for (InternalDao.StudentSubjectPair pair
                : internalDao.getAllStudentSubjectPairs()) {

            evaluateEligibility(pair.studentId(), pair.subjectId());
        }

        System.out.println("✅ Eligibility computed (CIE + Attendance applied correctly)");
    }

    // =====================================================
// EXPORT ELIGIBILITY BY SUBJECT (CSV)
// =====================================================
public void exportEligibilityBySubjectToCSV(String subjectCode, String filePath) {

    try (java.io.FileWriter fw = new java.io.FileWriter(filePath)) {

        java.sql.ResultSet rs =
                internalDao.getEligibilityDataBySubject(subjectCode);

        // CSV header
        fw.write(
            "student_name,usn,subject_code,subject_name," +
            "eligible_for_see,eligibility_reason\n"
        );

        while (rs.next()) {
            fw.write(String.format(
                "%s,%s,%s,%s,%s,%s%n",
                rs.getString("student_name"),
                rs.getString("usn"),
                rs.getString("subject_code"),
                rs.getString("subject_name"),
                rs.getBoolean("eligible_for_see") ? "YES" : "NO",
                rs.getString("eligibility_reason")
            ));
        }

        System.out.println(
            "✅ Eligibility CSV exported successfully for subject: " + subjectCode
        );

    } catch (Exception e) {
        System.out.println(
            "❌ Eligibility CSV export error: " + e.getMessage()
        );
    }
}

}

