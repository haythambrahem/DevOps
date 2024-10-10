package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ILeavService;
import tn.esprit.se.pispring.entities.Leav;
import tn.esprit.se.pispring.entities.Notification;
import tn.esprit.se.pispring.entities.User;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/leav")
public class LeavController {

    private final ILeavService leavService;
    @PostMapping("/addLeav")
    Leav addLeav(@RequestBody Leav leav)
    {
        return leavService.addOrUpdateLeav(leav);
    }
    @PutMapping("/updateLeav")
    Leav updateLeav(@RequestBody Leav leav)
    {
        return leavService.addOrUpdateLeav(leav);
    }
    @GetMapping("/get/{leaveId}")
    Leav getLeav(@PathVariable("leaveId") Long leaveId)
    {
        return leavService.retrieveLeav(leaveId);
    }
    @GetMapping("/allLeavs")
    List<Leav> getAllLeavs() { return leavService.retrieveAllLeaves();}
    @DeleteMapping("/delete/{leaveId}")
    void deleteLeav(@PathVariable("leaveId") Long leaveId)
    {
        leavService.removeLeav(leaveId);
    }

    @PutMapping("/assignLeavToUser/{leaveId}/{id}")
    public ResponseEntity<String> assignLeavToUser(@PathVariable("leaveId") Long leaveId, @PathVariable("id") Long id) {
        try {
            leavService.assignLeavToUser(leaveId, id);
            return ResponseEntity.ok("Leave assigned to user successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign leave to user.");
        }
    }

    @PutMapping("/accept/{leaveId}")
    public ResponseEntity<Leav> acceptLeaveRequest(@PathVariable Long leaveId) {
        try {
            Leav acceptedLeave = leavService.acceptLeaveRequest(leaveId);
            return ResponseEntity.ok(acceptedLeave);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @PutMapping("/{leaveId}/refuse")
    public ResponseEntity<Map<String, String>> refuseLeaveRequest(@PathVariable("leaveId") Long leaveId) {
        try {
            leavService.refuseLeaveRequest(leaveId);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Leave request refused successfully.");
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to refuse leave request: " + e.getMessage()));
        }
    }


    @GetMapping("/leave/duration/{startDate}/{endDate}")
    public int calculateLeaveDurationInDays(@PathVariable String startDate, @PathVariable String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            return leavService.calculateLeaveDurationInDays(start, end);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd format.");
        }
    }
    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = leavService.getNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{leaveId}/userByLeave")
    public ResponseEntity<User> getUserByLeaveId(@PathVariable("leaveId") Long leaveId) {
        User user = leavService.getUserByLeaveId(leaveId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/leaveIdByDate/{leaveStartdate}")
    public ResponseEntity<Long> getLeaveIdByDate(@PathVariable("leaveStartdate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date leaveStartdate) {
        System.out.println("Received request to get leave ID for date: " + leaveStartdate);


        Long leaveId = leavService.getLeaveIdByDate(leaveStartdate);

        if (leaveId != null) {
            System.out.println("Leave ID found: " + leaveId);
            return ResponseEntity.ok(leaveId);
        } else {
            System.out.println("Leave ID not found for date: " + leaveStartdate);
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/user/{id}")
    public List<Leav> getLeavesForUser(@PathVariable Long id) {
        return leavService.getLeavesForUser(id);
    }

    @GetMapping("/statistics")
    public Map<String, Long> getLeaveStatistics() {
        return leavService.getLeaveStatistics();
    }
}

