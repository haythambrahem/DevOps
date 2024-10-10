package tn.esprit.se.pispring.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ConsultantService;
import tn.esprit.se.pispring.Service.PortfolioService;
import tn.esprit.se.pispring.entities.Portfolio;
import tn.esprit.se.pispring.entities.PortfolioDomain;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/PORTFOLIO")
@Slf4j
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@CrossOrigin(origins = "http://localhost:4200")

public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService ;
    private ConsultantService consultantService ;

    @GetMapping("/retrieve-all-portfolio")
    public List<Portfolio> getPortfolios() {
        List<Portfolio> listPortfolios = portfolioService.retrieveAllPortfolios();
        return listPortfolios;
    }
    @GetMapping("/retrieve-all-portfoliononaffectes")
    public List<Portfolio> retrieveAllPortfoliosnonaffectes() {
        List<Portfolio> listPortfolios = portfolioService.retrieveAllPortfoliosnonaffectes();
        return listPortfolios;
    }
    @GetMapping("/retrieve-all-user")
    public List<User> getUsers() {
        List<User> listUsers = portfolioService.retrieveAllUsers();
        return listUsers;
    }

    @GetMapping("/retrieve-Portfolio/{portfolio-id}")
    public  Portfolio retrievePortfolio(@PathVariable("portfolio-id") Long portfolioId) {
        return portfolioService.retrievePortfolio(portfolioId);
    }

    @PostMapping("/add-Portfolio")
    public  Portfolio addPortfolio(@RequestBody  Portfolio p) {
        return portfolioService.addPortfolio(p) ;
    }

    @DeleteMapping("/remove-portfolio/{portfolio-id}")
    public void removePortfolio(@PathVariable("portfolio-id") Long portfolioId) {
        portfolioService.removePortfolio(portfolioId);
    }

    @PutMapping("/update-portfolio")
    public Portfolio updatePortfolio(@RequestBody Portfolio p) {
        Portfolio portfolio = portfolioService.updatePortfolio(p);
        return portfolio;
    }
    @PutMapping("/affecterportfolioAConsultant/{idConsultant}/{idPortfolio}")
    @ResponseBody
    public void affectPortfolioaConsultant(@PathVariable("idConsultant") Long idConsultant, @PathVariable("idPortfolio") Long idPortfolio) {
        consultantService.affectPortfolioaConsultant(idConsultant ,idPortfolio);

    }
    @PutMapping ("affceter/{portfolioId}/{userId}")
    public ResponseEntity<String> affectUserToPortfolio(@PathVariable Long portfolioId, @PathVariable Long userId) {
        try {
            portfolioService.affectUserToPortfolio(userId, portfolioId);
            return new ResponseEntity<>("User added to portfolio successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping ("disaffceter/{portfolioId}/{userId}")
    public ResponseEntity<String> desaffectUserToPortfolio( @PathVariable Long portfolioId,@PathVariable Long userId) {
        try {
            portfolioService.desaffectUserToPortfolio(userId, portfolioId);
            return new ResponseEntity<>("User removed from portfolio successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/updateSkillMonthly")
    public ResponseEntity<String> updateConsultantSkillMonthly() {
        try {
            portfolioService.updateConsultantSkillMonthly();
            return new ResponseEntity<>("Mise à jour des compétences des consultants effectuée avec succès.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la mise à jour des compétences des consultants : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/evolution/{portfolioId}")
    public ResponseEntity<Map<String, Map<String, Integer>>> getPortfolioEvolution(@PathVariable Long portfolioId) {
        Map<String, Map<String, Integer>> portfolioEvolution = portfolioService.getPortfolioEvolution(portfolioId);
        return new ResponseEntity<>(portfolioEvolution, HttpStatus.OK);
    }

    @PostMapping("/schedule-meeting/{consultantId}/{dateReunion}")
    public ResponseEntity<?> scheduleMeeting(@PathVariable Long consultantId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateReunion) {
        try {
            portfolioService.planifierReunion(consultantId, dateReunion);
            return ResponseEntity.ok("Meeting scheduled successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la plannification de réunion : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/meetingsCount")
    public int getMeetingsCountThisMonth() {
        return portfolioService.getMeetingsCountThisMonth();
    }
    @GetMapping("/countByDomain")
    public Map<PortfolioDomain, Integer> getPortfoliosCountByDomain() {
        return portfolioService.getPortfoliosCountByDomain();
    }
    @GetMapping("/{portfolioId}/users")
    public List<User> getUsersByPortfolioId(@PathVariable Long portfolioId) {
        return portfolioService.getUsersByPortfolioId(portfolioId);
    }
    @GetMapping("/non-affectes")
    public ResponseEntity<List<User>> getUsersNonAffectes() {
        List<User> usersNonAffectes = portfolioService.getUsersNonAffectes();
        return ResponseEntity.ok(usersNonAffectes);
    }

}
