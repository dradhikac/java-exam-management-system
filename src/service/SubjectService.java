package service;

import dao.DBConnectionManager;
import dao.SubjectDao;
import model.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class SubjectService {

    private SubjectDao subjectDao = new SubjectDao();

    // ---------------- ADD SUBJECT ----------------
    public void addSubject(String code, String name,
                           int semester, String category) {

        if (!(category.equals("BSC") || category.equals("IPCC") || category.equals("PCC"))) {
            System.out.println("❌ Invalid category. Use BSC / IPCC / PCC.");
            return;
        }

        if (subjectDao.existsByCode(code)) {
            System.out.println("❌ Subject with this code already exists.");
            return;
        }

        Subject s = new Subject(
                0,
                code,
                name,
                semester,
                category
        );

        subjectDao.insertSubject(s);
    }

    // ---------------- LIST SUBJECTS ----------------
    public void listSubjects() {

        List<Subject> list = subjectDao.getAllSubjects();

        if (list.isEmpty()) {
            System.out.println("No subjects found.");
            return;
        }

        System.out.println("\n===== SUBJECT LIST =====");
        System.out.printf("%-5s %-12s %-30s %-8s %-8s%n",
                "ID", "Code", "Name", "Semester", "Category");

        for (Subject s : list) {
            System.out.printf("%-5d %-12s %-30s %-8d %-8s%n",
                    s.getId(),
                    s.getCode(),
                    s.getName(),
                    s.getSemester(),
                    s.getCategoryCode()
            );
        }
    }

    // ---------------- GET SUBJECT BY ID ----------------
    public Subject getSubjectById(int id) {
        return subjectDao.getSubjectById(id);
    }

    // ---------------- CSV IMPORT ----------------
    public void importSubjectsFromCSV(String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                String code = data[0].trim();
                String name = data[1].trim();
                int semester = Integer.parseInt(data[2].trim());
                String category = data[3].trim().toUpperCase();

                if (!subjectDao.existsByCode(code)) {
                    Subject s = new Subject(
                            0,
                            code,
                            name,
                            semester,
                            category
                    );
                    subjectDao.insertSubject(s);
                }
            }

            System.out.println("✅ Subjects imported successfully");

        } catch (Exception e) {
            System.out.println("❌ Subject CSV import error: " + e.getMessage());
        }
    }
    
    public Subject getByCode(String subjectCode) {

    Subject subject = null;

    String sql = "SELECT * FROM subjects WHERE subject_code = ?";

    try (Connection con = DBConnectionManager.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, subjectCode);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            subject = new Subject(
                rs.getInt("id"),
                rs.getString("subject_code"),
                rs.getString("subject_name"),
                rs.getInt("semester"),
                rs.getString("category_code")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return subject;
}


}

