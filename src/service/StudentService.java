package service;

import dao.StudentDao;
import model.Student;
import util.CSVUtils;
import util.CSVWriter;

import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private StudentDao dao = new StudentDao();

    public void addStudent(Student s) {
        dao.insertStudent(s);
    }

    public void updateStudent(Student s) {
        dao.updateStudent(s);
    }

    public void listStudents() {
        List<Student> students = dao.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("\n=== STUDENT LIST ===");
        for (Student s : students) {
            System.out.println(
                s.getId() + " | " +
                s.getFullName() + " | " +
                (s.getUsn() == null ? "N/A" : s.getUsn()) + " | " +
                (s.getTempRoll() == null ? "N/A" : s.getTempRoll()) + " | " +
                "Sem " + s.getSemester() + " | " + s.getDepartment()
            );
        }
        System.out.println("====================");
    }

    public Student getStudentById(int id) {
        return dao.getStudentById(id);
    }

    public Student getByUSN(String usn) {
        return dao.getByUSN(usn);
    }

    public Student getByTempRoll(String roll) {
        return dao.getByTempRoll(roll);
    }

    public List<Student> searchByName(String name) {
        return dao.searchByName(name);
    }

    public List<Student> getAllStudents() {
        return dao.getAllStudents();
    }

    // CSV IMPORT
    public void importStudentsFromCSV(String path) {
        List<String[]> rows = CSVUtils.readCSV(path);

        for (String[] row : rows) {
            try {
                String fullName = row[0];
                String dob = row[1]; // unused
                String dept = row[2];
                String usn = row[3].trim().isEmpty() ? null : row[3];
                String temp = row[4].trim().isEmpty() ? null : row[4];
                int sem = Integer.parseInt(row[5]);
                String primaryDept = row[6];

                Student s = new Student(0, fullName, usn, temp, sem, primaryDept, false);
                dao.insertStudent(s);

            } catch (Exception e) {
                System.out.println("Error importing row: " + e.getMessage());
            }
        }

        System.out.println("CSV Import Completed.");
    }

    // CSV EXPORT
    public void exportStudentsToCSV(String path) {
        List<Student> students = dao.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students to export!");
            return;
        }

        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id", "full_name", "usn", "temp_roll_no", "semester", "department"});

        for (Student s : students) {
            rows.add(new String[]{
                String.valueOf(s.getId()),
                s.getFullName(),
                s.getUsn() == null ? "" : s.getUsn(),
                s.getTempRoll() == null ? "" : s.getTempRoll(),
                String.valueOf(s.getSemester()),
                s.getDepartment()
            });
        }

        CSVWriter.writeCSV(path, rows);
    }

    public void addStudent(String name, String usn, String temp, int sem, String dept) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addStudent'");
    }

    
}
