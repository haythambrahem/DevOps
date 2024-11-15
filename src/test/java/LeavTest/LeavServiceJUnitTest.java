package LeavTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.se.pispring.Service.LeavService;
import tn.esprit.se.pispring.entities.Leav;
import tn.esprit.se.pispring.entities.LeaveStatus;
import tn.esprit.se.pispring.entities.LeaveType;
import tn.esprit.se.pispring.entities.User;
import tn.esprit.se.pispring.Repository.LeavRepository;
import tn.esprit.se.pispring.Repository.NotificationRepository;
import tn.esprit.se.pispring.Repository.UserRepository;

import java.util.Date;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class LeavServiceJUnitTest {

    private LeavService leavService;
    private LeavRepository leavRepository;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        leavRepository = mock(LeavRepository.class);
        userRepository = mock(UserRepository.class);
        notificationRepository = mock(NotificationRepository.class);
        leavService = new LeavService(userRepository, leavRepository, notificationRepository);
    }

    @Test
    void testRetrieveLeav_ValidId() {
        Leav leave = new Leav();
        leave.setLeaveId(1L);
        when(leavRepository.findById(1L)).thenReturn(java.util.Optional.of(leave));

        Leav result = leavService.retrieveLeav(1L);
        assertNotNull(result);
        assertEquals(1L, result.getLeaveId());
    }

    @Test
    void testRetrieveLeav_InvalidId() {
        when(leavRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        Leav result = leavService.retrieveLeav(1L);
        assertNull(result);
    }

    @Test
    void testAddOrUpdateLeav() {
        Leav leave = new Leav();
        leave.setLeaveStatus(LeaveStatus.PENDING);
        when(leavRepository.save(leave)).thenReturn(leave);

        Leav result = leavService.addOrUpdateLeav(leave);
        assertNotNull(result);
        assertEquals(LeaveStatus.PENDING, result.getLeaveStatus());
    }

    @Test
    void testRemoveLeav() {
        Leav leave = new Leav();
        leave.setLeaveId(1L);
        doNothing().when(leavRepository).deleteById(1L);

        leavService.removeLeav(1L);

        verify(leavRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignLeavToUser_Success() {
        Leav leave = new Leav();
        leave.setLeaveId(1L);
        leave.setLeaveStatus(LeaveStatus.APPROVED);
        User user = new User();
        user.setId(1L);

        when(leavRepository.findById(1L)).thenReturn(java.util.Optional.of(leave));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(leavRepository.save(leave)).thenReturn(leave);

        Leav result = leavService.assignLeavToUser(1L, 1L);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    void testAssignLeavToUser_LeaveNotApproved() {
        Leav leave = new Leav();
        leave.setLeaveId(1L);
        leave.setLeaveStatus(LeaveStatus.PENDING);
        User user = new User();
        user.setId(1L);

        when(leavRepository.findById(1L)).thenReturn(java.util.Optional.of(leave));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            leavService.assignLeavToUser(1L, 1L);
        });

        assertEquals("Leave with ID 1 cannot be assigned. Leave status is not approved.", exception.getMessage());
    }

    @Test
    void testAcceptLeaveRequest_Success() {
        Leav leave = new Leav();
        leave.setLeaveId(1L);
        leave.setLeaveStatus(LeaveStatus.PENDING);
        leave.setLeaveStartdate(new Date());
        leave.setLeaveEnddate(new Date());
        leave.setLeaveDaysLeft(10);
        when(leavRepository.findById(1L)).thenReturn(java.util.Optional.of(leave));
        when(leavRepository.save(leave)).thenReturn(leave);

        Leav result = leavService.acceptLeaveRequest(1L);

        assertNotNull(result);
        assertEquals(LeaveStatus.APPROVED, result.getLeaveStatus());
        assertTrue(result.getLeaveApproved());
    }



    @Test
    void testRefuseLeaveRequest() {
        Leav leave = new Leav();
        leave.setLeaveId(1L);
        leave.setLeaveStatus(LeaveStatus.PENDING);
        leave.setLeaveStartdate(new Date());
        leave.setLeaveEnddate(new Date());
        leave.setLeaveDaysLeft(10);
        when(leavRepository.findById(1L)).thenReturn(java.util.Optional.of(leave));
        when(leavRepository.save(leave)).thenReturn(leave);

        Leav result = leavService.refuseLeaveRequest(1L);

        assertNotNull(result);
        assertEquals(LeaveStatus.REFUSED, result.getLeaveStatus());
        assertFalse(result.getLeaveApproved());
    }

    @Test
    void testGetLeaveStatistics() {
        when(leavRepository.count()).thenReturn(10L);
        when(leavRepository.countByLeaveType(LeaveType.SICK_LEAVE)).thenReturn(5L);
        when(leavRepository.countByLeaveType(LeaveType.VACATION_LEAVE)).thenReturn(3L);

        Map<String, Long> statistics = leavService.getLeaveStatistics();

        assertNotNull(statistics);
        assertEquals(10L, statistics.get("totalLeaves"));
        assertEquals(5L, statistics.get("sickLeaves"));
        assertEquals(3L, statistics.get("vacationLeaves"));
    }
}