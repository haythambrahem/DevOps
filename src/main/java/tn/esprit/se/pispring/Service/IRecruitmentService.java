package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Recruitment;
import tn.esprit.se.pispring.entities.RecruitmentStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRecruitmentService {
    Recruitment addOrUpdateRecruitment(Recruitment R);

    void removeRecruitment(Long offerId);

    Recruitment retrieveRecruitment(Long offerId);

    List<Recruitment> retrieveAllRecruitments();

    List<String> getPostTitles();

    Optional<Recruitment> getPostTitleDetails(String postTitle);

    double calculateAverageSalaryRange();

    Map<String, Integer> getRecruitmentsPerHiringManager();

    Map<String, Integer> getOpenPositionsByLocation();

    Map<String, Map<RecruitmentStatus, Integer>> analyzeRecruitmentTrend(int startYear, int endYear);

    Map<String, Map<String, Integer>> analyzePostTitleTrend(String postTitle, int startYear, int endYear);

    Map<String, Integer> analyzeRecruitmentCandidateRelation();

    void assignCandidateToRecruitment(Long idCandidate, Long offerId);

    Optional<Recruitment> getRecruitmentByPostTitle(String postTitle);

    // Méthode pour calculer le pourcentage de correspondance entre l'expérience requise et l'expérience du candidat
    double calculateExperienceMatch(int experienceRequired, int experienceCand);

    int getExperienceRequired(Long offerId);

    int getExperience(Long idCandidate);
}
