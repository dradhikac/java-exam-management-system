package model;

public class Subject {

    private int id;
    private String code;
    private String name;
    private int semester;
    private String categoryCode;

    public Subject(int id, String code, String name,
                   int semester, String categoryCode) {

        this.id = id;
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.categoryCode = categoryCode;
    }

    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getSemester() { return semester; }
    public String getCategoryCode() { return categoryCode; }

    public void setName(String name) { this.name = name; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
}
