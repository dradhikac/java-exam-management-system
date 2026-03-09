package service;

import dao.ExamScheduleDao;
import dao.SubjectDao;
import model.ExamSchedule;
import model.Subject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ExamScheduleService {

    private final ExamScheduleDao examScheduleDao = new ExamScheduleDao();
    private final SubjectDao subjectDao = new SubjectDao();

    public void setSchedule(String subjectCode,
                            String session,
                            LocalDate examDate,
                            LocalTime startTime,
                            LocalTime endTime) {

        // 1️⃣ Validate subject
        Subject subject = subjectDao.getByCode(subjectCode);
        if (subject == null) {
            System.out.println("❌ Subject not found: " + subjectCode);
            return;
        }

        // 2️⃣ Validate time
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            System.out.println("❌ End time must be after start time");
            return;
        }

        // 3️⃣ Create schedule object
        ExamSchedule schedule = new ExamSchedule(
                0,
                subject.getId(),
                subject.getSemester(),
                session,
                examDate,
                startTime,
                endTime
        );

        // 4️⃣ Save (insert/update)
        examScheduleDao.upsertSchedule(schedule);

        System.out.println("✅ Exam schedule saved successfully");
    }

    public void importFromCSV(String filePath) {

    DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

        String line = br.readLine(); // skip header

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            String subjectCode = data[0].trim();
            String session = data[1].trim();
            LocalDate examDate = LocalDate.parse(data[2].trim());
            LocalTime startTime =
                    LocalTime.parse(data[3].trim().toUpperCase(), timeFormatter);
            LocalTime endTime =
                    LocalTime.parse(data[4].trim().toUpperCase(), timeFormatter);

            Subject subject = subjectDao.getByCode(subjectCode);
            if (subject == null) {
                System.out.println("⚠ Subject not found: " + subjectCode);
                continue;
            }

            ExamSchedule schedule = new ExamSchedule(
                    0,
                    subject.getId(),
                    subject.getSemester(),
                    session,
                    examDate,
                    startTime,
                    endTime
            );

            examScheduleDao.upsertSchedule(schedule);
        }

        System.out.println("✅ Exam schedules imported successfully");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void exportToCSV(String session, String filePath) {

    DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("hh:mm a");

    try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

        pw.println("subject_id,semester,session,exam_date,start_time,end_time");

        for (ExamSchedule s : examScheduleDao.getBySession(session)) {

            pw.printf("%d,%d,%s,%s,%s,%s%n",
                    s.getSubjectId(),
                    s.getSemester(),
                    s.getSession(),
                    s.getExamDate(),
                    s.getStartTime().format(timeFormatter),
                    s.getEndTime().format(timeFormatter)
            );
        }

        System.out.println("✅ Exam schedules exported successfully");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
