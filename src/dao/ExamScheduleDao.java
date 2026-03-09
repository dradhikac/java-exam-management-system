package dao;

import model.ExamSchedule;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;

public class ExamScheduleDao {

    // ==============================
    // INSERT / UPDATE EXAM SCHEDULE
    // ==============================
    public void upsertSchedule(ExamSchedule schedule) {

        String sql = """
            INSERT INTO exam_schedule
            (subject_id, semester, session, exam_date, start_time, end_time)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                exam_date = VALUES(exam_date),
                start_time = VALUES(start_time),
                end_time = VALUES(end_time)
        """;

        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, schedule.getSubjectId());
            ps.setInt(2, schedule.getSemester());
            ps.setString(3, schedule.getSession());
            ps.setDate(4, Date.valueOf(schedule.getExamDate()));
            ps.setTime(5, Time.valueOf(schedule.getStartTime()));
            ps.setTime(6, Time.valueOf(schedule.getEndTime()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // FETCH EXAM SCHEDULE (FOR ADMIT CARD MODULE)
    // ==========================================
    public ExamSchedule getBySubjectAndSession(int subjectId, String session) {

        String sql = """
            SELECT * FROM exam_schedule
            WHERE subject_id = ? AND session = ?
        """;

        try (Connection con = DBConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, subjectId);
            ps.setString(2, session);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new ExamSchedule(
                        rs.getInt("id"),
                        rs.getInt("subject_id"),
                        rs.getInt("semester"),
                        rs.getString("session"),
                        rs.getDate("exam_date").toLocalDate(),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // VERY IMPORTANT
    }

    public java.util.List<ExamSchedule> getBySession(String session) {

        java.util.List<ExamSchedule> list = new ArrayList<>();

    String sql = """
        SELECT * FROM exam_schedule
        WHERE session = ?
        ORDER BY exam_date, start_time
    """;

    try (Connection con = DBConnectionManager.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, session);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new ExamSchedule(
                    rs.getInt("id"),
                    rs.getInt("subject_id"),
                    rs.getInt("semester"),
                    rs.getString("session"),
                    rs.getDate("exam_date").toLocalDate(),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getTime("end_time").toLocalTime()
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
