package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.EPermission;
import tn.esprit.se.pispring.entities.Permission;
import tn.esprit.se.pispring.entities.Role;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {
    Permission findPermissionByPermissionName(EPermission ePermission);
}
