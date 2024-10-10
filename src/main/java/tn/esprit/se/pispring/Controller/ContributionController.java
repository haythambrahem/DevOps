package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ContributionService;
import tn.esprit.se.pispring.entities.Contribution;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/contribution")
public class ContributionController {
    ContributionService contributionService;

    @GetMapping("/retrieve-all-contributions")
    public List<Contribution> getPrimes() {
        List<Contribution> listPrimes = contributionService.retrieveAllContributions();
        return listPrimes;
    }
    @PostMapping("/add-contribution")
    public Contribution addPrime(@RequestBody Contribution prime) {
        return contributionService.addContribution(prime);
    }

    @PutMapping("/update-contribution/{idprime}")
    public Contribution updatePrime(@RequestBody Contribution prime, @PathVariable Long idprime) {
        return contributionService.updateContribution(prime,idprime);
    }

    @PutMapping("/affect-contribution/{userId}")
    public Contribution affectPrimeUser(@RequestBody Contribution prime, @PathVariable Long userId) {
        return contributionService.affectContributionUser(prime,userId);
    }

    @GetMapping("/retrieve-contribution/{id}")
    public Contribution getPrime(@PathVariable Long id) {return contributionService.retrieveContribution(id);}

    @DeleteMapping("/remove-contribution/{id}")
    public void removePrime(@PathVariable Long id) {
        contributionService.removeContribution(id);
    }
}
