package tn.esprit.se.pispring.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.CustomerTrackingService;
import tn.esprit.se.pispring.entities.CustomerTracking;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/CUSTUMERTRACKING")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class CustomerTrackingController {
    @Autowired
    private CustomerTrackingService customerTrackingService;

    @GetMapping("/retrieve-all-CustomerTracking")
    public List<CustomerTracking> getCustomerTrackings() {
        List<CustomerTracking> listCustomerTracking = customerTrackingService.retrieveAllCustomerTrackings() ;
        return listCustomerTracking;
    }

    @GetMapping("/retrieve-CustomerTracking/{customerTracking-id}")
    public  CustomerTracking retrievecustomerTracking(@PathVariable("customerTracking-id") Long idCustomerTracking) {
        return customerTrackingService.retrieveCustomerTracking(idCustomerTracking);
    }

    @PostMapping("/add-CustomerTracking")
    public  CustomerTracking addcustomerTracking(@RequestBody  CustomerTracking c) {
        return customerTrackingService.addCustomerTracking(c) ;
    }

    @DeleteMapping("/remove-customerTracking/{customerTracking-id}")
    public void removecustomerTracking(@PathVariable("customerTracking-id") Long idCustomerTracking) {
        customerTrackingService.removeCustomerTracking(idCustomerTracking);
    }

    @PutMapping("/update-customerTracking")
    public CustomerTracking updatecustomerTracking(@RequestBody CustomerTracking c) {
        CustomerTracking customerTracking = customerTrackingService.updateCustomerTracking(c);
        return customerTracking;
    }
    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalUsers() {
        int total = customerTrackingService.getTotalUsers();
        return ResponseEntity.ok(total);
    }

}
