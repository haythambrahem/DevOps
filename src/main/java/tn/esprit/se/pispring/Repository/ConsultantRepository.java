package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.se.pispring.entities.Consultant;

import java.util.List;

public interface ConsultantRepository extends JpaRepository<Consultant,Long> {

    @Query("SELECT c FROM Consultant c ORDER BY c.consultant_firstname")
    List<Consultant> findAllByOrderByConsultantFirstname();

    @Query("SELECT c FROM Consultant c ORDER BY c.consultant_lastname")
    List<Consultant> findAllByOrderByConsultantLastname();
    List<Consultant> findAllByOrderByHireDate();

    List<Consultant> findAllByOrderBySkill();

    @Query("SELECT c FROM Consultant c ORDER BY c.consultant_id")
    List<Consultant> findAllByOrderByID();

    List<Consultant> findAllByOrderBySkillAscHireDateAsc();
}
