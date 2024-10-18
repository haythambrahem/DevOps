package com.example.devopstestunitairesuser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.UserImp;
import tn.esprit.se.pispring.entities.*;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplMockTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserImp userService;

    @Test
    void testGetUsersTaskStatus_MultipleCases() {
        Role roleUser = new Role();
        roleUser.setRoleName(ERole.ROLE_USER);

        List<User> userList = new ArrayList<>();

        // Loop to create multiple users and vary the task statuses
        for (int i = 1; i <= 6; i++) {
            User user = new User();
            user.setRoles(Collections.singletonList(roleUser));

            // Randomly assign completed or incomplete tasks based on the loop index
            Set<Task> tasks = new HashSet<>();
            int finalI = i;
            IntStream.range(1, 4).forEach(j -> {
                Task task = new Task();
                task.setTaskStatus(finalI % 2 == 0 ? TaskStatus.COMPLETED : TaskStatus.IN_PROGRESS);  // Alternate between completed and incomplete
                tasks.add(task);
            });

            user.setTasks(tasks);
            userList.add(user);
        }

        // Mock the repository to return the user list
        when(userRepository.findAll()).thenReturn(userList);

        // Call the service method
        Map<String, Object> result = userService.getUsersTaskStatus();

        // Output the result for debugging purposes
        System.out.println("Result: " + result);

        // Verify the results
        assertEquals(6, result.get("totalUsers"));  // There are five users in total
        assertEquals(3, result.get("usersWithAllTasksCompleted"));  // Two users have all tasks completed
        assertEquals(3, result.get("usersWithIncompleteTasks"));  // Three users have incomplete tasks
    }
}