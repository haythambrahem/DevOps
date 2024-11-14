package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.PrimeRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Prime;
import tn.esprit.se.pispring.entities.User;

import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class PrimeImp implements PrimeService {
    PrimeRepository primeRepository;
    UserRepository userRepository;
    NoteService noteService;
    @Override
    public List<Prime> retrieveAllPrimes() {
        return primeRepository.findAll();
    }

    @Override
    public Prime addPrime(Prime prime) {
        return primeRepository.save(prime);
    }

    @Override
    public Prime updatePrime(Prime prime, Long idPrime) {
        Prime p = primeRepository.findById(idPrime).get();
        if (p != null){
            prime.setPrime_id(idPrime);
            return primeRepository.save(prime);
        }
        return null;
    }

    @Override
    public Prime retrievePrime(Long idPrime) {
        return primeRepository.findById(idPrime).get();
    }

    @Override
    public void removePrime(Long idPrime) {
             primeRepository.deleteById(idPrime);

    }

    @Override
    public Prime affectPrimeUser(Prime prime, Long userId) {
        User user = userRepository.findById(userId).get();
        prime.setUser(user);
        primeRepository.save(prime);
        return prime;
    }

    public Float getSumAmountForUserMonthYear(Long userId, String month, Integer year) {
        return primeRepository.getSumAmountForUserMonthYear(userId, month, year);
    }

    @Override
    public List<Prime> getListPrimeByUserAndMonth(User user, String primeMonth, Integer primeYear) {
        return primeRepository.findPrimesByUserAndMonthAndYear(user,primeMonth,primeYear);
    }




    }
