package com.school.result_management.service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.school.result_management.model.Result;
import com.school.result_management.model.Student;
import com.school.result_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private ResultService resultService;

    @Autowired
    private StudentRepository studentRepository;

    // ── Custom Colors ─────────────────────────────
    private static final DeviceRgb DARK_BLUE    = new DeviceRgb(30, 58, 138);
    private static final DeviceRgb LIGHT_BLUE   = new DeviceRgb(219, 234, 254);
    private static final DeviceRgb DARK_GRAY    = new DeviceRgb(55, 65, 81);
    private static final DeviceRgb LIGHT_GRAY   = new DeviceRgb(248, 250, 252);
    private static final DeviceRgb GREEN_BG     = new DeviceRgb(220, 252, 231);
    private static final DeviceRgb GREEN_TEXT   = new DeviceRgb(22, 101, 52);
    private static final DeviceRgb RED_BG       = new DeviceRgb(254, 226, 226);
    private static final DeviceRgb RED_TEXT     = new DeviceRgb(153, 27, 27);
    private static final DeviceRgb GRADE_BG     = new DeviceRgb(224, 242, 254);
    private static final DeviceRgb GRADE_TEXT   = new DeviceRgb(3, 105, 161);
    private static final DeviceRgb WHITE        = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb GRAY         = new DeviceRgb(107, 114, 128);

    public byte[] generateMarksheet(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        List<Result> results = resultService.getResultsByStudent(studentId);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(30, 40, 30, 40);

        // ── HEADER BANNER ─────────────────────────────
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        Cell headerCell = new Cell()
                .setBackgroundColor(DARK_BLUE)
                .setBorder(Border.NO_BORDER)
                .setPadding(20)
                .setTextAlignment(TextAlignment.CENTER);

        headerCell.add(new Paragraph("SCHOOL RESULT MANAGEMENT SYSTEM")
                .setFontColor(WHITE)
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER));

        headerCell.add(new Paragraph("OFFICIAL STUDENT MARKSHEET")
                .setFontColor(LIGHT_BLUE)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        headerTable.addCell(headerCell);
        document.add(headerTable);

        document.add(new Paragraph(" ").setFontSize(6));

        // ── STUDENT INFO HEADER ───────────────────────
        Table infoHeaderTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        infoHeaderTable.addCell(new Cell()
                .add(new Paragraph("  STUDENT INFORMATION").setBold().setFontSize(11))
                .setBackgroundColor(LIGHT_BLUE)
                .setFontColor(DARK_BLUE)
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(6).setPaddingBottom(6));
        document.add(infoHeaderTable);

        // ── STUDENT INFO GRID ─────────────────────────
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));

        infoTable.addCell(createInfoCell("Student Name",
                student.getUser().getName()));
        infoTable.addCell(createInfoCell("Roll Number",
                student.getRollNumber()));
        infoTable.addCell(createInfoCell("Class",
                student.getClassRoom().getName() + " - " + student.getClassRoom().getSection()));
        infoTable.addCell(createInfoCell("Academic Year",
                student.getClassRoom().getAcademicYear()));

        document.add(infoTable);
        document.add(new Paragraph(" ").setFontSize(8));

        // ── RESULTS SECTION HEADER ────────────────────
        Table resultHeaderTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        resultHeaderTable.addCell(new Cell()
                .add(new Paragraph("  SUBJECT-WISE RESULTS").setBold().setFontSize(11))
                .setBackgroundColor(LIGHT_BLUE)
                .setFontColor(DARK_BLUE)
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(6).setPaddingBottom(6));
        document.add(resultHeaderTable);

        // ── RESULTS TABLE ─────────────────────────────
        Table resultTable = new Table(
                UnitValue.createPercentArray(new float[]{3, 1.2f, 1.5f, 1, 1.2f}))
                .setWidth(UnitValue.createPercentValue(100));

        // Column headers
        String[] headers = {"Subject", "Max Marks", "Marks Obtained", "Grade", "Status"};
        for (String header : headers) {
            resultTable.addHeaderCell(
                    new Cell()
                            .add(new Paragraph(header).setBold().setFontSize(10))
                            .setBackgroundColor(DARK_BLUE)
                            .setFontColor(WHITE)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(9)
                            .setBorder(new SolidBorder(WHITE, 0.5f))
            );
        }

        // Data rows
        int totalMarks    = 0;
        int totalObtained = 0;
        boolean overallPass = true;
        boolean alternate   = false;

        for (Result result : results) {
            totalMarks    += result.getSubject().getMaxMarks();
            totalObtained += result.getMarksObtained();
            if (!result.getIsPassed()) overallPass = false;

            DeviceRgb rowBg = alternate ? LIGHT_GRAY : WHITE;
            alternate = !alternate;

            // Subject name
            resultTable.addCell(createDataCell(
                    result.getSubject().getName(), false, rowBg, DARK_GRAY));

            // Max marks
            resultTable.addCell(createDataCell(
                    String.valueOf(result.getSubject().getMaxMarks()), true, rowBg, DARK_GRAY));

            // Marks obtained
            resultTable.addCell(createDataCell(
                    String.valueOf(result.getMarksObtained()), true, rowBg, DARK_GRAY));

            // Grade cell
            resultTable.addCell(new Cell()
                    .add(new Paragraph(result.getGrade()).setBold().setFontSize(11))
                    .setBackgroundColor(GRADE_BG)
                    .setFontColor(GRADE_TEXT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(7)
                    .setBorder(new SolidBorder(WHITE, 0.5f)));

            // Pass / Fail cell
            boolean passed = result.getIsPassed();
            resultTable.addCell(new Cell()
                    .add(new Paragraph(passed ? "PASS" : "FAIL").setBold().setFontSize(10))
                    .setBackgroundColor(passed ? GREEN_BG : RED_BG)
                    .setFontColor(passed ? GREEN_TEXT : RED_TEXT)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(7)
                    .setBorder(new SolidBorder(WHITE, 0.5f)));
        }

        document.add(resultTable);
        document.add(new Paragraph(" ").setFontSize(8));

        // ── SUMMARY SECTION HEADER ────────────────────
        Table summaryHeaderTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        summaryHeaderTable.addCell(new Cell()
                .add(new Paragraph("  RESULT SUMMARY").setBold().setFontSize(11))
                .setBackgroundColor(LIGHT_BLUE)
                .setFontColor(DARK_BLUE)
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(6).setPaddingBottom(6));
        document.add(summaryHeaderTable);

        // ── SUMMARY BOXES ─────────────────────────────
        double percentage = totalMarks > 0
                ? (totalObtained * 100.0) / totalMarks : 0;

        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));

        summaryTable.addCell(createSummaryCell("Total Marks",
                totalObtained + " / " + totalMarks));
        summaryTable.addCell(createSummaryCell("Percentage",
                String.format("%.2f%%", percentage)));
        summaryTable.addCell(createSummaryCell("Subjects",
                String.valueOf(results.size())));

        // Overall result — big colored box
        summaryTable.addCell(new Cell()
                .add(new Paragraph("Overall Result")
                        .setFontSize(9)
                        .setFontColor(WHITE))
                .add(new Paragraph(overallPass ? "PASS" : "FAIL")
                        .setBold()
                        .setFontSize(16)
                        .setFontColor(WHITE))
                .setBackgroundColor(overallPass
                        ? new DeviceRgb(5, 150, 105)
                        : new DeviceRgb(220, 38, 38))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setBorder(new SolidBorder(WHITE, 1)));

        document.add(summaryTable);
        document.add(new Paragraph(" ").setFontSize(16));

        // ── SIGNATURE ROW ─────────────────────────────
        Table signTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(100));

        signTable.addCell(createSignCell("Class Teacher"));
        signTable.addCell(createSignCell("Principal"));
        signTable.addCell(createSignCell("School Stamp"));

        document.add(signTable);
        document.add(new Paragraph(" ").setFontSize(8));

        // ── FOOTER ────────────────────────────────────
        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));

        footerTable.addCell(new Cell()
                .add(new Paragraph(
                        "This is a computer generated marksheet " +
                                "and does not require a physical signature.")
                        .setFontSize(8)
                        .setFontColor(GRAY)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(8));

        document.add(footerTable);

        document.close();
        return outputStream.toByteArray();
    }

    // ── HELPER METHODS ────────────────────────────────

    private Cell createInfoCell(String label, String value) {
        return new Cell()
                .add(new Paragraph(label)
                        .setFontSize(8)
                        .setFontColor(GRAY))
                .add(new Paragraph(value)
                        .setBold()
                        .setFontSize(11)
                        .setFontColor(DARK_GRAY))
                .setPadding(10)
                .setBackgroundColor(LIGHT_GRAY)
                .setBorder(new SolidBorder(WHITE, 1));
    }

    private Cell createDataCell(String value, boolean centered,
                                DeviceRgb bg, DeviceRgb textColor) {
        return new Cell()
                .add(new Paragraph(value).setFontSize(10).setFontColor(textColor))
                .setBackgroundColor(bg)
                .setTextAlignment(centered ? TextAlignment.CENTER : TextAlignment.LEFT)
                .setPadding(7)
                .setBorder(new SolidBorder(WHITE, 0.5f));
    }

    private Cell createSummaryCell(String label, String value) {
        return new Cell()
                .add(new Paragraph(label)
                        .setFontSize(9)
                        .setFontColor(GRAY))
                .add(new Paragraph(value)
                        .setBold()
                        .setFontSize(14)
                        .setFontColor(DARK_BLUE))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setBackgroundColor(LIGHT_GRAY)
                .setBorder(new SolidBorder(WHITE, 1));
    }

    private Cell createSignCell(String label) {
        return new Cell()
                .add(new Paragraph("\n\n"))
                .add(new Paragraph("_______________________")
                        .setFontColor(DARK_GRAY))
                .add(new Paragraph(label)
                        .setBold()
                        .setFontSize(10)
                        .setFontColor(DARK_GRAY))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setBorder(Border.NO_BORDER);
    }
}