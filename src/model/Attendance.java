package model;

public class Attendance {

    private int id;
    private int studentId;
    private int subjectId;

    // IPCC attendance
    private Double theoryAttendancePercent;
    private Double labAttendancePercent;

    // BSC / PCC / AEC attendance
    private Double attendancePercent;

    // Condonation
    private boolean condonationApproved;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public Attendance(
            int id,
            int studentId,
            int subjectId,
            Double theoryAttendancePercent,
            Double labAttendancePercent,
            Double attendancePercent,
            boolean condonationApproved
    ) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.theoryAttendancePercent = theoryAttendancePercent;
        this.labAttendancePercent = labAttendancePercent;
        this.attendancePercent = attendancePercent;
        this.condonationApproved = condonationApproved;
    }

    // =====================================================
    // GETTERS
    // =====================================================
    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    // IPCC
    public Double getTheoryAttendance() {
        return theoryAttendancePercent;
    }

    public Double getLabAttendance() {
        return labAttendancePercent;
    }

    // BSC / PCC / AEC
    public Double getOverallAttendance() {
        return attendancePercent;
    }

    // Condonation
    public boolean isCondonationApproved() {
        return condonationApproved;
    }

    // =====================================================
    // SETTERS (OPTIONAL BUT GOOD PRACTICE)
    // =====================================================
    public void setTheoryAttendance(Double theoryAttendancePercent) {
        this.theoryAttendancePercent = theoryAttendancePercent;
    }

    public void setLabAttendance(Double labAttendancePercent) {
        this.labAttendancePercent = labAttendancePercent;
    }

    public void setOverallAttendance(Double attendancePercent) {
        this.attendancePercent = attendancePercent;
    }

    public void setCondonationApproved(boolean condonationApproved) {
        this.condonationApproved = condonationApproved;
    }
}
