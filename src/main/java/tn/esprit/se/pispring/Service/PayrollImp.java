package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.DTO.Response.PayrollDTO;
import tn.esprit.se.pispring.Repository.PayrollRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.PayrollConfig;
import tn.esprit.se.pispring.entities.Prime;
import tn.esprit.se.pispring.entities.User;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class PayrollImp implements PayrollService {
    PayrollRepository payrollRepository;
    UserRepository userRepository;
    ContributionService contributionService;
    PrimeService primeService;
    PayrollConfigService payrollConfigService;


    @Override
    public List<PayrollDTO> retrieveAllPayrolls() {
        List<Payroll> payrollList = payrollRepository.findAll();
        List<PayrollDTO> payrollDTOList = new ArrayList<>();
        for (Payroll payroll: payrollList
             ) {
            PayrollDTO payrollDTO = PayrollDTO.builder()
                    .payroll_id(payroll.getPayroll_id())
                    .net_salary(payroll.getNet_salary())
                    .month(payroll.getMonth())
                    .category(payroll.getCategory())
                    .seniority(payroll.getSeniority())
                    .year(payroll.getYear())
                    .bank_name(payroll.getBank_name())
                    .account_number(payroll.getAccount_number())
                    .base_salary(payroll.getBase_salary())
                    .payment_method(payroll.getPayment_method())
                    .brut_salary(payroll.getBrut_salary())
                    .work_hours_number(payroll.getWork_hours_number())
                    .user_name(payroll.getUser().getFirstName()+" "+payroll.getUser().getLastName())
                            .build();
            payrollDTOList.add(payrollDTO);
        }
        return payrollDTOList;
    }

    @Override
    public Set<PayrollDTO> getPayrollsByUser(Long userId) {
        User user = userRepository.findById(userId).get();
        Set<Payroll> payrollSet = user.getPayrolls();
        Set<PayrollDTO> payrollDTOList = new HashSet<>();
        for (Payroll payroll: payrollSet
        ) {
            PayrollDTO payrollDTO = PayrollDTO.builder()
                    .payroll_id(payroll.getPayroll_id())
                    .net_salary(payroll.getNet_salary())
                    .month(payroll.getMonth())
                    .category(payroll.getCategory())
                    .seniority(payroll.getSeniority())
                    .year(payroll.getYear())
                    .bank_name(payroll.getBank_name())
                    .account_number(payroll.getAccount_number())
                    .base_salary(payroll.getBase_salary())
                    .payment_method(payroll.getPayment_method())
                    .brut_salary(payroll.getBrut_salary())
                    .work_hours_number(payroll.getWork_hours_number())
                    .user_name(payroll.getUser().getFirstName()+" "+payroll.getUser().getLastName())
                    .build();
            payrollDTOList.add(payrollDTO);
        }
        return payrollDTOList;
    }

    public String getPayrollUser(Long idpayroll) {
        Payroll p = payrollRepository.findById(idpayroll).get();
        User user = p.getUser();
        return user.getFirstName()+" "+user.getLastName();
    }

    @Override
    public List<Payroll> getPayrollByYearAndMonth(int year, String month) {
        List<Payroll> payrolls = payrollRepository.findByPayrollDateYearAndPayrollDateMonth(year, month);
        return payrolls;
    }

    @Override
    public Payroll addPayroll(Payroll payroll) {
        return payrollRepository.save(payroll);
    }
    @Override
    public Payroll updatePayroll(Payroll payroll,  Long idpayroll)
    {
        Payroll p = payrollRepository.findById(idpayroll).get();
        if (p != null){
            payroll.setPayroll_id(idpayroll);
            return payrollRepository.save(payroll);
        }
        return null;
    }

    @Override
    public Payroll retrievePayroll(Long idPayroll) {
        return payrollRepository.findById(idPayroll).get();
    }

    @Override
    public void removePayroll(Long idPayroll) {
        payrollRepository.deleteById(idPayroll);
    }

    @Override
    public Map<String, Float> calculateTotalExpensesByMonth(int year) {
        Map<String, Float> monthlyExpenses = new LinkedHashMap<>();

        for (int month = 1; month <= 12; month++) {
            String monthName = getMonthName(month);
            Float totalExpenses = payrollRepository.calculateTotalExpensesByYearAndMonth(year, monthName);
            monthlyExpenses.put(monthName, totalExpenses);
        }

        return monthlyExpenses;
    }
    public Map<String, Float> calculateTotalExpensesByUser(int year) {
        Map<String, Float> employeeExpenses = new LinkedHashMap<>();
        List<User> users = userRepository.findAll();
        for (User user: users
        ) {
            Float totalExpense = payrollRepository.calculateTotalExpensesByYearAndUser(year, user);
            employeeExpenses.put(user.getFirstName()+" "+user.getLastName(), totalExpense);
        }
        return employeeExpenses;
    }
    private String getMonthName(int month) {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }


    public Payroll affectPayrollUser(Payroll payroll, Long userId) {
        User user = userRepository.findById(userId).get();
        Float prime = primeService.getSumAmountForUserMonthYear(userId,payroll.getMonth(), payroll.getYear());
        Float contrib = contributionService.getSumAmountForUserMonthYear(userId,payroll.getMonth(), payroll.getYear());
        Double netSalary = calculateNetSalary(payroll.getBrut_salary(),payroll.getWork_hours_number(),prime, contrib);
        payroll.setNet_salary(netSalary.floatValue());
        payroll.setBase_salary(Float.valueOf(user.getSalaire()));
        payroll.setUser(user);
        payrollRepository.save(payroll);
        return payroll;
    }
    private Double calculateNetSalary(float brutSalary, int totalHoursWorked, float prime, float deductions){
        PayrollConfig payrollConfig = payrollConfigService.retrievePayrollConfig(1L);
        // Calcul du salaire horaire
        float dailyRate = brutSalary / payrollConfig.getMonth_days(); // 22 jours de travail par mois, 8 heures par jour
        // Calcul du salaire brut pour le mois entier
        float monthlySalary = totalHoursWorked * dailyRate;
        // Ajout des primes
        monthlySalary += prime;
        // Soustraction des cotisations
        monthlySalary -= deductions;
        // Calcul du salaire net
        double netSalary = monthlySalary * payrollConfig.getFees_rate(); // 22% de déduction pour les cotisations
        return netSalary;
    }

    public Map<Integer, Double> getTotalExpensesByYearRange(Integer startYear, Integer endYear) {
        List<Object[]> expensesByYear = payrollRepository.calculateTotalExpensesByYearRange(startYear, endYear);
        Map<Integer, Double> expensesMap = new HashMap<>();
        for (Object[] result : expensesByYear) {
            Integer year = (Integer) result[0];
            Double totalExpenses = (Double) result[1];
            expensesMap.put(year, totalExpenses);
        }
        return expensesMap;
    }



}

