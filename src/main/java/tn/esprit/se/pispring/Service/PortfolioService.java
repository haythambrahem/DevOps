package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.*;
import tn.esprit.se.pispring.entities.*;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PortfolioService implements PortfolioInterface{
    PortfilioRepository portfilioRepository;
    UserRepository userRepository ;
    ConsultantRepository consultantRepository ;
    UserRepository getUserRepository ;
    ProjectRepository projectRepository;
    TaskRepository taskRepository ;
    MeetRepository meetRepository ;
    @Override
    public Portfolio addPortfolio(Portfolio p) {
        return portfilioRepository.save(p);
    }

    @Override
    public List<Portfolio> retrieveAllPortfolios() {
        return portfilioRepository.findAll();
    }

    @Override
    public List<User> retrieveAllUsers() {
        return null;
    }

    @Override
    public Portfolio updatePortfolio(Portfolio p) {
        return portfilioRepository.save(p);
    }

    @Override
    public Portfolio retrievePortfolio(Long idPortfolio) {
        return portfilioRepository.findById(idPortfolio).get();
    }

    @Override
    public void removePortfolio(Long idPortfolio) {
    portfilioRepository.deleteById(idPortfolio);
    }

    @Override
    public void affectUserToPortfolio(Long userId, Long portfolioId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        Portfolio portfolio = portfilioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + portfolioId + " not found"));


        if (user.getPortfolio() != null) {
            throw new IllegalArgumentException("User is already assigned to a portfolio");
        }


        if (portfolio.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is already assigned to this portfolio");
        }


        portfolio.getUsers().add(user);
        user.setPortfolio(portfolio);


        int currentNumberOfClients = portfolio.getNbr_client();
        portfolio.setNbr_client(currentNumberOfClients + 1);


        userRepository.save(user);
        portfilioRepository.save(portfolio);
    }


    @Override
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateConsultantSkillMonthly() {
        List<Portfolio> portfolios = portfilioRepository.findAll();
        for (Portfolio portfolio : portfolios) {
            int numberOfClients = portfolio.getUsers().size();

            Consultant consultant = portfolio.getConsultant();
            Skill currentSkill = consultant.getSkill();
            Date hireDate = consultant.getHireDate() ;
            long monthsSinceHire = calculateMonthsSinceHire(hireDate);

            if (numberOfClients > 30 && monthsSinceHire > 12) {
                consultant.setSkill(Skill.THREE_STAR);
            } else if (numberOfClients > 20 && monthsSinceHire > 6) {
                consultant.setSkill(Skill.TWO_STAR);
            } else if (numberOfClients > 10 && monthsSinceHire > 3) {
                consultant.setSkill(Skill.ONE_STAR);
            } else {
                consultant.setSkill(Skill.ONE_STAR);
            }

            consultantRepository.save(consultant);
        }
    }

    @Override
    public void planifierReunion(Long consultantId, Date dateReunion) {

        Optional<Consultant> consultantOptional = consultantRepository.findById(consultantId);
        if (consultantOptional.isPresent()) {
            Consultant consultant = consultantOptional.get();



            Portfolio portfolio = consultant.getPortfolio();
            if (portfolio != null) {
                portfolio.getMeeting_dates().add(consultant.getDate_last_meet());
            }

            consultant.setDate_last_meet(dateReunion);


            consultantRepository.save(consultant);
            portfilioRepository.save(portfolio) ;
        }
    }
    @Override
    public List<Portfolio> retrieveAllPortfoliosnonaffectes() {
        List<Portfolio> allPortfolios = portfilioRepository.findAll();

        List<Portfolio> portfoliosWithoutConsultants = allPortfolios.stream()
                .filter(portfolio -> portfolio.getConsultant() == null)
                .collect(Collectors.toList());

        return portfoliosWithoutConsultants;
    }

    @Override
    public int getMeetingsCountThisMonth() {
        int meetingsCount = 0;
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        List<Portfolio> portfolios = portfilioRepository.findAll();
        for (Portfolio portfolio : portfolios) {
            List<Date> meetingDates = portfolio.getMeeting_dates();
            if (meetingDates != null) {
                for (Date date : meetingDates) {
                    calendar.setTime(date);
                    int meetingMonth = calendar.get(Calendar.MONTH);
                    int meetingYear = calendar.get(Calendar.YEAR);
                    if (meetingMonth == currentMonth && meetingYear == currentYear) {
                        meetingsCount++;
                    }
                }
            }
        }
        return meetingsCount;

    }

    @Override
    public Map<PortfolioDomain, Integer> getPortfoliosCountByDomain() {
        Map<PortfolioDomain, Integer> portfoliosCountByDomain = new HashMap<>();
        List<Portfolio> portfolios = portfilioRepository.findAll();
        for (Portfolio portfolio : portfolios) {
            PortfolioDomain domain = portfolio.getDomain();
            portfoliosCountByDomain.put(domain, portfoliosCountByDomain.getOrDefault(domain, 0) + 1);
        }
        return portfoliosCountByDomain;
    }

    @Override
    public void desaffectUserToPortfolio(Long userId, Long portfolioId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        Portfolio portfolio = portfilioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + portfolioId + " not found"));

        if (user.getPortfolio() == null || !user.getPortfolio().equals(portfolio)) {
            throw new IllegalArgumentException("User is not assigned to the specified portfolio");
        }

        portfolio.getUsers().remove(user);
        user.setPortfolio(null);

        int currentNumberOfClients = portfolio.getNbr_client();
        if (currentNumberOfClients > 0) {
            portfolio.setNbr_client(currentNumberOfClients - 1);
        }
        else{ portfolio.setNbr_client(0);}

        userRepository.save(user);
        portfilioRepository.save(portfolio);
    }

    @Override
    public List<User> getUsersByPortfolioId(Long portfolioId) {
        Portfolio portfolio = portfilioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + portfolioId + " not found"));

        List<User> users = new ArrayList<>(portfolio.getUsers());

        return users;
    }


    private long calculateMonthsSinceHire(Date hireDate) {
        java.util.Date utilDate = new java.util.Date(hireDate.getTime());

        LocalDate hireLocalDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate currentDate = LocalDate.now();

        return hireLocalDate.until(currentDate).toTotalMonths();
    }




    private String getMonthYear(Date date) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("yyyy-MM");
        return monthYearFormat.format(date);
    }
    public Map<String, Map<String, Integer>> getPortfolioEvolution(Long portfolioId) {
        Map<String, Map<String, Integer>> portfolioEvolution = new LinkedHashMap<>();

        Portfolio portfolio = portfilioRepository.findById(portfolioId).orElse(null);
        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found");
        }

        List<Project> projects = projectRepository.findByProjectManager(portfolio.getPotfolio_manager());

        Map<String, Integer> tasksPerMonth = new HashMap<>();
        Map<String, Integer> meetingsPerMonth = new HashMap<>();

        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                String monthYear = getMonthYear(task.getTaskEnddate());
                tasksPerMonth.put(monthYear, tasksPerMonth.getOrDefault(monthYear, 0) + 1);
            }
        }

        for (Date meetingDate : portfolio.getMeeting_dates()) {
            String monthYear = getMonthYear(meetingDate);
            meetingsPerMonth.put(monthYear, meetingsPerMonth.getOrDefault(monthYear, 0) + 1);
        }

        portfolioEvolution.put("tasks", tasksPerMonth);
        portfolioEvolution.put("meetings", meetingsPerMonth);

        return portfolioEvolution;
    }

    public List<User> getUsersNonAffectes() {
        List<User> allUsers = userRepository.findAll();


        List<Portfolio> allPortfolios = portfilioRepository.findAll();

        Set<User> usersInPortfolios = new HashSet<>();

        for (Portfolio portfolio : allPortfolios) {
            usersInPortfolios.addAll(portfolio.getUsers());
        }

        List<User> usersNonAffectes = new ArrayList<>();

        for (User user : allUsers) {
            if (!usersInPortfolios.contains(user)) {
                usersNonAffectes.add(user);
            }
        }

        return usersNonAffectes;
    }

    @Scheduled(cron = "0 0 0 * * MON") // Runs every Monday
    public void updateMeetings() {
        List<Consultant> consultants = consultantRepository.findAll();

        for (Consultant consultant : consultants) {


            consultant.setNbrPassedMeet(consultant.getMeetings().stream()
                    .filter(meeting -> meeting.getMeetStatus() == MeetStatus.PASSED)
                    .count());

            long canceledMeetingsCount = consultant.getMeetings().stream()
                    .filter(meeting -> meeting.getMeetStatus() == MeetStatus.CANCELED)
                    .count();

            consultant.setNbrCanceledMeet(consultant.getNbrCanceledMeet() + canceledMeetingsCount);

            consultant.getMeetings().removeIf(meeting -> meeting.getMeetStatus() == MeetStatus.CANCELED);


            LocalDateTime lastMeetingDate = consultant.getMeetings().stream()
                    .map(meeting -> Instant.ofEpochMilli(meeting.getMeettdate().getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            Portfolio portfolio = consultant.getPortfolio();
            if (portfolio != null) {
                portfolio.setMeeting_dates(consultant.getMeetings().stream()
                        .filter(meeting -> meeting.getMeetStatus() == MeetStatus.PASSED)
                        .map(Meeting::getMeettdate)
                        .collect(Collectors.toList()));
                portfilioRepository.save(portfolio);
            }


            consultantRepository.save(consultant);
        }

    }




}
