package seed;

import model.Subject;
import dao.SubjectDao;
import java.util.ArrayList;
import java.util.List;

public class DefaultSubjectsLoader {

    private SubjectDao dao = new SubjectDao();

    public void loadDefaults() {

        List<Subject> subjects = new ArrayList<>();

        // SEMESTER 1 (ODD)
        subjects.add(new Subject(0, "MAT101", "Engineering Mathematics I", "CSE", "ODD", 4));
        subjects.add(new Subject(0, "PHY101", "Engineering Physics", "CSE", "ODD", 4));
        subjects.add(new Subject(0, "CIV101", "Basic Civil Engineering", "CSE", "ODD", 3));
        subjects.add(new Subject(0, "ME101",  "Basic Mechanical Engineering", "CSE", "ODD", 3));
        subjects.add(new Subject(0, "CS101",  "Programming in C", "CSE", "ODD", 4));
        subjects.add(new Subject(0, "ELE101", "Basic Electrical Engineering", "CSE", "ODD", 3));
        subjects.add(new Subject(0, "ENG101", "Communicative English", "CSE", "ODD", 2));
        subjects.add(new Subject(0, "EVS101", "Environmental Studies", "CSE", "ODD", 2));

        // SEMESTER 2 (EVEN)
        subjects.add(new Subject(0, "MAT201", "Engineering Mathematics II", "CSE", "EVEN", 4));
        subjects.add(new Subject(0, "CHE201", "Engineering Chemistry", "CSE", "EVEN", 4));
        subjects.add(new Subject(0, "CS201",  "Data Structures", "CSE", "EVEN", 4));
        subjects.add(new Subject(0, "EC201",  "Basic Electronics", "CSE", "EVEN", 3));
        subjects.add(new Subject(0, "ME201",  "Engineering Mechanics", "CSE", "EVEN", 3));
        subjects.add(new Subject(0, "ENG201", "Technical English", "CSE", "EVEN", 2));
        subjects.add(new Subject(0, "WS201",  "Workshop Practice", "CSE", "EVEN", 2));
        subjects.add(new Subject(0, "CS202",  "Python Programming", "CSE", "EVEN", 3));

        // YOU CAN ADD SEMESTER 3–8 LATER, OR I CAN GENERATE FULL SET for all semesters.

        int inserted = 0;

        for (Subject s : subjects) {
            if (!dao.existsByCode(s.getCode())) {
                dao.insertSubject(s);
                inserted++;
            }
        }

        System.out.println("\nDefault Subjects Loaded: " + inserted);
    }
}
