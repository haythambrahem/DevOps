package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.PayrollReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class PayrollReportController {

    PayrollReportService payrollReportService;
    @GetMapping(value = "/pdf/order-payment", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> orderPaymentPDF(
            @RequestParam int year, @RequestParam String month, @RequestParam String accountNumber, @RequestParam String paymentDate)
            throws IOException {
        ByteArrayInputStream bis = this.payrollReportService.orderPaymentPdf(year, month, accountNumber, paymentDate);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=order-payroll.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
    @GetMapping("/excel/all")
    public void exportToExcel(HttpServletResponse response, @RequestParam int year) throws IOException {
        this.payrollReportService.exportToExcel(response, year);
    }
}
