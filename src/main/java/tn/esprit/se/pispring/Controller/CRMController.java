package tn.esprit.se.pispring.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ConsultantService;
import tn.esprit.se.pispring.Service.PortfolioService;
import tn.esprit.se.pispring.entities.Consultant;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/CRM")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CRMController {
    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/retrieve-all-consultant")
    public List<Consultant> getconsultants() {
        List<Consultant> listConsultants = consultantService.retrieveAllConsultants();
        return listConsultants;
    }

    @GetMapping("/retrieve-consultant/{consultant-id}")
    public  Consultant retrieveconsultant(@PathVariable("consultant-id") Long consultantId) {
        return consultantService.retrieveConsultant(consultantId);
    }

    @PostMapping("/add-consultant")
    //@PreAuthorize("hasAnyRole( 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN',  'ROLE_PRODUCT_ADMIN')")

    public  Consultant addconsultant(@RequestBody  Consultant c) {
        return consultantService.addConsultant(c) ;
    }

    @DeleteMapping("/remove-consultant/{consultant_id}")
    public void removeconsultant(@PathVariable("consultant_id") Long consultantId) {
        consultantService.removeConsultant(consultantId);
    }

    @PutMapping("/update-consultant")
    public Consultant updateconsultant(@RequestBody Consultant c) {
        Consultant consultant = consultantService.updateConsultant(c);
        return consultant;
    }

    @PutMapping("/affecterportfolioAConsultant/{idConsultant}/{idPortfolio}")
    @ResponseBody
    public void affectPortfolioaConsultant(@PathVariable("idConsultant") Long idConsultant, @PathVariable("idPortfolio") Long idPortfolio) {
        consultantService.affectPortfolioaConsultant(idConsultant ,idPortfolio);

    }

    @GetMapping("/consultant/{consultantId}/meetings")
    public Map<String, Integer> countMeetingsPerUser(@PathVariable Long consultantId) {
        return consultantService.countMeetingsPerUser(consultantId);
    }


    @GetMapping("/sorted/{sortBy}")
    public ResponseEntity<List<Consultant>> getSortedConsultants(@PathVariable String sortBy) {
        List<Consultant> sortedConsultants = consultantService.getSortedConsultants(sortBy);
        return new ResponseEntity<>(sortedConsultants, HttpStatus.OK);
    }
    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalConsultants() {
        int total = consultantService.getTotalConsultants();
        return ResponseEntity.ok(total);
    }
    @GetMapping("/gender")
    public Map<String, Integer> getConsultantsByGender() {
        return consultantService.getConsultantsByGender();
    }
    @GetMapping("/skill")
    public Map<String, Integer> getConsultantsBySkill() {
        return consultantService.getConsultantsBySkill();
    }
    @GetMapping("/hiredCount")
    public int getHiredConsultantsCountThisMonth() {
        return consultantService.getHiredConsultantsCountThisMonth();
    }
    @GetMapping("/retrieve-skilled-consultant")
    public List<Consultant> getConsultantsBySkillAndSeniority() {
        List<Consultant> listConsultants = consultantService.getConsultantsBySkillAndSeniority();
        return listConsultants;
    }
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Map<String, Long>>> getConsultantsStatistics() {
        Map<String, Map<String, Long>> statistics = consultantService.getStatistiquesMeetings();
        return ResponseEntity.ok(statistics);
    }

}
