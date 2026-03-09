package model;

public class InternalAssessment {

    private int id;
    private int studentId;
    private int subjectId;
    private int semester;
    private double score;
    private String component;

    // ✅ FULL CONSTRUCTOR (USED BY DAO + SERVICE)
    public InternalAssessment(int id,
                              int studentId,
                              int subjectId,
                              int semester,
                              double score,
                              String component) {

        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.semester = semester;
        this.score = score;
        this.component = component;
    }

    // ✅ OPTIONAL: constructor without id (useful sometimes)
    public InternalAssessment(int studentId,
                              int subjectId,
                              int semester,
                              double score,
                              String component) {

        this(0, studentId, subjectId, semester, score, component);
    }

    // -------- GETTERS --------
    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getSemester() {
        return semester;
    }

    public double getScore() {
        return score;
    }

    public String getComponent() {
        return component;
    }

    // -------- SETTERS --------
    public void setScore(double score) {
        this.score = score;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
