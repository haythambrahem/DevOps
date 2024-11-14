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



    @Override
    @Transactional
    public Leav acceptLeaveRequest(Long leaveId) {
        try {
            Leav leav = leavRepository.findById(leaveId).orElse(null);

            if (leav == null) {
                throw new EntityNotFoundException("Leave not found with ID: " + leaveId);
            }

            int leaveDurationInDays = calculateLeaveDurationInDays(leav.getLeaveStartdate(), leav.getLeaveEnddate());

            if (leav.getLeaveDaysLeft() < leaveDurationInDays) {
                throw new IllegalArgumentException("Leave duration exceeds available leave days left.");
            }

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


    @Override
    public Leav refuseLeaveRequest(Long leaveId) {
        Leav leav = leavRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leave not found with ID: " + leaveId));

        int leaveDurationInDays = calculateLeaveDurationInDays(leav.getLeaveStartdate(), leav.getLeaveEnddate());

        if (leav.getLeaveDaysLeft() < leaveDurationInDays) {
            throw new IllegalArgumentException("Leave duration exceeds available leave days left.");
        }

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


    @Transactional
    public Leav processLeaveRequest(Long leaveId) {
        Leav leav = leavRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Leav not found with ID: " + leaveId));

        if (leav.getLeaveType() == null) {
            throw new UnsupportedOperationException("Unknown leave type: null");
        }

        switch (leav.getLeaveType()) {
            case SICK_LEAVE:
                if (leav.getLeaveStatus() == LeaveStatus.PENDING) {
                    leav.setLeaveStatus(LeaveStatus.APPROVED);
                    leav.setLeaveApproved(true);
                    log.info("Sick leave request approved.");
                } else if (leav.getLeaveStatus() == LeaveStatus.APPROVED) {
                    log.info("Sick leave is already approved.");
                } else {
                    leav.setLeaveStatus(LeaveStatus.REFUSED);
                    leav.setLeaveApproved(false);
                    log.info("Sick leave request refused.");
                }
                break;

            case VACATION_LEAVE:
                if (leav.getLeaveStatus() == LeaveStatus.PENDING) {
                    int durationInDays = calculateLeaveDurationInDays(leav.getLeaveStartdate(), leav.getLeaveEnddate());
                    if (leav.getLeaveDaysLeft() >= durationInDays) {
                        leav.setLeaveStatus(LeaveStatus.APPROVED);
                        leav.setLeaveApproved(true);
                        leav.setLeaveDaysLeft(leav.getLeaveDaysLeft() - durationInDays);
                        log.info("Vacation leave approved. Days deducted from available leave days.");
                    } else {
                        leav.setLeaveStatus(LeaveStatus.REFUSED);
                        leav.setLeaveApproved(false);
                        log.warn("Vacation leave request refused due to insufficient leave days left.");
                    }
                }
                break;

            case EMERGENCY_LEAVE:
                if (leav.getLeaveStatus() == LeaveStatus.PENDING) {
                    leav.setLeaveStatus(LeaveStatus.APPROVED);
                    leav.setLeaveApproved(true);
                    log.info("Emergency leave request automatically approved.");
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown leave type: " + leav.getLeaveType());
        }

        return leavRepository.save(leav);
    }

    public int calculateRemainingLeaveDays(Date leaveStartDate, Date leaveEndDate) {
        long diffInMillis = leaveEndDate.getTime() - leaveStartDate.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }

}