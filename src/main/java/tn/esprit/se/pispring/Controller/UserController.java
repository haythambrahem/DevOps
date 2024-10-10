
package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.DTO.Request.*;
import tn.esprit.se.pispring.DTO.Response.*;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.RoleService;
import tn.esprit.se.pispring.Service.UserService;
import tn.esprit.se.pispring.entities.Role;
import tn.esprit.se.pispring.entities.ERole;
import tn.esprit.se.pispring.entities.TaskStatus;
import tn.esprit.se.pispring.entities.User;
import tn.esprit.se.pispring.events.NewUserAddedEvent;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins ="http://localhost:4200")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Value("${asserter.default.secret}")
    private String DEFAULT_PASSWORD;
    private final UserService userService;
    private final UserRepository userRepo;
    private final ApplicationEventPublisher publisher;
    private final RoleService roleService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public ResponseEntity<?> createNewUser(final HttpServletRequest request, @RequestHeader(name = "Authorization") String token, @RequestBody @Valid UserRequest userRequest) throws Exception {
        try {
            User newUser = userService.createNewUser(token, userRequest);
            if (newUser != null) {
                publisher.publishEvent(new NewUserAddedEvent(applicationUrl(request), newUser, DEFAULT_PASSWORD));

            }
            Role role = roleService.findRoleByRoleName(ERole.ROLE_USER);
            assert newUser != null;
            return ResponseEntity.ok(new UserResponse(newUser.getId(),newUser.getFirstName(), newUser.getFirstName(), newUser.getEmail(),  newUser.getTelephone(),newUser.getRoles().stream().map(role1 -> role1.getRoleName().toString()).toList()));

        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/getCurrent")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN', 'ROLE_HRE')")
    public ResponseEntity<?> getCurrentUserInfos(@RequestHeader(name = "Authorization") String token) throws Exception {
        try {
            log.error(token);
            log.error("#######################");
            log.error("####################3##");
            CurrentUserResponse user = userService.getCurrentUserInfos(token);
            return ResponseEntity.ok(user);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }

//    @GetMapping("/getCurrent")
//    public ResponseEntity<?> test () {
//        return ResponseEntity.ok("hello word");
//    }

    @PutMapping("/editCurrent")
    public ResponseEntity<?> editCurrent(@RequestHeader(name = "Authorization") String token, @RequestBody CurrentUserRequest request) throws Exception {
        try {
            CurrentUserResponse user = userService.editCurrentUserInfos(token, request);
            return ResponseEntity.ok(user);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @PostMapping("/editPassword")
    public ResponseEntity<?> editPassword(@RequestHeader("Authorization") String token, @RequestBody EditPasswordRequest request) throws Exception {
        try {
            String result = userService.editPassword(token, request);
            return ResponseEntity.ok(result);
        }catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/alll")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public ResponseEntity<?> getUsers(@RequestHeader(name = "Authorization") String token) throws Exception {
        try {
            List<UserResponse> users = userService.getUsers(token);
            return ResponseEntity.ok(users);
        }catch (Exception e) {
            throw new Exception(e);
        }

    }

    @PostMapping("/search")
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public ResponseEntity<?> searchUsers(@RequestHeader(name = "Authorization") String token, @RequestBody SearchRequest searchRequest) throws Exception {

        try {

            List<UserResponse> users = userService.searchUsers(token, searchRequest);
            return ResponseEntity.ok(users);


        }catch (Exception e) {
            throw new Exception(e);
        }


    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public Iterable<User> findConnectedUsers() {
        return userRepo.findAll();
    }

    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
    }

    @PostMapping("/delete")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestBody @Valid DeleteUsersRequest deleteUsersRequest) throws Exception {

        try {
            userService.deleteUsers(deleteUsersRequest);

            return ResponseEntity.ok("success deleting users");
        }catch (Exception e) {
            throw new Exception(e);
        }

    }

//    @GetMapping("/{userId}/tasks-by-status")
//    public ResponseEntity<Map<TaskStatus, Integer>> getTasksByStatus(@PathVariable Long userId) {
//        Map<TaskStatus, Integer> tasksByStatus = userService.getTasksByStatus(userId);
//        if (tasksByStatus == null) {
//            // Handle case where user is not found
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(tasksByStatus, HttpStatus.OK);
//    }

    //    @GetMapping("/users-per-project-and-tasks")
//    public ResponseEntity<List<ProjectUserTask>> getUsersPerProjectAndTasks() {
//        List<ProjectUserTask> result = userService.getUsersPerProjectAndTasks();
//        return ResponseEntity.ok(result);
//    }
    @GetMapping("/user-task-status")
    public ResponseEntity<List<UserTaskCountDTO>> getUsersWithTaskStatus() {
        List<UserTaskCountDTO> userTaskCountDTOs = userService.getUsersWithTaskStatus();
        return new ResponseEntity<>(userTaskCountDTOs, HttpStatus.OK);
    }

    @GetMapping("/task-status")
    public ResponseEntity<Map<String, Object>> getUsersTaskStatus() {
        Map<String, Object> result = userService.getUsersTaskStatus();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/tasks-by-status")
    public ResponseEntity<List<UserTasksDTO>> getUsersTasksWithCount() {
        List<UserTasksDTO> usersTasksDTOList = userService.getUsersTasksWithCount();
        return new ResponseEntity<>(usersTasksDTOList, HttpStatus.OK);
    }

    @GetMapping("/retrieve-users")
    public List<User> getLeaves() {
        List<User> listUsers = userService.getAllUsers();
        return listUsers;
    }

    @GetMapping("/get/{id}")
    User getUser(@PathVariable("id") Long id)
    {
        return userService.retrieveUser(id);
    }

    @GetMapping("/retrieve-all-users")
    public List<User> getAllUsers() {
        List<User> listUsers = userService.getAllUsers();
        return listUsers;
    }

    @GetMapping("/getIdByEmail/{email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable String email) {
        Long userId = userRepo.getUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }

}