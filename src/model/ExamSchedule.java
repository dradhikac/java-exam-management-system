package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamSchedule {

    private int id;
    private int subjectId;
    private int semester;
    private String session;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public ExamSchedule(int id,
                        int subjectId,
                        int semester,
                        String session,
                        LocalDate examDate,
                        LocalTime startTime,
                        LocalTime endTime) {

        this.id = id;
        this.subjectId = subjectId;
        this.semester = semester;
        this.session = session;
        this.examDate = examDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getSemester() {
        return semester;
    }

    public String getSession() {
        return session;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
