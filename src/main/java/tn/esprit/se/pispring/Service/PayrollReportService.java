package tn.esprit.se.pispring.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface PayrollReportService {
    public void exportToExcel(HttpServletResponse response, int year);
    public ByteArrayInputStream orderPaymentPdf(int year, String month, String accountNumber, String paymentDate);
}
