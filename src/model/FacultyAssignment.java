package model;

public class FacultyAssignment {

    private int id;
    private int professorId;
    private int subjectId;
    private int semester;
    private String section;

    public FacultyAssignment(int id, int professorId, int subjectId, int semester, String section) {
        this.id = id;
        this.professorId = professorId;
        this.subjectId = subjectId;
        this.semester = semester;
        this.section = section;
    }

    public int getId() { return id; }
    public int getProfessorId() { return professorId; }
    public int getSubjectId() { return subjectId; }
    public int getSemester() { return semester; }
    public String getSection() { return section; }
}
