package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.ERole;
import tn.esprit.se.pispring.entities.Role;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(ERole eRole);

    Optional<Role> findById(Long role);
}
