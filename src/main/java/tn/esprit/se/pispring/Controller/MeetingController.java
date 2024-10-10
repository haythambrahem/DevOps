package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ConsultantService;
import tn.esprit.se.pispring.Service.MeetInterface;
import tn.esprit.se.pispring.Service.MeetService;
import tn.esprit.se.pispring.entities.Consultant;
import tn.esprit.se.pispring.entities.Meeting;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins ="http://localhost:8089")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/meet")
public class MeetingController {
  //  private MeetInterface meetInterface;
    private final MeetInterface meetInterface;
    private final MeetService meetService ;
    private  final ConsultantService consultantService ;

    @GetMapping("/retriveMeetings")
    public ResponseEntity<List<Meeting>> retrieveAllMeetings() {
        List<Meeting> meetings = meetInterface.retrieveAllMeetings();
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @PostMapping("/addMeet")
    public ResponseEntity<Meeting> addMeeting(@RequestBody Meeting meeting) {
        Meeting addedMeeting = meetInterface.addMeeting(meeting);
        return new ResponseEntity<>(addedMeeting, HttpStatus.CREATED);
    }

    @PutMapping("/{meetId}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable Long meetId, @RequestBody Meeting meeting) {
        meeting.setMeetId(meetId); // Assurez-vous que l'ID de la réunion est correctement défini
        Meeting updatedMeeting = meetInterface.updateMeeting(meeting);
        return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
    }

    @GetMapping("/{meetId}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable Long meetId) {
        Meeting meeting = meetInterface.getMeetingById(meetId);
        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @DeleteMapping("/{meetId}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long meetId) {
        meetInterface.deleteMeeting(meetId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{meetId}/valider")
    public ResponseEntity<Void> validerMeeting(@PathVariable Long meetId) {
        meetInterface.validerMeeting(meetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/{meetId}/affecter")
    public ResponseEntity<Void> affecter(@PathVariable Long meetId) {
        meetService.affecter(meetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/{meetId}/annuler")
    public ResponseEntity<Void> annulerMeet(@PathVariable Long meetId) {
        meetInterface.annulerMeet(meetId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Double>> getMeetingStatistics() {
        Map<String, Double> meetingStatistics = meetService.calculateMeetingStatistics();
        return ResponseEntity.ok(meetingStatistics);
    }
    @GetMapping("/stats")
    public Map<Consultant, Map<String, Object>> getConsultantStats() {
        return meetService.calculateConsultantStats();
    }
    @PostMapping("/planifier/{consultantId}/{userId}")
    public ResponseEntity<Meeting> planifierMeeting(@RequestBody Meeting meeting, @PathVariable Long consultantId, @PathVariable Long userId) {
        Meeting newMeeting = meetService.planifierMeeting(meeting, consultantId,userId);
        return ResponseEntity.ok(newMeeting);
    }
  /*  @GetMapping("/count/{consultantId}/{dateBegin}/{dateEnd}")
    public Map<LocalDate, Long> countMeetingsByDateAndConsultantId(
            @PathVariable Long consultantId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateBegin,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {

        return meetService.countMeetingsByDateAndConsultantId(consultantId, dateBegin, dateEnd);
    }*/

 @GetMapping("/meetings/{consultantId}")
 public List<Meeting> getMeetingsByConsultantId(@PathVariable Long consultantId) {
     return meetService.getMeetingsByConsultantId(consultantId);
 }
    @GetMapping("/Pendingmeetings/{userId}")
    public List<Meeting> findFirstPendingOrApprovedMeetingByUserId(@PathVariable Long userId) {
        return meetService.findFirstPendingOrApprovedMeetingByUserId(userId);
    }
    @GetMapping("/meeeets/{userId}")
    public List<Meeting> meetingsPourUser(@PathVariable Long userId) {
        return  meetService.meetingsPourUser(userId);
    }

    /*@GetMapping("/meetingstats/{consultantId}")
    public Map<String, Map<String, Long>> calculateStatisticsForMonth(@PathVariable Long consultantId) {
        return meetService.calculateStatisticsForMonth(consultantId);
    }*/


    @GetMapping("/statss/{consultantId}")
    public Map<String, Map<String, Integer>> getMonthlyMeetingStats(@PathVariable Long consultantId) {
        return meetService.calculateMonthlyMeetingStats(consultantId);
    }
}
