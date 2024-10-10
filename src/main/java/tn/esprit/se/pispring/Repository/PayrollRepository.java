package tn.esprit.se.pispring.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.User;

import java.util.List;
@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {
    @Query("SELECT p FROM Payroll p WHERE p.year = :year AND p.month = :month")
    List<Payroll> findByPayrollDateYearAndPayrollDateMonth(@Param("year") Integer year, @Param("month") String month);

    @Query("SELECT SUM(p.brut_salary) FROM Payroll p WHERE p.year = :year AND p.month = :month")
    Float calculateTotalExpensesByYearAndMonth(@Param("year") Integer year, @Param("month") String month);

    @Query("SELECT SUM(p.brut_salary) FROM Payroll p WHERE p.year = :year AND p.user = :user")
    Float calculateTotalExpensesByYearAndUser(@Param("year") Integer year, @Param("user") User user);
    @Query("SELECT p.year, SUM(p.brut_salary) FROM Payroll p WHERE p.year BETWEEN :startYear AND :endYear GROUP BY p.year")
    List<Object[]> calculateTotalExpensesByYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);


    List<Payroll> findByUserId(Long id);
}
