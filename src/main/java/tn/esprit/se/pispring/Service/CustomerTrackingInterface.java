package tn.esprit.se.pispring.Service;

import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.CustomerTracking;

import java.util.List;

@Repository
public interface CustomerTrackingInterface {
    CustomerTracking addCustomerTracking(CustomerTracking ct) ;

    List<CustomerTracking> retrieveAllCustomerTrackings();


    CustomerTracking updateCustomerTracking(CustomerTracking ct);

    CustomerTracking retrieveCustomerTracking(Long idCustomerTracking);

    void removeCustomerTracking(Long idCustomerTracking);
    int getTotalUsers();

}
