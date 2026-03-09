package service;

import dao.*;
import model.ExamSchedule;
import model.Student;
import model.Subject;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdmitCardService {

    private StudentDao studentDao = new StudentDao();
    private SubjectDao subjectDao = new SubjectDao();
    private InternalDao internalDao = new InternalDao();
    private ExamScheduleDao examScheduleDao = new ExamScheduleDao();

    // =====================================================
    // GENERATE ADMIT CARD (PDF)
    // =====================================================
    public void generateAdmitCard(String usn) {

        Student student = studentDao.getByUSN(usn);

        if (student == null) {
            System.out.println("❌ Student not found");
            return;
        }

        if (student.isAdmitCardGenerated()) {
            System.out.println("❌ Admit card already generated (eligibility frozen)");
            return;
        }

        int semester = student.getSemester();
        String examSession = deriveSessionFromSemester(semester);
        String fileName = usn + "_Sem" + semester + "_AdmitCard.pdf";

        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a");

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // =====================================================
            // HEADER
            // =====================================================
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1, 4});

            Image logo = Image.getInstance(
                    "dsatm-exam-system/src/resources/college_logo.png"
            );
            logo.scaleAbsolute(55, 55);

            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            header.addCell(logoCell);

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);

            textCell.addElement(centerText(
                    "DAYANANDA SAGAR ACADEMY OF TECHNOLOGY & MANAGEMENT",
                    13, Font.BOLD
            ));
            textCell.addElement(centerText(
                    "(An Autonomous Institution affiliated to VTU, Belagavi)",
                    9, Font.NORMAL
            ));
            textCell.addElement(centerText(
                    "ADMISSION TICKET – SEMESTER END EXAMINATION : " + examSession + "\n",
                    11, Font.BOLD
            ));

            header.addCell(textCell);
            document.add(header);

            // =====================================================
            // STUDENT DETAILS
            // =====================================================
            Paragraph studentInfo = new Paragraph(
                    "NAME OF THE STUDENT : " + student.getFullName() + "\n" +
                    "USN               : " + student.getUsn() + "\n" +
                    "DEPARTMENT        : " + student.getDepartment() + "\n" +
                    "PROGRAM           : " + student.getDepartment() + "\n" +
                    "SEMESTER          : " + semester + "\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA, 9)
            );
            document.add(studentInfo);

            // =====================================================
            // EXAM SCHEDULE TABLE (OFFICIAL FORMAT)
            // =====================================================
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(8);
            table.setWidths(new float[]{2f, 6f, 3f, 4f, 3f, 3f});

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            addHeaderCell(table, "COURSE\nCODE", headerFont);
            addHeaderCell(table, "COURSE TITLE", headerFont);
            addHeaderCell(table, "DATE", headerFont);
            addHeaderCell(table, "TIME", headerFont);
            addHeaderCell(table, "ELIGIBILITY", headerFont);
            addHeaderCell(table, "INVIGILATOR\nSIGNATURE", headerFont);

            List<Subject> subjects =
                    subjectDao.getSubjectsBySemester(semester);

            for (Subject subject : subjects) {

                boolean eligible =
                        internalDao.getEligibilityStatus(
                                student.getId(), subject.getId()
                        );

                ExamSchedule sch =
                        examScheduleDao.getBySubjectAndSession(
                                subject.getId(),
                                examSession
                        );

                table.addCell(bodyCell(subject.getCode(), bodyFont));
                table.addCell(bodyCell(subject.getName(), bodyFont));

                table.addCell(bodyCell(
                        sch != null ? sch.getExamDate().toString() : "TBA",
                        bodyFont
                ));

                table.addCell(bodyCell(
                        sch != null
                                ? sch.getStartTime().format(timeFormatter)
                                    + " - " +
                                    sch.getEndTime().format(timeFormatter)
                                : "TBA",
                        bodyFont
                ));

                table.addCell(bodyCell(
                        eligible ? "ELIGIBLE" : "NOT ELIGIBLE",
                        bodyFont
                ));

                // Empty signature column
                table.addCell(bodyCell("", bodyFont));
            }

            document.add(table);

            // =====================================================
            // FOOTER
            // =====================================================
            document.add(new Paragraph("\n\n"));

            PdfPTable footer = new PdfPTable(3);
            footer.setWidthPercentage(100);

            footer.addCell(signatureCell("Signature of Student"));
            footer.addCell(signatureCell("Signature of COE"));
            footer.addCell(signatureCell("Signature of Principal"));

            document.add(footer);

            document.add(centerText(
                    "Controller of Examinations\n" +
                    "Dayananda Sagar Academy of Technology & Management\n" +
                    "Opp. Art of Living, Kanakapura Road, Bengaluru – 560082",
                    9, Font.NORMAL
            ));

            document.close();

            // =====================================================
            // FREEZE ADMIT CARD
            // =====================================================
            studentDao.markAdmitCardGenerated(student.getId());

            System.out.println("✅ Admit card generated successfully: " + fileName);

        } catch (Exception e) {
            System.out.println("❌ Admit card generation error");
            e.printStackTrace();
        }
    }

    // =====================================================
    // SEMESTER → SESSION
    // =====================================================
    private String deriveSessionFromSemester(int semester) {
        int year = LocalDate.now().getYear();
        return (semester % 2 == 1)
                ? "Jan-Feb " + year
                : "Jul-Aug " + year;
    }

    // =====================================================
    // HEADER CELL
    // =====================================================
    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        cell.setBackgroundColor(new Color(230, 230, 230));
        table.addCell(cell);
    }

    // =====================================================
    // BODY CELL
    // =====================================================
    private PdfPCell bodyCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

    // =====================================================
    // SIGNATURE CELL
    // =====================================================
    private PdfPCell signatureCell(String label) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.addElement(new Paragraph(
                "\n\n" + label,
                FontFactory.getFont(FontFactory.HELVETICA, 9)
        ));
        return cell;
    }

    // =====================================================
    // CENTERED TEXT
    // =====================================================
    private Paragraph centerText(String text, int size, int style) {
        Paragraph p = new Paragraph(
                text,
                FontFactory.getFont(FontFactory.HELVETICA, size, style)
        );
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }
}
