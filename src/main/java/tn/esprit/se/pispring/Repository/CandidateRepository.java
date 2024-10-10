package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("SELECT COUNT(c) FROM Candidate c WHERE c.recruitment.offerId = :offerId")
    int countCandidatesByRecruitment(Long offerId);
}
