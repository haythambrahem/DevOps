package tn.esprit.se.pispring.startup;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.esprit.se.pispring.Repository.PermissionRepo;
import tn.esprit.se.pispring.Repository.RoleRepo;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.ERole;
import tn.esprit.se.pispring.entities.Permission;
import tn.esprit.se.pispring.entities.Role;

import tn.esprit.se.pispring.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static tn.esprit.se.pispring.entities.EPermission.ROLE_ADMIN_READ;
import static tn.esprit.se.pispring.entities.ERole.ROLE_ADMIN;

@Component
@Slf4j
public class runner implements CommandLineRunner {


    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

      List<Role> roles = roleRepo.findAll();
      if (roles.isEmpty()) {
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(ROLE_ADMIN);
        roleRepo.save(roleAdmin);

        Role roleUser = new Role();
        roleUser.setRoleName(ERole.ROLE_USER);
        roleRepo.save(roleUser);

        Role roleHR = new Role();
        roleHR.setRoleName(ERole.ROLE_HRE);
        roleRepo.save(roleHR);

        Role roleHRAdmin = new Role();
        roleHRAdmin.setRoleName(ERole.ROLE_HR_ADMIN);
        roleRepo.save(roleHRAdmin);

        Role roleCRM = new Role();
        roleCRM.setRoleName(ERole.ROLE_CRME);
        roleRepo.save(roleCRM);

        Role roleCRMAdmin = new Role();
        roleCRMAdmin.setRoleName(ERole.ROLE_CRM_ADMIN);
        roleRepo.save(roleCRMAdmin);

        Role roleProject = new Role();
        roleProject.setRoleName(ERole.ROLE_PROJECT);
        roleRepo.save(roleProject);

        Role roleProjectAdmin = new Role();
        roleProjectAdmin.setRoleName(ERole.ROLE_PROJECT_ADMIN);
        roleRepo.save(roleProjectAdmin);

        Role roleProduct = new Role();
        roleProduct.setRoleName(ERole.ROLE_PRODUCT);
        roleRepo.save(roleProduct);

        Role roleProductAdmin = new Role();
        roleProductAdmin.setRoleName(ERole.ROLE_PRODUCT_ADMIN);
        roleRepo.save(roleProductAdmin);

        printLogs("DONE ADDING ROLES");
      }

      if (userRepo.findAll().isEmpty()) {
        // Add a new Admin
        User admin = new User();
        admin.setFirstName("first");
        admin.setLastName("admin");
        admin.setPassword(passwordEncoder.encode("123456789"));
        List<Role> roless = new ArrayList<>();
        roless.add(roleRepo.findRoleByRoleName(ROLE_ADMIN));
        admin.setEmail("adming@email.com");
        admin.setRoles(roless);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(permissionRepo.findPermissionByPermissionName(ROLE_ADMIN_READ));
        admin.setPermissions(permissions);
        
        userRepo.save(admin);


        // Add a new HR_ADMIN
        User hrAdmin = new User();
        hrAdmin.setFirstName("first hr");
        hrAdmin.setLastName("admin");
        hrAdmin.setPassword(passwordEncoder.encode("123456789"));
        List<Role> rolesss = new ArrayList<>();
        rolesss.add(roleRepo.findRoleByRoleName(ERole.ROLE_HR_ADMIN));
        hrAdmin.setRoles(rolesss);
        hrAdmin.setEmail("hrAdmin@email.com");
        userRepo.save(hrAdmin);
//HR_ROLE
        User hr = new User();
        hr.setFirstName("first hr");
        hr.setLastName("hr");
        hr.setPassword(passwordEncoder.encode("123456789"));
        List<Role> roleHr = new ArrayList<>();
        roleHr.add(roleRepo.findRoleByRoleName(ERole.ROLE_HRE));
        hr.setRoles(roleHr);
        hr.setEmail("hr@email.com");
        userRepo.save(hr);

// Add a new HRE_ADMIN

        User hr1 = new User();
        hr1.setFirstName("first project user");
        hr1.setLastName("pr");
        hr1.setPassword(passwordEncoder.encode("123456789"));
        List<Role> roleHr1 = new ArrayList<>();
        roleHr1.add(roleRepo.findRoleByRoleName(ERole.ROLE_PROJECT));
        hr1.setRoles(roleHr);
        hr1.setEmail("pr@email.com");
        userRepo.save(hr1);
        // Add a list of users

        // Add a PRODUCT_ADMIN

        User pAdmin = new User();
        pAdmin.setFirstName("first product");
        pAdmin.setLastName("admin");
        pAdmin.setPassword(passwordEncoder.encode("123456789"));
        List<Role> rolessss = new ArrayList<>();
        rolessss.add(roleRepo.findRoleByRoleName(ERole.ROLE_PRODUCT_ADMIN));
        pAdmin.setRoles(rolessss);
        pAdmin.setEmail("pAdmin@email.com");
        userRepo.save(pAdmin);

        // Add a project admin

        User projAdmin = new User();
        projAdmin.setFirstName("project");
        projAdmin.setLastName("admin");
        projAdmin.setPassword(passwordEncoder.encode("123456789"));
        List<Role> rolesssss = new ArrayList<>();
        rolesssss.add(roleRepo.findRoleByRoleName(ERole.ROLE_PROJECT_ADMIN));
        projAdmin.setRoles(rolesssss);
        projAdmin.setEmail("projAdmin@email.com");
        userRepo.save(projAdmin);
        User crmAdmin = new User();
        crmAdmin.setFirstName("crm");
        crmAdmin.setLastName("admin");
        crmAdmin.setPassword(passwordEncoder.encode("123456789"));
        List<Role> rolessssss = new ArrayList<>();
        rolessssss.add(roleRepo.findRoleByRoleName(ERole.ROLE_CRM_ADMIN));
        crmAdmin.setRoles(rolessssss);
        crmAdmin.setEmail("crmAdmin@email.com");
        userRepo.save(crmAdmin);
        // Add a list of users


        IntStream.rangeClosed(1, 15).boxed().forEach(num -> {
          User user = new User();
          user.setFirstName("first test");
          user.setLastName("user " + num);
          user.setEmail("user"+num+"@email.com");
          user.setPassword(passwordEncoder.encode("123456789"));
          List<Role> rs = new ArrayList<>();
          rs.add(roleRepo.findRoleByRoleName(ERole.ROLE_USER));
          user.setRoles(rs);
          userRepo.save(user);
        });

        printLogs("DONE ADDING USERS");


      }

    }

  private void printLogs(String logging) {
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("=================================");
    log.info(logging);
    log.info("=================================");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
    log.info("#################################");
  }





}
