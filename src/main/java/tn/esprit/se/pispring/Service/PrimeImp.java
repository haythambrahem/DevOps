package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.PrimeRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Prime;
import tn.esprit.se.pispring.entities.User;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    //@Scheduled(cron = "*/10 * * * * *") // Execute at 00:00:00 on the 26th of each month
    public void generatePrimeForCurrentMonth() {
        String monthName = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = Year.now().getValue();
        String designation = "Prime de transport";
        Float amount = 100F;
        List<User> users = userRepository.findAll();
        for (User user : users
        ) {
            Prime prime = new Prime();
            prime.setPrime_month(monthName);
            prime.setPrime_year(year);
            prime.setUser(user);
            prime.setPrime_designation(designation);
            prime.setValue_amount(amount);
            primeRepository.save(prime);
            log.info("############# Cron tab job ####################");
        }
    }

    //@Scheduled(cron = "*/7 * * * * *")
    public void primeByNotePerfermance(){
        Map<User, Long> notes = noteService.countUserOccurrencesForNote1();
        String monthName = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = Year.now().getValue();
        String designation = "Performance prime";
        notes.forEach((k,v) -> {
            System.out.println("User "+k.getFirstName()+" "+k.getLastName()+" "+k.getId());
            System.out.println("Note "+v);
            User user = userRepository.findById(k.getId()).get();
            Prime prime = new Prime();
            long amount = k.getSalaire()*v;
            prime.setPrime_month(monthName);
            prime.setPrime_year(year);
            prime.setPrime_designation(designation);
            prime.setValue_amount((float)amount);
            prime.setUser(user);
            primeRepository.save(prime);
        });
    }

    }
