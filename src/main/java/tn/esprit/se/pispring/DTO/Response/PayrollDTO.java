package tn.esprit.se.pispring.DTO.Response;

import lombok.*;
import tn.esprit.se.pispring.entities.PaymentMethod;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigInteger;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayrollDTO {
    private Long payroll_id;
    private String seniority;
    private String category;
    private Float base_salary;
    private Integer year;
    private String month;
    private Float brut_salary;
    private Float net_salary;
    private Integer work_hours_number;
    private String bank_name;
    private BigInteger account_number;
    @Enumerated(EnumType.STRING)
    private PaymentMethod payment_method;
    String user_name;
}
