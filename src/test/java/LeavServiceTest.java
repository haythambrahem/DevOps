import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Service.LeavService;
import tn.esprit.se.pispring.entities.*;
import tn.esprit.se.pispring.Repository.*;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LeavServiceTest {

    @Mock
    private LeavRepository leavRepository;

    @InjectMocks
    private LeavService leavService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;


    @Test
    void testProcessLeaveRequest_SickLeavePending() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.SICK_LEAVE);
        leav.setLeaveStatus(LeaveStatus.PENDING);
        leav.setLeaveApproved(false);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        // Act
        Leav result = leavService.processLeaveRequest(leaveId);

        // Assert
        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved());
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_SickLeaveAlreadyApproved() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.SICK_LEAVE);
        leav.setLeaveStatus(LeaveStatus.APPROVED);
        leav.setLeaveApproved(true);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        // Act
        Leav result = leavService.processLeaveRequest(leaveId);

        // Assert
        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved()); // Already approved, should remain so
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_VacationLeaveInsufficientDays() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.VACATION_LEAVE);
        leav.setLeaveStatus(LeaveStatus.PENDING);
        leav.setLeaveDaysLeft(3); // 3 days available
        leav.setLeaveStartdate(new Date());
        leav.setLeaveEnddate(new Date(System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000))); // 5 days leave

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        // Act
        Leav result = leavService.processLeaveRequest(leaveId);

        // Assert
        assertNotNull(result);
        assertEquals(LeaveStatus.REFUSED, result.getLeaveStatus());
        assertFalse(result.isLeaveApproved()); // Should be refused due to insufficient days
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_EmergencyLeavePending() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.EMERGENCY_LEAVE);
        leav.setLeaveStatus(LeaveStatus.PENDING);
        leav.setLeaveApproved(false);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        // Act
        Leav result = leavService.processLeaveRequest(leaveId);

        // Assert
        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved()); // Emergency leave is automatically approved
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_UnknownLeaveType() {
        // Arrange
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(null); // Unknown leave type
        leav.setLeaveStatus(LeaveStatus.PENDING);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            leavService.processLeaveRequest(leaveId);
        });

        assertEquals("Unknown leave type: null", exception.getMessage());
        verify(leavRepository, never()).save(any(Leav.class));
    }

    @Test
    void testCalculateRemainingLeaveDays() {
        // Arrange
        LeavService leavService = new LeavService(userRepository, leavRepository, notificationRepository);

        // Suppose the leave is from 1st to 5th of the month
        Date leaveStartDate = new Date(2024, 10, 1); // November 1st, 2024
        Date leaveEndDate = new Date(2024, 10, 5);   // November 5th, 2024

        // Act
        int result = leavService.calculateRemainingLeaveDays(leaveStartDate, leaveEndDate);

        // Assert
        assertEquals(4, result); // Since there are 4 days from Nov 1st to Nov 5th
    }

}

