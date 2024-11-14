import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Service.LeavService;
import tn.esprit.se.pispring.entities.*;
import tn.esprit.se.pispring.Repository.*;

import java.time.LocalDate;
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
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.SICK_LEAVE);
        leav.setLeaveStatus(LeaveStatus.PENDING);
        leav.setLeaveApproved(false);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        Leav result = leavService.processLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved());
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_SickLeaveAlreadyApproved() {
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.SICK_LEAVE);
        leav.setLeaveStatus(LeaveStatus.APPROVED);
        leav.setLeaveApproved(true);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        Leav result = leavService.processLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved());
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_VacationLeaveInsufficientDays() {
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.VACATION_LEAVE);
        leav.setLeaveStatus(LeaveStatus.PENDING);
        leav.setLeaveDaysLeft(3);
        leav.setLeaveStartdate(new Date());
        leav.setLeaveEnddate(new Date(System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000)));

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        Leav result = leavService.processLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(LeaveStatus.REFUSED, result.getLeaveStatus());
        assertFalse(result.isLeaveApproved());
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_EmergencyLeavePending() {
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(LeaveType.EMERGENCY_LEAVE);
        leav.setLeaveStatus(LeaveStatus.PENDING);
        leav.setLeaveApproved(false);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));
        when(leavRepository.save(any(Leav.class))).thenReturn(leav);

        Leav result = leavService.processLeaveRequest(leaveId);

        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.isLeaveApproved());
        verify(leavRepository).save(leav);
    }

    @Test
    void testProcessLeaveRequest_UnknownLeaveType() {
        Long leaveId = 1L;
        Leav leav = new Leav();
        leav.setLeaveType(null);
        leav.setLeaveStatus(LeaveStatus.PENDING);

        when(leavRepository.findById(leaveId)).thenReturn(Optional.of(leav));

        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> leavService.processLeaveRequest(leaveId)
        );

        assertEquals("Unknown leave type: null", exception.getMessage());
        verify(leavRepository, never()).save(any(Leav.class));
    }


    @Test
    void testCalculateRemainingLeaveDays() {
        LeavService leavService = new LeavService(userRepository, leavRepository, notificationRepository);

        LocalDate leaveStartDate = LocalDate.of(2024, 11, 1);
        LocalDate leaveEndDate = LocalDate.of(2024, 11, 5);

        int result = leavService.calculateRemainingLeaveDays(
                java.sql.Date.valueOf(leaveStartDate),
                java.sql.Date.valueOf(leaveEndDate)
        );

        assertEquals(4, result);
    }
}
