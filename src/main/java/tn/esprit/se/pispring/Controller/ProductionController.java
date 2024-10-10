package tn.esprit.se.pispring.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ProductionService.ProductionServiceImpl;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.ProductionStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productions")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductionController {


@Autowired ProductionServiceImpl productionService;

//CRUUUUUUUUUUUUUUUUUUUUD


    @PostMapping("/add") //OK
    public Production addProduction(@RequestBody Production production) {
        return productionService.addProduction(production);
    }
    @GetMapping("/all") //OK
    public List<Production> getAllProductions() {
        return productionService.getAllProductions();
    }

    @GetMapping("/{productionId}") //OK
    public Production getProductionById(@PathVariable Long productionId) {
        return productionService.getProductionById(productionId);
    }
    @PutMapping("/update") //OK
    public Production updateProduction(@RequestBody Production production) {
        return productionService.updateProduction(production);
    }

    @DeleteMapping("/delete/{productionId}") //Ok
    public void deleteProduction(@PathVariable Long productionId) {
        productionService.deleteProduction(productionId);
    }


    @PutMapping("/status/{productionId}/{newStatus}")
    public ResponseEntity<?> updateProductionStatus(@PathVariable Long productionId, @PathVariable ProductionStatus newStatus) {
        Optional<Production> updatedProduction = productionService.updateProductionStatus(productionId, newStatus);
        if (updatedProduction.isPresent()) {
            return ResponseEntity.ok(updatedProduction.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //LOGIQUE   METIERR
    @GetMapping("/totalProductionTimedayss/{id}")
    public int getTotalProductionTime(@PathVariable("id") Long productionId) {

        Production production = productionService.getProductionById(productionId);
        return productionService.calculateTotalProductionTime(production);
    }

    @GetMapping("/production/yield-rate/{id}") //OK
    public double getProductionYieldRate(@PathVariable("id") long id) {
        Production production = productionService.getProductionById(id);
        if (production == null) {
            return -1;
        }
        return productionService.calculateYieldRate(production);
    }
    @GetMapping("/production/machine-maintenance-cost") //OK
    public double getTotalMachineMaintenanceCost() {
        List<Production> productions = productionService.getAllProductions();
        if (productions.isEmpty()) {
            return -1;
        }
        return productionService.calculateTotalMachineMaintenanceCost(productions);
    }
    @GetMapping("/productions/most-defective") //OK
    public List<Production> getProductionsWithMostDefectiveProducts() {
        return productionService.findProductionsWithMostDefectiveProducts();
    }
    @PostMapping("/production/total-cost")
    public double calculateTotalProductionCost(@RequestBody Production production) {
        return productionService.calculateTotalProductionCost(production);
    }


    @GetMapping("/overallEquipmentEffectiveness/{productionId}")
    public ResponseEntity<Double> getOverallEquipmentEffectiveness(@PathVariable Long productionId) {
        Production production = productionService.getProductionById(productionId);
        double oee = productionService.calculateOverallEquipmentEffectiveness(production);
        return ResponseEntity.ok(oee);
    }

    @GetMapping("/calculateQualité/{productionId}") //mrigla
    public ResponseEntity<Double> calculateQuality(@PathVariable Long productionId) {
        Production production = productionService.getProductionById(productionId);
        double quality = productionService.calculateQuality(production);
        return ResponseEntity.ok(quality);
    }
    @GetMapping("/calculateAvailability/{productionId}") //OK
    public ResponseEntity<Double> calculateAvailability(@PathVariable Long productionId) {
        Production production = productionService.getProductionById(productionId);
        double availability = productionService.calculateAvailability(production);
        return ResponseEntity.ok(availability);
    }
    @GetMapping("/performance/{productionId}")
    public double calculatePerformance(@PathVariable Long productionId) {
        Production production = productionService.getProductionById(productionId);
        return productionService.calculatePerformance(production);
    }
    @GetMapping("/totalProductionTimeDays/{productionId}")
    public ResponseEntity<Long> calculateTotalProductionTimeDays(@PathVariable Long productionId) {
        Production production = productionService.getProductionById(productionId);
        long totalProductionTimeMilliseconds = productionService.calculateTotalProductionTimeDays(production);
        return ResponseEntity.ok(totalProductionTimeMilliseconds);
    }


    @GetMapping("/oee/{productionId}")
    public ResponseEntity<Double> calculateOEE(@PathVariable Long productionId) {
        Production production = productionService.getProductionById(productionId);
        double oee = productionService.calculateOverallEquipmentEffectiveness(production);
        return ResponseEntity.ok(oee);
    }

//    @PostMapping("/calculate-stats")
//    public void calculateProductionStats(@RequestBody Production production) {
//        productionService.calculateProductionStats(production);
//    }



//
//    @GetMapping("/overall-equipment-effectiveness")
//    public double calculateOverallEquipmentEffectiveness(@RequestBody Production production) {
//        return productionService.calculateOverallEquipmentEffectiveness(production);
//    }
//
//    @GetMapping("/average-cycle-time")
//    public long calculateAverageCycleTime(@RequestBody List<Production> productions) {
//        return productionService.calculateAverageCycleTime(productions);
//    }
//
//    @GetMapping("/average-availability")
//    public double calculateAverageAvailability(@RequestBody List<Production> productions) {
//        return productionService.calculateAverageAvailability(productions);
//    }


//
//    @GetMapping("/total-maintenance-cost")
//    public double calculateTotalMachineMaintenanceCost(@RequestBody List<Production> productions) {
//        return productionService.calculateTotalMachineMaintenanceCost(productions);
//    }
}
