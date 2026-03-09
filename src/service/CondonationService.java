package service;

import dao.AttendanceDao;
import dao.StudentDao;
import dao.SubjectDao;

public class CondonationService {

    private AttendanceDao attendanceDao = new AttendanceDao();
    private StudentDao studentDao = new StudentDao();
    private SubjectDao subjectDao = new SubjectDao();
    private EligibilityService eligibilityService = new EligibilityService();

    public void approveCondonation(String usn, String subjectCode, String reason) {

        int studentId = studentDao.getByUSN(usn).getId();
        int subjectId = subjectDao.getByCode(subjectCode).getId();

        var status = attendanceDao.getCondonationStatus(studentId, subjectId);

        if (status == null) {
            System.out.println("❌ Attendance record not found");
            return;
        }

        double percent = status.attendancePercent();

        if (percent < 65) {
            System.out.println("❌ Attendance below condonation limit");
            return;
        }

        if (percent >= 75) {
            System.out.println("⚠ Attendance already sufficient — condonation not required");
            return;
        }

        attendanceDao.approveCondonation(studentId, subjectId, reason);

        // 🔁 Re-evaluate eligibility
        eligibilityService.evaluateEligibility(studentId, subjectId);

        System.out.println("✅ Condonation approved successfully");
    }
}
