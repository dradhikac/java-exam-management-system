package model;

public class Student {

    private int id;
    private String fullName;
    private String usn;
    private String tempRoll;
    private int semester;
    private String department;
    private boolean admitCardGenerated;

    public Student(int id, String fullName, String usn, String tempRoll, int semester, String department, boolean  admitCardGenerated) {
        this.id = id;
        this.fullName = fullName;
        this.usn = usn;
        this.tempRoll = tempRoll;
        this.semester = semester;
        this.department = department;
        this.admitCardGenerated = admitCardGenerated;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsn() { return usn; }
    public String getTempRoll() { return tempRoll; }
    public int getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isAdmitCardGenerated() {
    return admitCardGenerated;
}
}
