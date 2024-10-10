package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.DTO.Response.PayrollDTO;
import tn.esprit.se.pispring.entities.Payroll;


import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PayrollService {
    List<PayrollDTO> retrieveAllPayrolls();
    Set<PayrollDTO> getPayrollsByUser(Long userId);
    public String getPayrollUser(Long idpayroll);
    List<Payroll> getPayrollByYearAndMonth(int year, String month);

    Payroll addPayroll(Payroll payroll);

    Payroll updatePayroll(Payroll payroll, Long idpayroll);

    Payroll retrievePayroll(Long idPayroll);
    public Payroll affectPayrollUser(Payroll payroll, Long userId);
    void removePayroll(Long idPayroll);
    public Map<String, Float> calculateTotalExpensesByMonth(int year);

    public Map<String, Float> calculateTotalExpensesByUser(int year);
    public Map<Integer, Double> getTotalExpensesByYearRange(Integer startYear, Integer endYear);
    //void generatePaymentOrder(Payroll payroll);


}
