package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Leav;
import tn.esprit.se.pispring.entities.Notification;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ILeavService {

    Leav retrieveLeav(Long leaveId);
    List<Leav> retrieveAllLeaves();
    Leav addOrUpdateLeav(Leav L);
    void removeLeav(Long leaveId);

    Leav assignLeavToUser(Long leavId, Long id);
    Leav acceptLeaveRequest(Long leaveId);

    int calculateLeaveDurationInDays(Date leaveStartDate, Date leaveEndDate);

    Leav refuseLeaveRequest(Long leaveId);

    List<Notification> getNotifications();

    User getUserByLeaveId(Long leaveId);

    Long getLeaveIdByDate(Date leaveStartdate);

    List<Leav> getLeavesForUser(Long id);

    Map<String, Long> getLeaveStatistics();

}
