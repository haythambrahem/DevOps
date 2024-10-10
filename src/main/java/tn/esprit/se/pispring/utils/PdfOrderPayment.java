package tn.esprit.se.pispring.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.entities.Payroll;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
@Service
@AllArgsConstructor
@Slf4j
public class PdfOrderPayment {
    public ByteArrayInputStream generatePdfOrderPayment (Object data, String accountNumber, String paymentDate){
        float total = 0.0f;
        List<Payroll> list = (List<Payroll>) data;
        Document document = new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        // Create a paragraph with the new font
        Paragraph title = new Paragraph("Monsieur directeur de la banque",headFont);
        title.setAlignment(Element.ALIGN_CENTER);
        Paragraph object = new Paragraph("Objet: ",headFont);
        Paragraph compte = new Paragraph("Compte N°: "+accountNumber,headFont);
        String bodyHeaderMessage = "Nous vous demande prions d'executer le virement détaillé comme suit du moi:  ";
        Paragraph bodyHeader = new Paragraph(bodyHeaderMessage+paymentDate,headFont);
        String bodyFooterMessage = "Nous vous remercions par avance et vous prions d'agréer monsieur, l'expression de notre salutation distinguée";
        Paragraph bodyFooter = new Paragraph(bodyFooterMessage,headFont);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 2, 2, 2});

        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Nom et Prénom", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Motif", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("N° de compte", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Montant", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);
        for (Payroll payroll: list
             ) {
                createCell(table, String.valueOf(payroll.getUser().getLastName()+" "+payroll.getUser().getFirstName()));
                createCell(table, "Règlement salaire");
                createCell(table, String.valueOf(payroll.getAccount_number()));
                createCell(table, String.valueOf(payroll.getNet_salary()));
                total += payroll.getNet_salary();
        }

                createCell(table,"Total");
                createCell(table,"");
                createCell(table,"");
                createCell(table,String.valueOf(total));

        Paragraph space = new Paragraph();
        space.setSpacingAfter(20);

        PdfWriter.getInstance(document, out);
        document.open();
        document.add(title);
        document.add(space);
        document.add(space);
        document.add(object);
        document.add(compte);
        document.add(bodyHeader);
        document.add(space);
        document.add(table);
        document.add(space);
        document.add(bodyFooter);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());

    }

    private void createCell(PdfPTable table, String value){
        PdfPCell cell = new PdfPCell(new Phrase(value));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPaddingRight(5);
        table.addCell(cell);
    }
}
