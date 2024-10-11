package com.example.devopstestunitairesuser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.UserImp;
import tn.esprit.se.pispring.entities.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplMockTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserImp userService;

    @Test
    void testGetUsersTaskStatus_AllTasksCompleted() {
        // Create ROLE_USER
        Role roleUser = new Role();
        roleUser.setRoleName(ERole.ROLE_USER);

        // Create completed tasks
        Task completedTask1 = new Task();
        completedTask1.setTaskStatus(TaskStatus.COMPLETED);

        Task completedTask2 = new Task();
        completedTask2.setTaskStatus(TaskStatus.COMPLETED);

        // Create user with ROLE_USER and all completed tasks
        User user1 = new User();
        user1.setRoles(Collections.singletonList(roleUser));
        Set<Task> tasks = new HashSet<>(Arrays.asList(completedTask1, completedTask2));
        user1.setTasks(tasks);

        // Mock repository response
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user1));

        // Call the service method
        Map<String, Object> result = userService.getUsersTaskStatus();

        // Debugging outputs
        System.out.println("Result: " + result); // Debugging output to verify the result

        // Verify the results
        assertEquals(1, result.get("totalUsers"));  // There is one user
        assertEquals(1, result.get("usersWithAllTasksCompleted"));  // This user has all tasks completed
        assertEquals(0, result.get("usersWithIncompleteTasks"));  // No users have incomplete tasks
    }

    @Test
    void testGetUsersTaskStatus_WithIncompleteTasks() {
        // Create ROLE_USER
        Role roleUser = new Role();
        roleUser.setRoleName(ERole.ROLE_USER);

        // Create tasks (one completed, one incomplete)
        Task completedTask = new Task();
        completedTask.setTaskStatus(TaskStatus.COMPLETED);

        Task incompleteTask = new Task();
        incompleteTask.setTaskStatus(TaskStatus.IN_PROGRESS);  // Not completed

        // Create user with ROLE_USER and tasks
        User user2 = new User();
        user2.setRoles(Collections.singletonList(roleUser));
        Set<Task> tasks = new HashSet<>(Arrays.asList(completedTask, incompleteTask));
        user2.setTasks(tasks);

        // Mock repository response
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user2));

        // Call the service method
        Map<String, Object> result = userService.getUsersTaskStatus();

        // Debugging outputs
        System.out.println("Result: " + result); // Debugging output to verify the result

        // Verify the results
        assertEquals(1, result.get("totalUsers"));  // There is one user
        assertEquals(0, result.get("usersWithAllTasksCompleted"));  // This user has incomplete tasks
        assertEquals(1, result.get("usersWithIncompleteTasks"));  // The user has incomplete tasks
    }
}
