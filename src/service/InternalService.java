package service;

import dao.InternalDao;
import dao.StudentDao;
import dao.SubjectDao;
import model.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class InternalService {

    private InternalDao internalDao = new InternalDao();
    private StudentDao studentDao = new StudentDao();
    private SubjectDao subjectDao = new SubjectDao();

    // ---------------- MANUAL ENTRY ----------------
    public void enterInternal(String usn, String subjectCode, Scanner sc) {

        int studentId = studentDao.getStudentIdByUSN(usn);
        if (studentId == -1) {
            System.out.println("❌ Invalid Student USN");
            return;
        }

        int subjectId = subjectDao.getSubjectIdByCode(subjectCode);
        if (subjectId == -1) {
            System.out.println("❌ Invalid Subject Code");
            return;
        }

        Subject subject = subjectDao.getSubjectById(subjectId);
        String category = subject.getCategoryCode();

        int cieTheory = 0, cieLab = 0;
        boolean eligible;
        String reason = "ELIGIBLE";

        if (category.equals("IPCC")) {

            System.out.print("Enter CIE Theory Marks (0–25): ");
            cieTheory = Integer.parseInt(sc.nextLine());

            System.out.print("Enter CIE Lab Marks (0–25): ");
            cieLab = Integer.parseInt(sc.nextLine());

            eligible = cieTheory >= 10 && cieLab >= 10;
            if (!eligible)
                reason = "Failed IPCC CIE (Theory/Lab)";

        } else { // BSC / PCC / AEC

            System.out.print("Enter CIE Marks (0–50): ");
            cieTheory = Integer.parseInt(sc.nextLine());

            eligible = cieTheory >= 20;
            if (!eligible)
                reason = "Failed CIE";
        }

        int cieTotal = cieTheory + cieLab;

        internalDao.upsertInternal(
                studentId,
                subjectId,
                cieTheory,
                cieLab,
                cieTotal,
                eligible,
                reason
        );

        System.out.println("✅ Internal marks saved");
    }

    // ---------------- CSV IMPORT ----------------
    public void importInternalFromCSV(String filePath) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            br.readLine(); // header
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");

                String usn = d[0].trim();
                String subjectCode = d[1].trim();

                Integer theory = d[2].isBlank() ? null : Integer.parseInt(d[2]);
                Integer lab = d.length > 3 && !d[3].isBlank()
                        ? Integer.parseInt(d[3]) : null;

                int studentId = studentDao.getStudentIdByUSN(usn);
                int subjectId = subjectDao.getSubjectIdByCode(subjectCode);

                Subject s = subjectDao.getSubjectById(subjectId);
                String category = s.getCategoryCode();

                boolean eligible;
                String reason = "ELIGIBLE";

                if (category.equals("IPCC")) {
                    eligible = theory != null && lab != null
                            && theory >= 10 && lab >= 10;
                    if (!eligible) reason = "Failed IPCC CIE";
                } else {
                    eligible = theory != null && theory >= 20;
                    if (!eligible) reason = "Failed CIE";
                }

                int total = (theory == null ? 0 : theory)
                          + (lab == null ? 0 : lab);

                internalDao.upsertInternal(
                        studentId, subjectId,
                        theory == null ? 0 : theory,
                        lab == null ? 0 : lab,
                        total, eligible, reason
                );
            }

            System.out.println("✅ Internal CSV imported successfully");

        } catch (Exception e) {
            System.out.println("❌ CSV Import Error: " + e.getMessage());
        }
    }

    // ---------------- LIST INTERNALS ----------------
    public void listInternals() {
        internalDao.listAll();
    }
}
