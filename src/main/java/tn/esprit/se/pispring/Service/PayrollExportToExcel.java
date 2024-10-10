package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.User;
import tn.esprit.se.pispring.utils.ReportAbstract;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Slf4j
public class PayrollExportToExcel extends ReportAbstract {
    private void writePayrollTableData(Object data) {
        Set<Payroll> list = (Set<Payroll>) data;
        // font style content
        CellStyle style = getFontContentExcel();
        // starting write on row
        int startRow = 2;
        // write content
        for (Payroll payroll : list) {
            Row row = sheet.createRow(startRow++);
            int columnCount = 0;
            createCell(row, columnCount++, payroll.getMonth(), style);
            createCell(row, columnCount++, payroll.getSeniority(), style);
            createCell(row, columnCount++, payroll.getWork_hours_number(), style);
            createCell(row, columnCount++, Float.toString(payroll.getBase_salary()), style);
            createCell(row, columnCount++, Float.toString(payroll.getBrut_salary()), style);
            createCell(row, columnCount++, Float.toString(payroll.getNet_salary()), style);

        }
    }

    private void writeUsersTableData(Object data) {
        Map<String, Float> payrollList = (Map<String, Float>) data;
        // font style content
        CellStyle style = getFontContentExcel();
        // starting write on row
        AtomicInteger startRow = new AtomicInteger(2);
        // write content
        payrollList.forEach((k,v) -> {
                    Row row = sheet.createRow(startRow.getAndIncrement());
                    int columnCount = 0;
                    createCell(row, columnCount++, k, style);
                    if (v != null) {
                        createCell(row, columnCount++, Float.toString(v), style);
                    } else {
                        createCell(row, columnCount++, "N/A", style); // or any other default representation
                    }
                }
        );
    }


    public void exportToExcel(HttpServletResponse response, Object data, Map<String, Float> data1) throws IOException {
        newReportExcel();
        // response  writer to excel
        response = initResponseForExportExcel(response, "Payroll Report");
        ServletOutputStream outputStream = response.getOutputStream();
        // write sheet, title & header
        String[] firstSheetheaders = new String[]{"Employee Name", "Expenses"};
        String[] headers = new String[]{"Month", "Seniority","Working Days","Base Salary","Brut Salary","Net Salary"};
        writeTableHeaderExcel("Employee list", "Employee List", firstSheetheaders);
        // write content row
        writeUsersTableData(data1);

        List<User> list = (List<User>) data;
        for (User user: list
             ) {
            writeTableHeaderExcel(user.getFirstName()+" "+user.getLastName(), "Payroll List", headers);
            Set<Payroll> payrollList = user.getPayrolls();
            writePayrollTableData(payrollList);
        }

        createSheetLink("Employee list");

        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
