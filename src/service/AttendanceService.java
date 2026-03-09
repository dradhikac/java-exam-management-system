package service;

import dao.AttendanceDao;
import dao.StudentDao;
import dao.SubjectDao;
import model.Attendance;
import model.Subject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;


public class AttendanceService {

    private AttendanceDao attendanceDao = new AttendanceDao();
    private StudentDao studentDao = new StudentDao();
    private SubjectDao subjectDao = new SubjectDao();

    public void enterAttendance(String usn, String subjectCode,
                                Double theory, Double lab, Double overall) {

        int studentId = studentDao.getByUSN(usn).getId();
        Subject subject = subjectDao.getByCode(subjectCode);
        int subjectId = subject.getId();

        Attendance a = new Attendance(
                0,
                studentId,
                subjectId,
                theory,
                lab,
                overall,
    false   // condonationApproved default
);

        attendanceDao.upsertAttendance(a);
        System.out.println("✅ Attendance saved successfully");
    }

    public void importAttendanceFromCSV(String filePath) {

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

        String header = br.readLine(); // header
        if (header == null) {
            System.out.println("❌ Empty CSV file");
            return;
        }

        boolean isIPCC = header.contains("theory_attendance_percent");

        String line;
        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            String usn = data[0].trim();
            String subjectCode = data[1].trim();

            if (isIPCC) {
                double theory = Double.parseDouble(data[2].trim());
                double lab = Double.parseDouble(data[3].trim());

                enterAttendance(usn, subjectCode, theory, lab, null);

            } else {
                double percent = Double.parseDouble(data[2].trim());

                enterAttendance(usn, subjectCode, null, null, percent);
            }
        }

        System.out.println("✅ Attendance CSV imported successfully");

    } catch (Exception e) {
        System.out.println("❌ Attendance CSV import error: " + e.getMessage());
    }
}

public void exportAttendanceBySubject(String subjectCode, String filePath) {

    try (FileWriter fw = new FileWriter(filePath)) {

        ResultSet rs = attendanceDao.getAttendanceBySubjectCode(subjectCode);

        boolean headerWritten = false;
        boolean isIPCC = false;

        while (rs.next()) {

            if (!headerWritten) {
                String category = rs.getString("category_code");
                isIPCC = category.equalsIgnoreCase("IPCC");

                if (isIPCC) {
                    fw.write(
                        "student_name,usn,subject_code,subject_name," +
                        "theory_attendance_percent,lab_attendance_percent\n"
                    );
                } else {
                    fw.write(
                        "student_name,usn,subject_code,subject_name," +
                        "attendance_percent\n"
                    );
                }
                headerWritten = true;
            }

            if (isIPCC) {
                fw.write(String.format(
                    "%s,%s,%s,%s,%.2f,%.2f%n",
                    rs.getString("student_name"),
                    rs.getString("usn"),
                    rs.getString("subject_code"),
                    rs.getString("subject_name"),
                    rs.getDouble("theory_attendance_percent"),
                    rs.getDouble("lab_attendance_percent")
                ));
            } else {
                fw.write(String.format(
                    "%s,%s,%s,%s,%.2f%n",
                    rs.getString("student_name"),
                    rs.getString("usn"),
                    rs.getString("subject_code"),
                    rs.getString("subject_name"),
                    rs.getDouble("attendance_percent")
                ));
            }
        }

        System.out.println("✅ Attendance CSV exported successfully");

    } catch (Exception e) {
        System.out.println("❌ Attendance CSV export error: " + e.getMessage());
    }
}

}
