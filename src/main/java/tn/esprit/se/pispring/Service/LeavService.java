package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.LeavRepository;
import tn.esprit.se.pispring.Repository.NotificationRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.*;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeavService implements ILeavService {
    private final UserRepository userRepository;

    private final LeavRepository leavRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public Leav retrieveLeav(Long leaveId) {
        return leavRepository.findById(leaveId).orElse(null);
    }

    @Override
    public List<Leav> retrieveAllLeaves() {
        return leavRepository.findAll();
    }

    @Override
    public Leav addOrUpdateLeav(Leav L) {
        return leavRepository.save(L);
    }

    @Override
    public void removeLeav(Long leaveId) {
        leavRepository.deleteById(leaveId);
    }

    @Override
    @Transactional
    public Leav assignLeavToUser(Long leaveId, Long id) {
        Leav leav = leavRepository.findById(leaveId).orElse(null);
        User user = userRepository.findById(id).orElse(null);

        if (leav != null && user != null) {
            // verif q le statut du congé est "approved"
            if (leav.getLeaveStatus() == LeaveStatus.APPROVED) {
                leav.setUser(user);
                leavRepository.save(leav);
                log.info("Leave with ID {} has been assigned to user ({} {}).", leaveId, user.getFirstName(), user.getLastName());
                Notification notification = new Notification();
                notification.setMessage("Your leave request has been assigned.");
                notification.setRecipient(user);
                notificationRepository.save(notification);
                return leav;
            } else {
                // sinon n pas affecter le congé
                log.error("Leave with ID {} cannot be assigned to user with ID {}. Leave status is not approved.", leaveId, id);
                throw new IllegalStateException("Leave with ID " + leaveId + " cannot be assigned. Leave status is not approved.");
            }
        } else {
            log.error("Failed to assign leave to user. Leave or user not found.");
            throw new IllegalArgumentException("Failed to assign leave to user. Leave or user not found.");
        }
    }


    //    @Scheduled(fixedRate = 60000)
//    public void sendScheduledNotifications() {
//        LocalDateTime currentTime = LocalDateTime.now();
//
//        // Convert LocalDateTime to Date
//        Date currentDateTime = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
//        List<Leav> upcomingLeave = leavRepository.findUpcomingLeave(currentDateTime);
//
//        for (Leav leav : upcomingLeave) {
//            String notificationMessage = "Your leave is scheduled for " + leav.getLeaveStartdate() + ".";
//            Notification notification = new Notification();
//            notification.setMessage(notificationMessage);
//            notification.setRecipient(leav.getUser());
//            notificationRepository.save(notification);
//        }
//    // Methode pour calculer el duree mtaa el congé entre jour début et jour fin
//    private int calculateLeaveDurationInDays(Date leaveStartDate, Date leaveEnddate) {
//        LocalDate startLocalDate = leaveStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        LocalDate endLocalDate = leaveEnddate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        return (int) ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
//    }
//accepter le congé par l'admin hr
    @Override
    @Transactional
    public Leav acceptLeaveRequest(Long leaveId) {
        try {
            Leav leav = leavRepository.findById(leaveId).orElse(null);

            if (leav == null) {
                throw new EntityNotFoundException("Leave not found with ID: " + leaveId);
            }

            // Calculate leave duration in days
            int leaveDurationInDays = calculateLeaveDurationInDays(leav.getLeaveStartdate(), leav.getLeaveEnddate());

            // Check if leave duration exceeds available leave days left
            if (leav.getLeaveDaysLeft() < leaveDurationInDays) {
                throw new IllegalArgumentException("Leave duration exceeds available leave days left.");
            }

            // Update leave status and approval
            leav.setLeaveStatus(LeaveStatus.APPROVED);
            leav.setLeaveApproved(true);

            return leavRepository.save(leav);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to accept leave request.", e);
        }
    }

    @Override
    public int calculateLeaveDurationInDays(Date leaveStartDate, Date leaveEndDate) {
        if (leaveStartDate == null || leaveEndDate == null) {
            throw new IllegalArgumentException("Leave start date or end date cannot be null.");
        }

        long startTime = leaveStartDate.getTime();
        long endTime = leaveEndDate.getTime();

        long durationInMillis = endTime - startTime;
        int durationInDays = (int) (durationInMillis / (24 * 60 * 60 * 1000));

        return durationInDays;
    }


    //refuser le congé par l'admin hr si le nbr de jours restants exceeds el nbr de jours demandés.
    @Override
    public Leav refuseLeaveRequest(Long leaveId) {
        Leav leav = leavRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leave not found with ID: " + leaveId));

        // Calculate leave duration in days
        int leaveDurationInDays = calculateLeaveDurationInDays(leav.getLeaveStartdate(), leav.getLeaveEnddate());

        // Check if leave duration exceeds available leave days left
        if (leav.getLeaveDaysLeft() < leaveDurationInDays) {
            throw new IllegalArgumentException("Leave duration exceeds available leave days left.");
        }

        // Update leave status and approval
        leav.setLeaveStatus(LeaveStatus.REFUSED);
        leav.setLeaveApproved(false);

        return leavRepository.save(leav);
    }



    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public User getUserByLeaveId(Long leaveId) {
        Optional<Leav> leaveOptional = leavRepository.findById(leaveId);
        if (leaveOptional.isPresent()) {
            Leav leave = leaveOptional.get();
            return leave.getUser();
        }
        return null;
    }
    @Override
    public Long getLeaveIdByDate(Date leaveStartdate) {
        return leavRepository.findIdByLeaveStartdate(leaveStartdate);
    }
    @Override
    public List<Leav> getLeavesForUser(Long id) {
        return leavRepository.findByUserId(id);
    }
    @Override
    public Map<String, Long> getLeaveStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalLeaves", leavRepository.count());
        statistics.put("sickLeaves", leavRepository.countByLeaveType(LeaveType.SICK_LEAVE));
        statistics.put("vacationLeaves", leavRepository.countByLeaveType(LeaveType.VACATION_LEAVE
        ));
        return statistics;
    }
}