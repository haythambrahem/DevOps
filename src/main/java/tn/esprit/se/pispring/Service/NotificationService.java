package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.LeavRepository;
import tn.esprit.se.pispring.Repository.NotificationRepository;
import tn.esprit.se.pispring.entities.Leav;
import tn.esprit.se.pispring.entities.Notification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final LeavRepository leavRepository;

    public void createNotification(Notification notification) {
        notification.setCreationDateTime(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Scheduled(fixedRate = 60000)
    public void sendScheduledNotifications() {
        LocalDateTime currentTime = LocalDateTime.now();
        Date currentDateTime = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        // Calculate the end time (e.g., one hour from the current time)
        LocalDateTime endTime = currentTime.plusHours(1);
        Date endDateTime = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());

        List<Leav> upcomingLeave = leavRepository.findUpcomingLeave(currentDateTime, endDateTime);

        for (Leav leav : upcomingLeave) {
            String notificationMessage = "Your leave is scheduled for " + leav.getLeaveStartdate() + ".";
            Notification notification = new Notification();
            notification.setMessage(notificationMessage);
            notification.setRecipient(leav.getUser());
            notificationRepository.save(notification);
        }
    }
}
