package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.ERole;
import tn.esprit.se.pispring.entities.Role;

public interface RoleService {
    Role findRoleByRoleName(ERole roleUser) throws Exception;

}
