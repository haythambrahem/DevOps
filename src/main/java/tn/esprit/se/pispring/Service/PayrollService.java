package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.DTO.Response.PayrollDTO;
import tn.esprit.se.pispring.entities.Payroll;


import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PayrollService {
    List<PayrollDTO> retrieveAllPayrolls();
    Set<PayrollDTO> getPayrollsByUser(Long userId);
   String getPayrollUser(Long idpayroll);
    List<Payroll> getPayrollByYearAndMonth(int year, String month);

    Payroll addPayroll(Payroll payroll);

    Payroll updatePayroll(Payroll payroll, Long idpayroll);

    Payroll retrievePayroll(Long idPayroll);
    Payroll affectPayrollUser(Payroll payroll, Long userId);
    void removePayroll(Long idPayroll);
     Map<String, Float> calculateTotalExpensesByMonth(int year);

    Map<String, Float> calculateTotalExpensesByUser(int year);
     Map<Integer, Double> getTotalExpensesByYearRange(Integer startYear, Integer endYear);



}
