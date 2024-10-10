package tn.esprit.se.pispring.Controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.PrimeService;
import tn.esprit.se.pispring.entities.Prime;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/prime")
public class PrimeController {
    PrimeService primeService;

    @GetMapping("/retrieve-all-primes")
    public List<Prime> getPrimes() {
        List<Prime> listPrimes = primeService.retrieveAllPrimes();
        return listPrimes;
    }
    @PostMapping("/add-prime")
    public Prime addPrime(@RequestBody Prime prime) {
        return primeService.addPrime(prime);
    }

    @PutMapping("/update-prime/{idprime}")
    public Prime updatePrime(@RequestBody Prime prime, @PathVariable Long idprime) {
        return primeService.updatePrime(prime,idprime);
    }

    @PutMapping("/affect-prime/{userId}")
    public Prime affectPrimeUser(@RequestBody Prime prime, @PathVariable Long userId) {
        return primeService.affectPrimeUser(prime,userId);
    }

    @GetMapping("/retrieve-prime/{id}")
    public Prime getPrime(@PathVariable Long id) {return primeService.retrievePrime(id);}

    @DeleteMapping("/remove-prime/{id}")
    public void removePrime(@PathVariable Long id) {
        primeService.removePrime(id);
    }
}
