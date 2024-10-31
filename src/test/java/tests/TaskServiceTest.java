package tests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.TaskService;
import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateTaskBudget_WithValidUsers_ReturnsCorrectBudget() {
        // Arrange
        Task task = new Task();
        User user1 = new User();
        user1.setId(1L);
        user1.setSalaire(1000);

        User user2 = new User();
        user2.setId(2L);
        user2.setSalaire(1500);

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        // Act
        double result = taskService.calculateTaskBudget(task, users);

        System.out.print("Test: calculateTaskBudget_WithValidUsers, Expected: 2500.0, Actual: " + result + "\n");

        // Assert
        assertEquals(2500.0, result, "The task budget should be the sum of user salaries.");
    }

    @Test
    void calculateTaskBudget_WithUserNotFound_ReturnsCorrectBudget() {
        // Arrange
        Task task = new Task();
        User user1 = new User();
        user1.setId(1L);
        user1.setSalaire(1000);
        User user2 = new User();
        user2.setId(2L);

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        double result = taskService.calculateTaskBudget(task, users);

        System.out.print("Test: calculateTaskBudget_WithUserNotFound, Expected: 1000.0, Actual: " + result + "\n");

        // Assert
        assertEquals(1000.0, result, "The task budget should be the salary of the found user only.");
    }

    @Test
    void calculateTaskBudget_WithEmptyUserSet_ReturnsZero() {
        // Arrange
        Task task = new Task();
        Set<User> users = new HashSet<>();

        // Act
        double result = taskService.calculateTaskBudget(task, users);


        System.out.print("Test: calculateTaskBudget_WithEmptyUserSet, Expected: 0.0, Actual: " + result + "\n");

        // Assert
        assertEquals(0, result, "The task budget should be zero for an empty user set.");
    }
}