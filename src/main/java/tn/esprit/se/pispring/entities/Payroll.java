package tn.esprit.se.pispring.entities;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.math.BigInteger;

@Builder
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payroll {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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
    @ManyToOne
    @JsonBackReference
    User user;


}
