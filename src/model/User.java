package model;

public class User {

    private int id;
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String department;

    public User(int id, String username, String password, String fullName, String role, String department) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.department = department;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
}
