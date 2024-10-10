package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Contribution;
import tn.esprit.se.pispring.entities.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface ContributionRepository extends JpaRepository<Contribution,Long> {
    @Query("SELECT SUM(c.contribution_amount) FROM Contribution c WHERE c.user.id = :userId AND c.contribution_month = :month AND c.contribution_year = :year")
    Float getSumAmountForUserMonthYear(@Param("userId") Long userId, @Param("month") String month, @Param("year") Integer year);

    @Query("SELECT c FROM Contribution c WHERE c.user = :user AND c.contribution_month = :contributionMonth AND c.contribution_year = :contributionYear")
    List<Contribution> findByUserAndContributionMonthAndContributionYear(@Param("user") User user, @Param("contributionMonth") String contributionMonth, @Param("contributionYear") Integer contributionYear);
}
