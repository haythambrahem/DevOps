package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.DTO.Response.PayrollDTO;
import tn.esprit.se.pispring.Service.*;
import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.PayrollConfig;
import tn.esprit.se.pispring.utils.GeneratePdfPayroll;
import tn.esprit.se.pispring.utils.SendEmail;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/Payroll")
public class PayrollController  {
    PayrollConfigService payrollConfigService;
    PayrollService payrollService;
    PrimeService primeService;
    ContributionService contributionService;
    SendEmail sendEmail;

    @GetMapping("/retrieve-all-payrolls")
    public List<PayrollDTO> getPayrolls() {
        List<PayrollDTO> listPayrolls = payrollService.retrieveAllPayrolls();
        return listPayrolls;
    }
    @PostMapping("/add-payroll")
    public Payroll addPayroll(@RequestBody Payroll payroll) {
        return payrollService.addPayroll(payroll);
    }

    @PutMapping("/update-payroll/{idpayroll}")
    public Payroll updatePayroll(@RequestBody Payroll payroll, @PathVariable Long idpayroll) {
        return payrollService.updatePayroll(payroll,idpayroll);
    }

    @PutMapping("/affect-payroll/{userId}")
    public Payroll affectPayrollUser(@RequestBody Payroll payroll, @PathVariable Long userId) {
        return payrollService.affectPayrollUser(payroll,userId);
    }

    @GetMapping("/payrolls-by-user/{userId}")
    public Set<PayrollDTO> getPayrollByUser(@PathVariable Long userId) {
        return payrollService.getPayrollsByUser(userId);
    }

    @RequestMapping(value = "/pdfpayroll/{idpayroll}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> payrollReport(@PathVariable Long idpayroll) {
        var payroll = (Payroll) payrollService.retrievePayroll(idpayroll);
        ByteArrayInputStream bis = payrollPDFOutput(payroll);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=payroll.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    @GetMapping("/retrieve-payroll/{id}")
    public Payroll getPayroll(@PathVariable Long id) {return payrollService.retrievePayroll(id);}
    @GetMapping("/get-payroll-user/{id}")
    public String getPyrollUser(@PathVariable Long id){
        return payrollService.getPayrollUser(id);
    }

    @DeleteMapping("/remove-payroll/{id}")
    public void removePayroll(@PathVariable Long id) {
        payrollService.removePayroll(id);
    }

    @RequestMapping(value = "/send-email/{idpayroll}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<String> sendEmail(@PathVariable Long idpayroll) throws MessagingException, IOException {
        var payroll = (Payroll) payrollService.retrievePayroll(idpayroll);
        ByteArrayInputStream bis = payrollPDFOutput(payroll);
        String emailBody = "Please find attached your payslip";
        sendEmail.sendEmailWithAttachment(
                bis,
                payroll.getUser().getEmail(),
                "BULLETIN DE PAIE " +payroll.getMonth()+" "+ payroll.getYear(),
                emailBody
        );

        return ResponseEntity
                .ok("");

    }

    ByteArrayInputStream payrollPDFOutput(Payroll payroll){
        var primes = primeService.getListPrimeByUserAndMonth(payroll.getUser(),payroll.getMonth(), payroll.getYear());
        var contributions = contributionService.getListContributionByUserAndMonth(payroll.getUser(),payroll.getMonth(), payroll.getYear());
        GeneratePdfPayroll generatePdfPayroll = new GeneratePdfPayroll();
        PayrollConfig payrollConfig = payrollConfigService.retrievePayrollConfig(1L);
        return generatePdfPayroll.payrollReport(payroll,primes, contributions, payrollConfig);
    }
    @GetMapping("/total-expenses")
    public ResponseEntity<Map<String, Float>> getTotalExpenses(@RequestParam int year) {
        Map<String, Float> monthlyExpenses = payrollService.calculateTotalExpensesByMonth(year);
        return ResponseEntity.ok(monthlyExpenses);
    }
    @GetMapping("/expenses-per-user")
    public ResponseEntity<Map<String, Float>> getTotalExpensesByUser(@RequestParam int year) {
        Map<String, Float> monthlyExpenses = payrollService.calculateTotalExpensesByUser(year);
        return ResponseEntity.ok(monthlyExpenses);
    }

    @GetMapping("/expenses-per-year")
    public ResponseEntity<Map<Integer, Double>> getTotalExpensesByYear(@RequestParam int startYear, @RequestParam int endtYear) {
        Map<Integer, Double> monthlyExpenses = payrollService.getTotalExpensesByYearRange(startYear,endtYear);
        return ResponseEntity.ok(monthlyExpenses);
    }

}

