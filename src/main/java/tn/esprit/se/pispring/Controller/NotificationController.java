package tn.esprit.se.pispring.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.NotificationService;
import tn.esprit.se.pispring.entities.Notification;
@CrossOrigin(origins ="http://localhost:8089")

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private NotificationService notificationService;
    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody Notification notification) {
        notificationService.createNotification(notification);
        return ResponseEntity.ok("Notification created successfully.");
    }}
