//package com.example.devopstestunitairesuser;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//import tn.esprit.se.pispring.PiSpringApplication;
//import tn.esprit.se.pispring.Repository.RoleRepo;
//import tn.esprit.se.pispring.Repository.UserRepository;
//import tn.esprit.se.pispring.Service.UserImp;
//import tn.esprit.se.pispring.entities.*;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = PiSpringApplication.class)
//@Transactional // Ensures database changes are rolled back after each test
//class UserServiceImplJunitTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepo roleRepo;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private UserImp userService;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize the userService with injected dependencies
//        userService = new UserImp(userRepository, passwordEncoder, roleRepo, null);
//    }
//
//    @Test
//    void testGetUsersTaskStatus_AllTasksCompleted() {
//        // Create and save ROLE_USER
//        Role roleUser = new Role();
//        roleUser.setRoleName(ERole.ROLE_USER);
//        roleRepo.save(roleUser);
//
//        // Create completed tasks
//        Task completedTask1 = new Task();
//        completedTask1.setTaskStatus(TaskStatus.COMPLETED);
//
//        Task completedTask2 = new Task();
//        completedTask2.setTaskStatus(TaskStatus.COMPLETED);
//
//        // Create a user with ROLE_USER and all completed tasks
//        User user1 = new User();
//        user1.setRoles(Collections.singletonList(roleUser));
//        user1.setTasks(new HashSet<>(Arrays.asList(completedTask1, completedTask2)));
//
//        // Save the user to the in-memory database
//        userRepository.save(user1);
//
//        // Call the service method
//        Map<String, Object> result = userService.getUsersTaskStatus();
//
//        // Verify the results
//        assertEquals(1, result.get("totalUsers")); // There should be one user
//        assertEquals(1, result.get("usersWithAllTasksCompleted")); // This user has all tasks completed
//        assertEquals(0, result.get("usersWithIncompleteTasks")); // No users have incomplete tasks
//    }
//
//    @Test
//    void testGetUsersTaskStatus_WithIncompleteTasks() {
//        // Create and save ROLE_USER
//        Role roleUser = new Role();
//        roleUser.setRoleName(ERole.ROLE_USER);
//        roleRepo.save(roleUser);
//
//        // Create tasks (one completed, one incomplete)
//        Task completedTask = new Task();
//        completedTask.setTaskStatus(TaskStatus.COMPLETED);
//
//        Task incompleteTask = new Task();
//        incompleteTask.setTaskStatus(TaskStatus.IN_PROGRESS);
//
//        // Create a user with ROLE_USER and a mix of completed and incomplete tasks
//        User user2 = new User();
//        user2.setRoles(Collections.singletonList(roleUser));
//        user2.setTasks(new HashSet<>(Arrays.asList(completedTask, incompleteTask)));
//
//        // Save the user to the in-memory database
//        userRepository.save(user2);
//
//        // Call the service method
//        Map<String, Object> result = userService.getUsersTaskStatus();
//
//        // Verify the results
//        assertEquals(1, result.get("totalUsers")); // There should be one user
//        assertEquals(0, result.get("usersWithAllTasksCompleted")); // No users have all tasks completed
//        assertEquals(1, result.get("usersWithIncompleteTasks")); // The user has incomplete tasks
//    }
//}
