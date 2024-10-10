package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.PayrollConfigRepository;
import tn.esprit.se.pispring.entities.PayrollConfig;
@Service
@AllArgsConstructor
@Slf4j
public class PayrollConfigImp implements  PayrollConfigService{
    PayrollConfigRepository payrollConfigRepository;
    @Override
    public PayrollConfig updatePayrollConfig(PayrollConfig payrollConfig, Long idPayrollConfig) {
        PayrollConfig p = payrollConfigRepository.findById(idPayrollConfig).get();
        if (p != null){
            payrollConfig.setId(idPayrollConfig);
            return payrollConfigRepository.save(payrollConfig);
        }
        return null;
    }

    @Override
    public PayrollConfig retrievePayrollConfig(Long idPayrollConfig) {
        return payrollConfigRepository.findById(idPayrollConfig).get();
    }
}
