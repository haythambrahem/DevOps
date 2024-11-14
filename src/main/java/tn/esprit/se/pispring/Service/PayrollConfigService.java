package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.PayrollConfig;


public interface PayrollConfigService {
    PayrollConfig updatePayrollConfig(PayrollConfig payrollConfig, Long idPayrollConfig);
    PayrollConfig retrievePayrollConfig(Long idPayrollConfig);

}
