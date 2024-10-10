package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.CustomerTrackingRepository;
import tn.esprit.se.pispring.entities.CustomerTracking;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerTrackingService implements CustomerTrackingInterface {
    CustomerTrackingRepository customerTrackingRepository;

    @Override
    public CustomerTracking addCustomerTracking(CustomerTracking ct) {
        return customerTrackingRepository.save(ct);
    }

    @Override
    public List<CustomerTracking> retrieveAllCustomerTrackings() {
        return  customerTrackingRepository.findAll() ;

    }

    @Override
    public CustomerTracking updateCustomerTracking(CustomerTracking ct) {
        ct = customerTrackingRepository.save(ct);

        // Mettre à jour la date de la dernière rencontre
        ct.setDate_last_meet(new Date());

        // Convertir java.util.Date en java.time.LocalDate
        LocalDate lastMeetLocalDate = ct.getDate_last_meet().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculer la période entre la dernière rencontre et la date actuelle
        Period period = calculatePeriod(lastMeetLocalDate, LocalDate.now());

        // Convertir la période en format convivial
        String friendlyPeriod = convertPeriodToFriendlyFormat(ct.getDate_last_meet());

        // Mettre à jour l'attribut date_last_meeet avec la chaîne conviviale calculée
        ct.setDate_last_meeet(friendlyPeriod);

        // Sauvegarder les modifications dans la base de données
        return customerTrackingRepository.save(ct);
    }

    private Period calculatePeriod(LocalDate lastMeetingDate, LocalDate currentDate) {
        return Period.between(lastMeetingDate, currentDate);
    }


    private String convertPeriodToFriendlyFormat(Date lastMeetDate) {
        // Convertir java.util.Date en java.time.LocalDate
        LocalDate lastMeetLocalDate = lastMeetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculer la période entre la dernière réunion et la date actuelle
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(lastMeetLocalDate, currentDate);

        if (period.getYears() > 0) {
            return formatYears(period.getYears());
        } else if (period.getMonths() > 0) {
            return formatMonths(period.getMonths());
        } else if (period.getDays() > 0) {
            return formatDays(period.getDays());
        } else {
            return "Aujourd'hui";
        }
    }

    private String formatYears(int years) {
        return "Il y a " + years + (years > 1 ? " ans" : " an");
    }

    private String formatMonths(int months) {
        return "Il y a " + months + (months > 1 ? " mois" : " mois");
    }

    private String formatDays(int days) {
        if (days >= 7) {
            long weeks = days / 7;
            return "Il y a " + weeks + (weeks > 1 ? " semaines" : " semaine");
        } else {
            return "Il y a " + days + (days > 1 ? " jours" : " jour");
        }
    }

    @Override
    public CustomerTracking retrieveCustomerTracking(Long idCustomerTracking) {
        return null;
    }

    @Override
    public void removeCustomerTracking(Long idCustomerTracking) {

    }

    @Override
    public int getTotalUsers() {
       return customerTrackingRepository.findAll().size() ;
    }
}
