package tn.esprit.se.pispring.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;


public interface PayrollReportService {
     void exportToExcel(HttpServletResponse response, int year);
     ByteArrayInputStream orderPaymentPdf(int year, String month, String accountNumber, String paymentDate);
}
