package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.Absence;

public interface AbsenceRepository extends JpaRepository<Absence,Long> {
}
