import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Repository.LeavRepository;
import tn.esprit.se.pispring.Service.LeavService;
import tn.esprit.se.pispring.entities.Leav;
import tn.esprit.se.pispring.entities.LeaveStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeavServiceTest {

    @Mock // yaamel simulation l repo menghir ma tnedi database
    private LeavRepository leavRepository;

    @InjectMocks
    private LeavService leavService;

    private Leav leave;

    @BeforeEach
    void setUp() {
        leave = new Leav();
        leave.setLeaveId(1L);
        leave.setLeaveStartdate(new Date());
        leave.setLeaveEnddate(new Date(System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000))); // 5 days later
        leave.setLeaveDaysLeft(10);
        leave.setLeaveStatus(LeaveStatus.PENDING);
    }


/////Verify  l exception kif t accepty leave request mech mawjoud f repo.
    @Test
    void testAcceptLeaveRequest_WhenLeaveNotFound_ShouldThrowException() {

        when(leavRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            leavService.acceptLeaveRequest(1L);
        });

        assertEquals("Leave not found with ID: 1", exception.getMessage());
    }

    ///////famma exception kif requested leave yfout  available leave days.
    @Test
    void testAcceptLeaveRequest_WhenLeaveDurationExceedsAvailable_ShouldThrowException() {

        when(leavRepository.findById(1L)).thenReturn(Optional.of(leave));
        leave.setLeaveDaysLeft(2);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leavService.acceptLeaveRequest(1L);
        });

        assertEquals("Leave duration exceeds available leave days left.", exception.getMessage());
        verify(leavRepository, never()).save(any(Leav.class));
    }

}
