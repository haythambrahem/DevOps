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
        // Vérifier si l'utilisateur et le portefeuille existent
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        Portfolio portfolio = portfilioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + portfolioId + " not found"));

        // Vérifier si l'utilisateur est déjà associé à un portfolio
        if (user.getPortfolio() != null) {
            throw new IllegalArgumentException("User is already assigned to a portfolio");
        }

        // Vérifier si le portefeuille contient déjà cet utilisateur
        if (portfolio.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is already assigned to this portfolio");
        }

        // Ajouter l'utilisateur au portefeuille et mettre à jour les références
        portfolio.getUsers().add(user);
        user.setPortfolio(portfolio);

        // Incrémenter le nombre de clients du portefeuille
        int currentNumberOfClients = portfolio.getNbr_client();
        portfolio.setNbr_client(currentNumberOfClients + 1);

        // Sauvegarder les modifications
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
        // Charger le consultant à partir de la base de données
        Optional<Consultant> consultantOptional = consultantRepository.findById(consultantId);
        if (consultantOptional.isPresent()) {
            Consultant consultant = consultantOptional.get();



            // Ajouter la date de réunion à la liste des dates de réunion dans le portfolio du consultant
            Portfolio portfolio = consultant.getPortfolio();
            if (portfolio != null) {
                portfolio.getMeeting_dates().add(consultant.getDate_last_meet());
            }

            // Mettre à jour la date de la dernière réunion du consultant
            consultant.setDate_last_meet(dateReunion);


            // Enregistrer les modifications dans la base de données
            consultantRepository.save(consultant);
            portfilioRepository.save(portfolio) ;
        }
    }
    @Override
    public List<Portfolio> retrieveAllPortfoliosnonaffectes() {
        List<Portfolio> allPortfolios = portfilioRepository.findAll();

        // Filtrer les portefeuilles non affectés à aucun consultant
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
        // Vérifier si l'utilisateur et le portefeuille existent
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        Portfolio portfolio = portfilioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + portfolioId + " not found"));

        // Vérifier si l'utilisateur est associé au portefeuille
        if (user.getPortfolio() == null || !user.getPortfolio().equals(portfolio)) {
            throw new IllegalArgumentException("User is not assigned to the specified portfolio");
        }

        // Retirer l'utilisateur du portefeuille et mettre à jour les références
        portfolio.getUsers().remove(user);
        user.setPortfolio(null);

        // Décrémenter le nombre de clients du portefeuille
        int currentNumberOfClients = portfolio.getNbr_client();
        if (currentNumberOfClients > 0) {
            portfolio.setNbr_client(currentNumberOfClients - 1);
        }
        else{ portfolio.setNbr_client(0);}

        // Sauvegarder les modifications
        userRepository.save(user);
        portfilioRepository.save(portfolio);
    }

    @Override
    public List<User> getUsersByPortfolioId(Long portfolioId) {
        // Vérifier si le portefeuille existe
        Portfolio portfolio = portfilioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio with id " + portfolioId + " not found"));

        // Récupérer la liste des utilisateurs affectés à ce portefeuille
        List<User> users = new ArrayList<>(portfolio.getUsers());

        return users;
    }


    private long calculateMonthsSinceHire(Date hireDate) {
        java.util.Date utilDate = new java.util.Date(hireDate.getTime());

        LocalDate hireLocalDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate currentDate = LocalDate.now();

        return hireLocalDate.until(currentDate).toTotalMonths();
    }



  /*  public Map<String, Map<String, Integer>> getPortfolioEvolution(Long portfolioId) {
        Map<String, Map<String, Integer>> portfolioEvolution = new LinkedHashMap<>();

        // Récupérer le portefeuille spécifié
        Portfolio portfolio = portfilioRepository.findById(portfolioId).orElse(null);
        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found");
        }

        // Récupérer les projets associés au portefeuille avec la condition spécifiée
        List<Project> projects = projectRepository.findAll();
        List<Project> filteredProjects = projects.stream()
                .filter(project -> project.getProject_manager().equals(portfolio.getPotfolio_manager()))
                .collect(Collectors.toList());


        // Compter le nombre de réunions pour chaque mois
        Map<String, Integer> meetingsPerMonth = new HashMap<>();
        // Compter le nombre de tâches effectuées pour chaque mois
        Map<String, Integer> tasksPerMonth = new HashMap<>();
        List<Task> allTasks = new ArrayList<>();

        for (Project project : filteredProjects) {

            allTasks.addAll(project.getTasks());
        }

            for (Task task : allTasks) {
                String monthYear = getMonthYear(task.getTask_enddate());
                tasksPerMonth.put(monthYear, tasksPerMonth.getOrDefault(monthYear, 0) + 1);
            }


        // Ajouter les résultats dans la structure de données finale
        portfolioEvolution.put("meetings", meetingsPerMonth);
        portfolioEvolution.put("tasks", tasksPerMonth);

        return portfolioEvolution;
    }*/

    private String getMonthYear(Date date) {
        SimpleDateFormat monthYearFormat = new SimpleDateFormat("yyyy-MM");
        return monthYearFormat.format(date);
    }
    public Map<String, Map<String, Integer>> getPortfolioEvolution(Long portfolioId) {
        Map<String, Map<String, Integer>> portfolioEvolution = new LinkedHashMap<>();

        // Récupérer le portefeuille spécifié
        Portfolio portfolio = portfilioRepository.findById(portfolioId).orElse(null);
        if (portfolio == null) {
            throw new RuntimeException("Portfolio not found");
        }

        // Récupérer les projets associés au portefeuille avec la condition spécifiée
        List<Project> projects = projectRepository.findByProjectManager(portfolio.getPotfolio_manager());

        // Compter le nombre de tâches effectuées et de réunions pour chaque mois
        Map<String, Integer> tasksPerMonth = new HashMap<>();
        Map<String, Integer> meetingsPerMonth = new HashMap<>();

        // Pour les tâches
        for (Project project : projects) {
            for (Task task : project.getTasks()) {
                String monthYear = getMonthYear(task.getTaskEnddate());
                tasksPerMonth.put(monthYear, tasksPerMonth.getOrDefault(monthYear, 0) + 1);
            }
        }

        // Pour les réunions
        for (Date meetingDate : portfolio.getMeeting_dates()) {
            String monthYear = getMonthYear(meetingDate);
            meetingsPerMonth.put(monthYear, meetingsPerMonth.getOrDefault(monthYear, 0) + 1);
        }

        // Ajouter les résultats dans la structure de données finale
        portfolioEvolution.put("tasks", tasksPerMonth);
        portfolioEvolution.put("meetings", meetingsPerMonth);

        return portfolioEvolution;
    }

    public List<User> getUsersNonAffectes() {
        // Récupérer tous les utilisateurs
        List<User> allUsers = userRepository.findAll();

        // Récupérer tous les portefeuilles
        List<Portfolio> allPortfolios = portfilioRepository.findAll();

        // Créer un ensemble pour stocker tous les utilisateurs affectés à un portefeuille
        Set<User> usersInPortfolios = new HashSet<>();

        // Parcourir tous les portefeuilles pour récupérer les utilisateurs affectés
        for (Portfolio portfolio : allPortfolios) {
            usersInPortfolios.addAll(portfolio.getUsers());
        }

        // Liste pour stocker les utilisateurs non affectés
        List<User> usersNonAffectes = new ArrayList<>();

        // Parcourir tous les utilisateurs
        for (User user : allUsers) {
            // Vérifier si l'utilisateur n'est pas affecté à un portefeuille
            if (!usersInPortfolios.contains(user)) {
                // Ajouter l'utilisateur à la liste des utilisateurs non affectés
                usersNonAffectes.add(user);
            }
        }

        return usersNonAffectes;
    }

    // Scheduled method to update meetings weekly
    @Scheduled(cron = "0 0 0 * * MON") // Runs every Monday
    public void updateMeetings() {
        List<Consultant> consultants = consultantRepository.findAll();

        for (Consultant consultant : consultants) {

            //  total duration of meetings for the consultant
          /*  Duration totalMeetingDuration = consultant.getMeetings().stream()
                    .filter(meeting -> meeting.getMeetStatus() == MeetStatus.PASSED)
                    .map(Meeting::getDureeReunion)
                    .reduce(Duration::plus)
                    .orElse(Duration.ZERO);*/

            // Update consultant's total meeting duration
         //   consultant.setDureeTotaleReunions(totalMeetingDuration);

            // Update consultant's number of meetings passed
            consultant.setNbrPassedMeet(consultant.getMeetings().stream()
                    .filter(meeting -> meeting.getMeetStatus() == MeetStatus.PASSED)
                    .count());

            // Update consultant's number of canceled meetings
            long canceledMeetingsCount = consultant.getMeetings().stream()
                    .filter(meeting -> meeting.getMeetStatus() == MeetStatus.CANCELED)
                    .count();

            consultant.setNbrCanceledMeet(consultant.getNbrCanceledMeet() + canceledMeetingsCount);

            consultant.getMeetings().removeIf(meeting -> meeting.getMeetStatus() == MeetStatus.CANCELED);


            // Update the date of the last meeting
            LocalDateTime lastMeetingDate = consultant.getMeetings().stream()
                    .map(meeting -> Instant.ofEpochMilli(meeting.getMeettdate().getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            // Update portfolio's meeting dates
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
