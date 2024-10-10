package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Prime;
import tn.esprit.se.pispring.entities.User;

import java.util.List;

@Repository
public interface PrimeRepository extends JpaRepository<Prime,Long> {
    @Query("SELECT SUM(p.value_amount) FROM Prime p WHERE p.user.id = :userId AND p.prime_month = :month AND p.prime_year = :year")
    Float getSumAmountForUserMonthYear(@Param("userId") Long userId, @Param("month") String month, @Param("year") Integer year);

    @Query("SELECT p FROM Prime p WHERE p.user = :user AND p.prime_month = :primeMonth AND p.prime_year = :primeYear")
    List<Prime> findPrimesByUserAndMonthAndYear(@Param("user") User user, @Param("primeMonth") String primeMonth, @Param("primeYear") Integer primeYear);

}
