
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Repository.LeavRepository;
import tn.esprit.se.pispring.Repository.NotificationRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.LeavService;
import tn.esprit.se.pispring.entities.Leav;
import tn.esprit.se.pispring.entities.LeaveStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeavServiceTest1 {

    @Mock
    private LeavRepository leavRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private LeavService leavService;

    @Test
    void testAcceptLeaveRequest_Success() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveStartdate(new Date());
        leav.setLeaveEnddate(new Date(System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000))); // 3 days leave
        leav.setLeaveDaysLeft(5);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        // Act
        Leav result = leavService.acceptLeaveRequest(leaveId);

        // Assert
        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved());
        verify(leavRepository).save(leav);
    }

    @Test
    void testAcceptLeaveRequest_LeaveNotFound() {
        // Arrange
        Long leaveId = 1L;
        when(leavRepository.findById(leaveId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            leavService.acceptLeaveRequest(leaveId);
        });

        assertEquals("Leave not found with ID: " + leaveId, exception.getMessage());
        verify(leavRepository, never()).save(any(Leav.class));
    }

    @Test
    void testAcceptLeaveRequest_LeaveDurationExceedsAvailableDays() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveStartdate(new Date());
        leav.setLeaveEnddate(new Date(System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000))); // 5 days leave
        leav.setLeaveDaysLeft(3); // Only 3 days available

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leavService.acceptLeaveRequest(leaveId);
        });

        assertEquals("Leave duration exceeds available leave days left.", exception.getMessage());
        verify(leavRepository, never()).save(any(Leav.class));
    }

    @Test
    void testAcceptLeaveRequest_UnexpectedException() {
        // Arrange
        Long leaveId = 1L;
        when(leavRepository.findById(leaveId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            leavService.acceptLeaveRequest(leaveId);
        });

        assertTrue(exception.getMessage().contains("Failed to accept leave request."));
        verify(leavRepository, never()).save(any(Leav.class));
    }
}
