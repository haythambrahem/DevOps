package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.PayrollConfig;
@Repository
public interface PayrollConfigRepository extends JpaRepository<PayrollConfig,Long> {
}
