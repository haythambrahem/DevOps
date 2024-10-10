package tn.esprit.se.pispring.Service;

import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Portfolio;
import tn.esprit.se.pispring.entities.PortfolioDomain;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface PortfolioInterface {
    Portfolio addPortfolio(Portfolio p) ;

    List<Portfolio> retrieveAllPortfolios();
    List<User> retrieveAllUsers();


    Portfolio updatePortfolio(Portfolio p);

    Portfolio retrievePortfolio(Long idPortfolio);

    void removePortfolio(Long idPortfolio);
    void affectUserToPortfolio(Long userId, Long portfolioId);

    void updateConsultantSkillMonthly();

    void planifierReunion(Long consultantId, Date dateReunion);

    int getMeetingsCountThisMonth() ;
    Map<PortfolioDomain, Integer> getPortfoliosCountByDomain();
    void desaffectUserToPortfolio(Long userId, Long portfolioId);
    List<User> getUsersByPortfolioId(Long portfolioId) ;
    List<Portfolio> retrieveAllPortfoliosnonaffectes() ;

}
