package tn.esprit.se.pispring.Controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.DTO.Request.AddUserRequest;
import tn.esprit.se.pispring.DTO.Response.PageResponse;
import tn.esprit.se.pispring.DTO.Response.UserResponse;
import tn.esprit.se.pispring.Service.UserService;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;




    @GetMapping("")
    public ResponseEntity<?> findAll(@RequestParam(name = "page") Long page, @RequestParam(name = "size") Long size, @RequestParam(name = "criteria", defaultValue = "firstName") String criteria, @RequestParam(value = "direction", defaultValue = "asc") String direction, @RequestParam(name = "search", defaultValue = "") String searchTerm) throws Exception {
        try {
            System.out.println("this is the page");
            System.out.println(page);
            System.out.println("this is the size");
            System.out.println(size);
            System.out.println("this is the criteria");
            System.out.println(criteria);
            System.out.println("this is the direction");
            System.out.println(direction);
            System.out.println("this is the searchTerm");
            System.out.println(searchTerm);
            PageResponse<UserResponse> result = userService.findAll(page, size, criteria, direction, searchTerm);
            return ResponseEntity.ok(result);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }



    @PostMapping("")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public ResponseEntity<?> addUser(@RequestBody AddUserRequest request) throws Exception {
        try {


            UserResponse result = userService.addUser(request);



            return ResponseEntity.ok(result);


        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PostMapping("/ban")
    public ResponseEntity<?> banUserByEmail(@RequestParam("email") String email) {

        userService.banUserByEmail(email);


        return ResponseEntity.ok("User with email " + email + " has been banned.");
    }

    @GetMapping("/stats/banned-users")
    public ResponseEntity<Long> getBannedUsersStats() {

        long totalBannedUsers = userService.getTotalBannedUsers();


        return ResponseEntity.ok(totalBannedUsers);
    }






}
