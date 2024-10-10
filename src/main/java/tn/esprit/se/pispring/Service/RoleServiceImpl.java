package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.RoleRepo;
import tn.esprit.se.pispring.entities.ERole;
import tn.esprit.se.pispring.entities.Role;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService{

    private final RoleRepo roleRepository;


    @Override
    public Role findRoleByRoleName(ERole roleStaff) throws Exception {

        try {

            return roleRepository.findRoleByRoleName(roleStaff);

        }catch (Exception e) {
            throw new Exception(e);
        }

    }
}
