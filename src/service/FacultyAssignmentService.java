package service;

import dao.FacultyAssignmentDao;
import model.FacultyAssignment;

import java.util.List;

public class FacultyAssignmentService {

    private FacultyAssignmentDao dao = new FacultyAssignmentDao();

    public void addAssignment(int professorId, int subjectId, int semester, String section) {
        FacultyAssignment fa = new FacultyAssignment(0, professorId, subjectId, semester, section);
        dao.insertAssignment(fa);
    }

    public void listAssignments() {
        List<FacultyAssignment> list = dao.getAllAssignments();

        if (list.isEmpty()) {
            System.out.println("No faculty assignments found.");
            return;
        }

        System.out.println("\n===== Faculty Assignments =====");
        for (FacultyAssignment fa : list) {
            System.out.println(
                "ID: " + fa.getId() +
                " | Professor ID: " + fa.getProfessorId() +
                " | Subject ID: " + fa.getSubjectId() +
                " | Semester: " + fa.getSemester() +
                " | Section: " + fa.getSection()
            );
        }
        System.out.println("================================");
    }

    public void assignFaculty(int userId, int subjectId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignFaculty'");
    }

    public void removeAssignment(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeAssignment'");
    }
}
