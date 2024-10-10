package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.CustomerTracking;

import java.util.List;

public interface CustomerTrackingRepository extends JpaRepository<CustomerTracking,Long> {
    @Query("SELECT ct FROM CustomerTracking ct " +
            "JOIN ct.user u " +
            "JOIN u.portfolio p " +
            "JOIN p.consultant c " +
            "WHERE c.consultant_id = :consultantId")
    List<CustomerTracking> findByConsultantId(@Param("consultantId") Long consultantId);
}
