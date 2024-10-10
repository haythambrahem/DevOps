package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ICandidateService;
import tn.esprit.se.pispring.Service.IRecruitmentService;
import tn.esprit.se.pispring.entities.Recruitment;
import tn.esprit.se.pispring.entities.RecruitmentStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins ="http://localhost:8089")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recruitment")
public class RecruitmentController {
    private final IRecruitmentService recruitmentService;
    private final ICandidateService candidateService;

    @PostMapping("/addRecruitment")
    Recruitment addRecruitment(@RequestBody Recruitment recruitment)
    {
        return recruitmentService.addOrUpdateRecruitment(recruitment);
    }
    @PutMapping("/updateRecruitment")
    Recruitment updateRecruitment(@RequestBody Recruitment recruitment)
    {
        return recruitmentService.addOrUpdateRecruitment(recruitment);
    }

    @GetMapping("/get/{offerId}")
    Recruitment getRecruitment(@PathVariable("offerId") Long offerId)
    {
        return recruitmentService.retrieveRecruitment(offerId);
    }

    @GetMapping("/allRecruitments")
    List<Recruitment> getAllRecruitments()
    {
        return recruitmentService.retrieveAllRecruitments();
    }

    @DeleteMapping("/delete/{offerId}")
    void deleteRecruitment(@PathVariable("offerId") Long offerId)
    {
        recruitmentService.removeRecruitment(offerId);
    }

    @GetMapping("/postTitles")
    public List<String> getPostTitles() {
        return recruitmentService.getPostTitles();
    }

    @GetMapping("/getPostTitleDetails/{postTitle}")
    public ResponseEntity<?> getPostTitleDetails(@PathVariable String postTitle) {
        Optional<Recruitment> recruitment = recruitmentService.getPostTitleDetails(postTitle);
        if (recruitment.isPresent()) {
            return ResponseEntity.ok(recruitment.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/averageSalaryRange")
    public ResponseEntity<Double> getAverageSalaryRange() {
        double averageSalaryRange = recruitmentService.calculateAverageSalaryRange();
        return ResponseEntity.ok(averageSalaryRange);
    }
    @GetMapping("/recruitmentsPerManager")
    public ResponseEntity<Map<String, Integer>> getRecruitmentsPerManager() {
        Map<String, Integer> recruitmentsPerManager = recruitmentService.getRecruitmentsPerHiringManager();
        return ResponseEntity.ok(recruitmentsPerManager);
    }

    @GetMapping("/openPositionsByLocation")
    public ResponseEntity<Map<String, Integer>> getOpenPositionsByLocation() {
        Map<String, Integer> openPositionsByLocation = recruitmentService.getOpenPositionsByLocation();
        return ResponseEntity.ok(openPositionsByLocation);
    }

    @GetMapping("/recruitmentTrend/{startYear}/{endYear}")
    public ResponseEntity<Map<String, Map<RecruitmentStatus, Integer>>> getRecruitmentTrend(
            @PathVariable int startYear,
            @PathVariable int endYear) {
        Map<String, Map<RecruitmentStatus, Integer>> trendData = recruitmentService.analyzeRecruitmentTrend(startYear, endYear);
        return ResponseEntity.ok(trendData);
    }
    @GetMapping("/recruitment/P")
    public Map<String, Map<String, Integer>> getPostTitleTrend(
            @RequestParam("postTitle") String postTitle,
            @RequestParam("startYear") int startYear,
            @RequestParam("endYear") int endYear) {
        return recruitmentService.analyzePostTitleTrend(postTitle, startYear, endYear);
    }

    @GetMapping("/analyzeRecruitmentCandidateRelation")
    public ResponseEntity<Map<String, Integer>> analyzeRecruitmentCandidateRelation() {
        Map<String, Integer> recruitmentCandidateRelation = recruitmentService.analyzeRecruitmentCandidateRelation();
        return ResponseEntity.ok(recruitmentCandidateRelation);
    }
    @PostMapping("/assignCandidate/{idCandidate}/{offerId}")
    public ResponseEntity<String> assignCandidateToRecruitment(
            @PathVariable("idCandidate") Long idCandidate,
            @PathVariable("offerId") Long offerId) {

        recruitmentService.assignCandidateToRecruitment(idCandidate, offerId);
        return ResponseEntity.ok("Candidate assigned to Recruitment successfully");
    }

    @GetMapping("/getByPostTitle/{postTitle}")
    public ResponseEntity<Optional<Recruitment>> getRecruitmentByPostTitle(@PathVariable String postTitle) {
        Optional<Recruitment> recruitment = recruitmentService.getRecruitmentByPostTitle(postTitle);
        if (recruitment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recruitment);
    }

    @GetMapping("/recruitment/candidate/{offerId}/{idCandidate}/experience-matching")
    public ResponseEntity<Double> calculateExperienceMatching(@PathVariable Long offerId, @PathVariable Long idCandidate) {
        // Récupérer l'expérience requise pour le recrutement
        int experienceRequired = recruitmentService.getExperienceRequired(offerId);

        if (experienceRequired == -1) { // Mettez ici la valeur que vous utilisez pour indiquer une absence de données
            return ResponseEntity.notFound().build();
        }

        // Récupérer l'expérience du candidat
        int experienceCand = recruitmentService.getExperience(idCandidate);

        // Calculer le pourcentage de correspondance
        double percentage = ((double) Math.min(experienceRequired, experienceCand) / Math.max(experienceRequired, experienceCand)) * 100;

        return ResponseEntity.ok(percentage);
    }

}
