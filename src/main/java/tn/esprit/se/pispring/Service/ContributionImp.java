package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.ContributionRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Contribution;

import tn.esprit.se.pispring.entities.User;


import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class ContributionImp implements ContributionService{
    ContributionRepository contributionRepository;
    UserRepository userRepository;
    @Override
    public List<Contribution> retrieveAllContributions() {
        return contributionRepository.findAll();
    }

    @Override
    public Contribution addContribution(Contribution contribution) {
        return contributionRepository.save(contribution);
    }

    @Override
    public Contribution updateContribution(Contribution contribution, Long idContribution) {
        Contribution p = contributionRepository.findById(idContribution).get();
        if (p != null){
            contribution.setContribution_id(idContribution);
            return contributionRepository.save(contribution);
        }
        return null;
    }

    @Override
    public Contribution retrieveContribution(Long idContribution) {
        return contributionRepository.findById(idContribution).get();
    }

    @Override
    public void removeContribution(Long idContribution) {
        contributionRepository.deleteById(idContribution);
    }
    public Contribution affectContributionUser(Contribution prime, Long userId) {
        User user = userRepository.findById(userId).get();
        prime.setUser(user);
        contributionRepository.save(prime);
        return prime;
    }

    public Float getSumAmountForUserMonthYear(Long userId, String month, Integer year) {
        return contributionRepository.getSumAmountForUserMonthYear(userId, month, year);
    }

    @Override
    public List<Contribution> getListContributionByUserAndMonth(User user, String contribution_month, Integer contribution_year) {
        return contributionRepository.findByUserAndContributionMonthAndContributionYear(user,contribution_month,contribution_year);
    }


}
