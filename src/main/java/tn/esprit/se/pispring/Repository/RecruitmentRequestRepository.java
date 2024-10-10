package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.RecruitmentRequest;

public interface RecruitmentRequestRepository extends JpaRepository<RecruitmentRequest, Long> {

}

