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


        ct.setDate_last_meet(new Date());


        LocalDate lastMeetLocalDate = ct.getDate_last_meet().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


        Period period = calculatePeriod(lastMeetLocalDate, LocalDate.now());


        String friendlyPeriod = convertPeriodToFriendlyFormat(ct.getDate_last_meet());


        ct.setDate_last_meeet(friendlyPeriod);


        return customerTrackingRepository.save(ct);
    }

    private Period calculatePeriod(LocalDate lastMeetingDate, LocalDate currentDate) {
        return Period.between(lastMeetingDate, currentDate);
    }


    private String convertPeriodToFriendlyFormat(Date lastMeetDate) {

        LocalDate lastMeetLocalDate = lastMeetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();


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
