package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.ConsultantRepository;
import tn.esprit.se.pispring.Repository.CustomerTrackingRepository;
import tn.esprit.se.pispring.Repository.PortfilioRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.*;

import java.util.*;


@Service
@AllArgsConstructor
@Slf4j
public class ConsultantService implements ConsultantInterface{
    ConsultantRepository consultantRepository ;
    UserRepository userRepository ;
    PortfilioRepository portfilioRepository;
    CustomerTrackingRepository customerTrackingRepository ;
    @Override
    public Consultant addConsultant(Consultant c) {
        return consultantRepository.save(c);
    }
    @Override
    public Consultant updateConsultant(Consultant c) {
        return consultantRepository.save(c);
    }

    @Override
     public List<Consultant> retrieveAllConsultants() {
         return consultantRepository.findAll() ;
     }

    @Override
    public Consultant retrieveConsultant(Long idConsultant) {
        return consultantRepository.findById(idConsultant).get();
    }

    @Override
    public void removeConsultant(Long idConsultant) {
        consultantRepository.deleteById(idConsultant);
    }

    @Override
    public void affectPortfolioaConsultant(Long idConsultant, Long idPortfolio) {

        Portfolio p = portfilioRepository.findById(idPortfolio).orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + idPortfolio + " not found"));
        Consultant c = consultantRepository.findById(idConsultant).orElseThrow(() -> new IllegalArgumentException("Consultant with id " + idConsultant + " not found"));

        if (p.getCreation_date().after(c.getHireDate())) {
            c.setPortfolio(p);
            consultantRepository.save(c);
        } else {
            throw new IllegalArgumentException("Portfolio creation date must be after consultant hire date");
        }
    }



    @Override
    public Map<String, Integer> countMeetingsPerUser(Long consultantId) {
        Map<String, Integer> meetingsPerUser = new HashMap<>();
       List<CustomerTracking>  customerTrackings = customerTrackingRepository.findByConsultantId(consultantId) ;
        for (CustomerTracking tracking : customerTrackings ) {
            if (tracking.getUser().getPortfolio().getConsultant().getConsultant_id().equals(consultantId)) {
                String userName = tracking.getUser().getFirstName() + " " + tracking.getUser().getLastName();
                int currentCount = meetingsPerUser.getOrDefault(userName, 0);
                meetingsPerUser.put(userName, currentCount + 1);
            }
        }
        return meetingsPerUser;    }

    @Override
    public List<Consultant> getSortedConsultants(String sortBy) {
        List<Consultant> consultants;

        switch (sortBy) {
            case "consultant_id":
                consultants = consultantRepository.findAllByOrderByID();
                break;
            case "consultant_firstname":
                consultants = consultantRepository.findAllByOrderByConsultantFirstname();
                break;
            case "consultant_lastname":
                consultants = consultantRepository.findAllByOrderByConsultantLastname();
                break;
            case "HireDate":
                consultants = consultantRepository.findAllByOrderByHireDate();
                break;
            case "skill":
                consultants = consultantRepository.findAllByOrderBySkill();
                break;
            default:
                consultants = consultantRepository.findAll();
                break;
        }
        return consultants;
    }

    @Override
    public int getTotalConsultants() {
        return consultantRepository.findAll().size();
    }

    @Override
    public Map<String, Integer> getConsultantsByGender() {
        Map<String, Integer> genderCounts = new HashMap<>();

        List<Consultant> allConsultants = consultantRepository.findAll();


        int maleCount = 0;
        int femaleCount = 0;
        for (Consultant consultant : allConsultants) {
            if (consultant.getGender() == Gender.male) {
                maleCount++;
            } else if (consultant.getGender() == Gender.female) {
                femaleCount++;
            }
        }

        genderCounts.put("male", maleCount);
        genderCounts.put("female", femaleCount);

        return genderCounts;
    }

    @Override
    public Map<String, Integer> getConsultantsBySkill() {
        Map<String, Integer> skillCounts = new HashMap<>();

        List<Consultant> allConsultants = consultantRepository.findAll();

        int oneStarCount = 0;
        int twoStarCount = 0;
        int threeStarCount = 0;

        for (Consultant consultant : allConsultants) {
            if (consultant.getSkill() == Skill.ONE_STAR) {
                oneStarCount++;
            } else if (consultant.getSkill() == Skill.TWO_STAR) {
                twoStarCount++;
            }
            else if (consultant.getSkill() == Skill.THREE_STAR) {
                threeStarCount++;
            }
        }

        skillCounts.put("oneStar", oneStarCount);
        skillCounts.put("twoStar", twoStarCount);
        skillCounts.put("threeStar", threeStarCount);

        return skillCounts;    }

    @Override
    public int getHiredConsultantsCountThisMonth() {
        int hiredConsultantsCount = 0;
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        List<Consultant> consultants = consultantRepository.findAll();
        for (Consultant consultant : consultants) {
            Calendar hireDateCalendar = Calendar.getInstance();
            hireDateCalendar.setTime(consultant.getHireDate());
            int hireMonth = hireDateCalendar.get(Calendar.MONTH);
            int hireYear = hireDateCalendar.get(Calendar.YEAR);
            if (hireMonth == currentMonth && hireYear == currentYear) {
                hiredConsultantsCount++;
            }
        }
        return hiredConsultantsCount;
    }

    @Override
    public List<Consultant> getConsultantsBySkillAndSeniority() {
        List<Consultant> consultants = consultantRepository.findAllByOrderBySkillAscHireDateAsc();

        List<Consultant> threeStarConsultants = new ArrayList<>();
        List<Consultant> twoStarConsultants = new ArrayList<>();
        List<Consultant> oneStarConsultants = new ArrayList<>();

        for (Consultant consultant : consultants) {
            switch (consultant.getSkill()) {
                case THREE_STAR:
                    threeStarConsultants.add(consultant);
                    break;
                case TWO_STAR:
                    twoStarConsultants.add(consultant);
                    break;
                case ONE_STAR:
                    oneStarConsultants.add(consultant);
                    break;
                default:
                    break;
            }
        }

        List<Consultant> sortedConsultants = new ArrayList<>();
        sortedConsultants.addAll(threeStarConsultants);
        sortedConsultants.addAll(twoStarConsultants);
        sortedConsultants.addAll(oneStarConsultants);

        return sortedConsultants;
    }
    public Map<String, Map<String, Long>> getStatistiquesMeetings() {
        List<Consultant> consultants = consultantRepository.findAll();
        Map<String, Map<String, Long>> statistiques = new HashMap<>();

        for (Consultant consultant : consultants) {
            Map<String, Long> consultantStats = new HashMap<>();
            consultantStats.put("nbrPassedMeet", consultant.getNbrPassedMeet());
            consultantStats.put("nbrFirstMeet", consultant.getNbrFirstMeet());
            consultantStats.put("nbrAffectation", consultant.getNbrAffectation());

            String nomConsultant = consultant.getConsultant_firstname() + " " + consultant.getConsultant_lastname();

            statistiques.put(nomConsultant, consultantStats);
        }

        return statistiques;
    }

}



