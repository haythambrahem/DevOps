package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ICandidateService;
import tn.esprit.se.pispring.Service.IRecruitmentService;
import tn.esprit.se.pispring.entities.Candidate;
import tn.esprit.se.pispring.entities.Leav;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/candidate")
public class CandidateController {

    private final ICandidateService candidateService;
    private final IRecruitmentService recruitmentService;


    @PostMapping("/addCandidate")
    public ResponseEntity<Candidate> addCandidate(@Valid @RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateService.addOrUpdateCandidate(candidate);
        return ResponseEntity.ok(savedCandidate);
    }

    @GetMapping("/getAllCand")
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateService.retrieveAllCandidates();
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/getCand/{idCandidate}")
    Candidate getCandidateById(@PathVariable("idCandidate") Long idCandidate)
    {
        return candidateService.retrieveCandidate(idCandidate);
    }
    @DeleteMapping("/delete/{idCandidate}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Long idCandidate) {
        candidateService.removeCandidate(idCandidate);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/candidates/{candidateId}/assign-recruitment/{offerId}")
    public ResponseEntity<?> assignRecruitment(@PathVariable Long candidateId, @PathVariable Long offerId) {
        candidateService.assignRecruitmentToCandidate(candidateId, offerId);
        return ResponseEntity.ok("Recruitment assigned successfully.");
    }

    @GetMapping("/count/{offerId}")
    public ResponseEntity<Integer> countCandidatesByRecruitmentOfferId(@PathVariable Long offerId) {
        int count = candidateService.countCandidatesByRecruitmentOfferId(offerId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}
