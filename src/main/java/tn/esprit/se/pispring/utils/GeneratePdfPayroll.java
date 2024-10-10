package tn.esprit.se.pispring.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.se.pispring.Service.ContributionService;
import tn.esprit.se.pispring.Service.PrimeService;
import tn.esprit.se.pispring.entities.Contribution;
import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.PayrollConfig;
import tn.esprit.se.pispring.entities.Prime;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GeneratePdfPayroll {
    @Autowired
    private PrimeService primeService;
    @Autowired
    private ContributionService contributionService;

    //private static final Logger logger = (Logger) LoggerFactory.getLogger(GeneratePdfPayroll.class);

    public  ByteArrayInputStream payrollReport(Payroll payroll, List<Prime> primes, List<Contribution> contributions, PayrollConfig payrollConfig) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            String title = "BULLETIN DE PAIE";
            Font titleFont = new Font(Font.COURIER, 20f, Font.BOLDITALIC, Color.BLUE);
            // Create a paragraph with the new font
            Paragraph paragraph = new Paragraph(title,titleFont);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
           // Create a table with 2 columns
            PdfPTable table1 = new PdfPTable(2);
            //table1.setWidths(new int[]{1, 2});
            table1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.setTotalWidth(200); // Set the width of the table
            table1.setLockedWidth(true); // Lock the width
            // Set table border width to 0 to remove inside borders
            table1.getDefaultCell().setBorderWidth(0);
            // Add cells to the table
            PdfPCell cell1 = new PdfPCell(new Phrase("Year", headFont));
            PdfPCell cell2 = new PdfPCell(new Phrase(String.valueOf(payroll.getYear())));
            PdfPCell cell3 = new PdfPCell(new Phrase("Month", headFont));
            PdfPCell cell4 = new PdfPCell(new Phrase(payroll.getMonth()));
            table1.addCell(cell1);
            table1.addCell(cell2);
            table1.addCell(cell3);
            table1.addCell(cell4);

            // Set the table's position to the top right corner


            // Eomployee info table
            PdfPTable table2 = new PdfPTable(2);
            // Set table border width to 0 to remove inside borders
            table2.getDefaultCell().setBorderWidth(0);
            // Add cells to the table
            PdfPCell table2cell1 = new PdfPCell(new Phrase("Employee", headFont));
            PdfPCell table2cell6 = new PdfPCell(new Phrase(String.valueOf(payroll.getUser().getFirstName()+" "+payroll.getUser().getLastName())));
            PdfPCell table2cell5 = new PdfPCell(new Phrase("Category", headFont));
            PdfPCell table2cell2 = new PdfPCell(new Phrase(String.valueOf(payroll.getCategory())));
            PdfPCell table2cell3 = new PdfPCell(new Phrase("Seniority", headFont));
            PdfPCell table2cell4 = new PdfPCell(new Phrase(payroll.getSeniority()));
            table2.addCell(table2cell1);
            table2.addCell(table2cell6);
            table2.addCell(table2cell5);
            table2.addCell(table2cell2);
            table2.addCell(table2cell3);
            table2.addCell(table2cell4);

            // Set the table's position to the top right corner
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table2.setTotalWidth(150); // Set the width of the table
            table2.setLockedWidth(true); // Lock the width

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 1, 3});

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Designation", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Nombre", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Base", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);


            createCell(table,"Number of days");
            createCell(table, String.valueOf(payroll.getWork_hours_number()));
            createCell(table, "");

            if(payroll.getWork_hours_number()>payrollConfig.getMonth_days()){
                createCell(table, "Extra days");
                createCell(table, String.valueOf(payroll.getWork_hours_number() - payrollConfig.getMonth_days()));
                createCell(table,"");
            }
                createCell(table,"Base Salary");
                createCell(table, "1");
                createCell(table,String.valueOf(payroll.getBase_salary()));
                createCell(table,"Brut Salary");
                createCell(table, "1");
                createCell(table, String.valueOf(payroll.getBrut_salary()));
            //Prime section
                createCell(table, "Primes");
                createCell(table, "");
                createCell(table, "");

            for (Prime prime:primes
                 ) {
                createCell(table, String.valueOf(prime.getPrime_designation()));
                createCell(table, "1");
                createCell(table, String.valueOf(prime.getValue_amount()));
            }
                //Contribution section
                createCell(table, "Contributions");
                createCell(table, "");
                createCell(table, "");
                for (Contribution contrib:contributions
                ) {
                    createCell(table, String.valueOf(contrib.getContribution_designation()));
                    createCell(table, "1");
                    createCell(table, String.valueOf(contrib.getContribution_amount()));
                }

                createCell(table, "Net Salary");
                createCell(table, "1");
                createCell(table, String.valueOf(payroll.getNet_salary()));

            //Payment method and bank table
            PdfPTable table3 = new PdfPTable(3);
            table3.setHorizontalAlignment(Element.ALIGN_LEFT);
            table3.setWidthPercentage(60);
            table3.setWidths(new int[]{2, 2, 4});

            PdfPCell hcell3;
            hcell3 = new PdfPCell(new Phrase("Bank Name", headFont));
            hcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(hcell3);

            hcell3 = new PdfPCell(new Phrase("IBAN/RIB", headFont));
            hcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(hcell3);

            hcell3 = new PdfPCell(new Phrase("Payed on", headFont));
            hcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(hcell3);

            createCell(table3, payroll.getBank_name());
            createCell(table3,String.valueOf(payroll.getAccount_number()));
            createCell(table3, "Payed on "+getLastDayOfMonth()+" by "+payroll.getPayment_method());

// Set the amount of space after the table (adjust as needed)
            Paragraph space = new Paragraph();
            space.setSpacingAfter(20);

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(paragraph);
            document.add(table1);
            document.add(table2);
            document.add(space);
            document.add(table);
            document.add(space);
            document.add(table3);
            document.close();

        } catch (DocumentException ex) {

            //logger.log(Level.INFO,"Error {}", ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static String getLastDayOfMonth(){
        LocalDate currentDate = LocalDate.now();
        // Get the last day of the current month
        LocalDate lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // Format and print the last day of the current month
        String formattedLastDayOfMonth = lastDayOfMonth.format(formatter);
        return formattedLastDayOfMonth;
    }

    private void createCell(PdfPTable table, String value){
        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingRight(5);
        table.addCell(cell);
    }
}

