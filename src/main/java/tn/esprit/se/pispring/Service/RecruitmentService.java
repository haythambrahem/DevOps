package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.CandidateRepository;
import tn.esprit.se.pispring.Repository.RecruitmentRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Candidate;
import tn.esprit.se.pispring.entities.Recruitment;
import tn.esprit.se.pispring.entities.RecruitmentStatus;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentService implements IRecruitmentService {

    private final UserRepository userRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final CandidateRepository candidateRepository;

    @Override
    public Recruitment addOrUpdateRecruitment(Recruitment R) {
        return recruitmentRepository.save(R);
    }

    @Override
    public void removeRecruitment(Long offerId) {
        recruitmentRepository.deleteById(offerId);
    }

    @Override
    public Recruitment retrieveRecruitment(Long offerId) {
        return recruitmentRepository.findById(offerId).orElse(null);
    }

    @Override
    public List<Recruitment> retrieveAllRecruitments() {
        return recruitmentRepository.findAll();
    }

    @Override
    public List<String> getPostTitles() {
        List<Recruitment> recruitments = recruitmentRepository.findAll();
        return recruitments.stream()
                .map(Recruitment::getPostTitle) // Assuming 'getPostTitle()' returns the post title
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Recruitment> getPostTitleDetails(String postTitle) {
        return recruitmentRepository.findByPostTitle(postTitle);
    }
    @Override
    public double calculateAverageSalaryRange() {
        List<Recruitment> recruitments = retrieveAllRecruitments();
        double totalSalaryRange = 0;
        for (Recruitment recruitment : recruitments) {
            totalSalaryRange += (recruitment.getSalaryRangeMax() + recruitment.getSalaryRangeMin()) / 2;
        }
        return totalSalaryRange / recruitments.size();
    }
    @Override
    public Map<String, Integer> getRecruitmentsPerHiringManager() {
        List<Recruitment> recruitments = retrieveAllRecruitments();
        Map<String, Integer> recruitmentsByManager = new HashMap<>();
        for (Recruitment recruitment : recruitments) {
            String hiringManager = recruitment.getHiringManager();
            recruitmentsByManager.put(hiringManager, recruitmentsByManager.getOrDefault(hiringManager, 0) + 1);
        }
        return recruitmentsByManager;
    }
    @Override
    public Map<String, Integer> getOpenPositionsByLocation() {
        List<Recruitment> recruitments = retrieveAllRecruitments();
        Map<String, Integer> positionsByLocation = new HashMap<>();
        for (Recruitment recruitment : recruitments) {
            String jobLocation = recruitment.getJobLocation();
            positionsByLocation.put(jobLocation, positionsByLocation.getOrDefault(jobLocation, 0) + recruitment.getNumberOfOpenings());
        }
        return positionsByLocation;
    }
    @Override
    public Map<String, Map<RecruitmentStatus, Integer>> analyzeRecruitmentTrend(int startYear, int endYear) {
        Map<String, Map<RecruitmentStatus, Integer>> trendData = new HashMap<>();

        List<RecruitmentStatus> statuses = Arrays.asList(RecruitmentStatus.OPEN, RecruitmentStatus.CLOSED);

        for (int year = startYear; year <= endYear; year++) {
            Map<RecruitmentStatus, Integer> statusCounts = new HashMap<>();
            List<Object[]> results = recruitmentRepository.countRecruitmentStatusByYear(year, statuses);
            for (Object[] result : results) {
                RecruitmentStatus status = (RecruitmentStatus) result[0];
                Long count = (Long) result[1];
                statusCounts.put(status, count.intValue());
            }
            trendData.put(String.valueOf(year), statusCounts);
        }

        return trendData;
    }
@Override
    public Map<String, Map<String, Integer>> analyzePostTitleTrend(String postTitle, int startYear, int endYear) {
        Map<String, Map<String, Integer>> titleTrendData = new HashMap<>();

        for (int year = startYear; year <= endYear; year++) {
            Map<String, Integer> yearData = new HashMap<>();

            // Retrieve the count of recruitments for the given post title and year
            List<Object[]> results = recruitmentRepository.countRecruitmentStatusByYearAndPostTitle(year, postTitle);

            // Iterate through the results and add them to the yearData map
            for (Object[] result : results) {
                // The result array should contain the post title and its count
                String currentPostTitle = (String) result[0];
                Long count = (Long) result[1];
                yearData.put(currentPostTitle, count.intValue());
            }

            // Add the yearData map to the titleTrendData map
            titleTrendData.put(String.valueOf(year), yearData);
        }

        return titleTrendData;
    }

    @Override
    public Map<String, Integer> analyzeRecruitmentCandidateRelation() {
        Map<String, Integer> recruitmentCandidateRelation = new HashMap<>();
        List<Candidate> candidates = candidateRepository.findAll();

        for (Candidate candidate : candidates) {
            Recruitment recruitment = candidate.getRecruitment();
            if (recruitment != null) {
                Long offerId = recruitment.getOfferId();
                if (offerId != null) {
                    // Increment the count for the offerId
                    recruitmentCandidateRelation.put(String.valueOf(offerId),
                            recruitmentCandidateRelation.getOrDefault(String.valueOf(offerId), 0) + 1);
                }
            }
        }

        return recruitmentCandidateRelation;
    }
@Override
    public void assignCandidateToRecruitment(Long idCandidate, Long offerId) {
        // Récupérer le candidat et le recrutement depuis la base de données
        Optional<Candidate> candidateOptional = candidateRepository.findById(idCandidate);
        Optional<Recruitment> recruitmentOptional = recruitmentRepository.findById(offerId);

        // Vérifier si le candidat et le recrutement existent
        if (candidateOptional.isPresent() && recruitmentOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            Recruitment recruitment = recruitmentOptional.get();

            // Affecter le candidat au recrutement
            candidate.setRecruitment(recruitment);
            candidateRepository.save(candidate);
        } else {
            // Gérer le cas où le candidat ou le recrutement n'existe pas
            throw new IllegalArgumentException("Candidate or Recruitment not found");
        }
    }
    @Override
    public Optional<Recruitment> getRecruitmentByPostTitle(String postTitle) {
        return recruitmentRepository.findByPostTitle(postTitle);
    }

    @Override
    // Méthode pour calculer le pourcentage de correspondance entre l'expérience requise et l'expérience du candidat
    public double calculateExperienceMatch(int experienceRequired, int experienceCand) {
        // Calcul de la différence entre l'expérience requise et l'expérience du candidat
        int experienceDifference = Math.abs(experienceRequired - experienceCand);

        // Calcul du pourcentage de correspondance en fonction de la différence
        double percentageMatch = 100.0 - (experienceDifference * 100.0 / experienceRequired);

        // Assure que le pourcentage est dans la plage de 0 à 100
        return Math.max(0, Math.min(100, percentageMatch));
    }
    @Override
    public int getExperienceRequired(Long offerId) {
        Recruitment recruitment = recruitmentRepository.findById(offerId).orElse(null);
        return recruitment != null ? recruitment.getExperienceRequired() : 1;
    }
    @Override
    public int getExperience(Long idCandidate) {
        Candidate candidate = candidateRepository.findById(idCandidate).orElse(null);
        return candidate != null ? candidate.getExperienceCand() : 1; // Vous pouvez choisir une valeur par défaut appropriée
    }

}
