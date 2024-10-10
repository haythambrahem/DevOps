package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.Portfolio;

public interface PortfilioRepository extends JpaRepository<Portfolio,Long> {
}
