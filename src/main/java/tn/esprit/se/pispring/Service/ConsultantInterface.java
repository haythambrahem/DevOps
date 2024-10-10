package tn.esprit.se.pispring.Service;

import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Consultant;
import tn.esprit.se.pispring.entities.CustomerTracking;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Repository

public interface ConsultantInterface {
    Consultant addConsultant(Consultant c) ;

    List<Consultant> retrieveAllConsultants();


    Consultant updateConsultant(Consultant c);

    Consultant retrieveConsultant(Long idConsultant);

    void removeConsultant(Long idConsultant);

    void affectPortfolioaConsultant(Long idConsultant ,Long idPortfolio);
    Map<String, Integer> countMeetingsPerUser(Long consultantId);
    List<Consultant> getSortedConsultants(String sortBy);

    int getTotalConsultants();

    Map<String, Integer> getConsultantsByGender() ;

    Map<String, Integer> getConsultantsBySkill() ;

    int getHiredConsultantsCountThisMonth();
    List<Consultant> getConsultantsBySkillAndSeniority() ;
}
