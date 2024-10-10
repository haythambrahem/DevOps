package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Contribution;
import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.PayrollConfig;
import tn.esprit.se.pispring.entities.Prime;

public interface PayrollConfigService {
    PayrollConfig updatePayrollConfig(PayrollConfig payrollConfig, Long idPayrollConfig);
    public PayrollConfig retrievePayrollConfig(Long idPayrollConfig);

}
