package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Payroll;
import tn.esprit.se.pispring.entities.Prime;
import tn.esprit.se.pispring.entities.User;

import java.util.List;

public interface PrimeService {
    List<Prime> retrieveAllPrimes();
    Prime addPrime(Prime prime);
    Prime updatePrime(Prime prime, Long idPrime);
    Prime retrievePrime(Long idPrime);
    void removePrime(Long idPrime);
    Prime affectPrimeUser(Prime prime, Long userId);
    Float getSumAmountForUserMonthYear(Long userId, String month, Integer year);
    List<Prime> getListPrimeByUserAndMonth(User user, String primeMonth, Integer primeYear);

}
