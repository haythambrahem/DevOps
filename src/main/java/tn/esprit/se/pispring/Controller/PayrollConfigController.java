package tn.esprit.se.pispring.Controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.PayrollConfigService;
import tn.esprit.se.pispring.entities.PayrollConfig;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/payroll-config")
public class PayrollConfigController {
    PayrollConfigService payrollConfigService;

    @PutMapping("/update-config/{id}")
    public PayrollConfig updatePayrollConfig(@RequestBody PayrollConfig payrollConfig, @PathVariable Long id) {
        return payrollConfigService.updatePayrollConfig(payrollConfig, id);
    }
    @GetMapping("/retrieve-config/{id}")
    public PayrollConfig getPayrollConfig(@PathVariable Long id) {return payrollConfigService.retrievePayrollConfig(id);}

}
