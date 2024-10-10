package tn.esprit.se.pispring.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.Stock.StockServiceImpl;
import tn.esprit.se.pispring.entities.MouvementStock;
import tn.esprit.se.pispring.entities.TypeMouvement;

import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin(origins = "http://localhost:4200")
public class MvtStockController {


   @Autowired StockServiceImpl stockService;

//    @PostMapping("/addMvt") //CREATED
//    public ResponseEntity<MouvementStock> addMouvement(@RequestBody MouvementStock mouvementStock) {
//        MouvementStock newMouvement = stockService.addMvt(mouvementStock);
//        return new ResponseEntity<>(newMouvement, HttpStatus.CREATED);
//    }

    @PostMapping("/addMvt/{productId}")
    public ResponseEntity<MouvementStock> addMvt(@RequestBody MouvementStock mouvementStock, @PathVariable Long productId) {
        MouvementStock newMvt = stockService.addMvt(mouvementStock, productId);
        return ResponseEntity.ok(newMvt);
    }

    @PutMapping("/updateMvt") //ok
    public MouvementStock updateMouvement(@RequestBody MouvementStock mouvementStock) {
        return stockService.updateMvt(mouvementStock);
    }
    @GetMapping("/allMvt") //ok
    public ResponseEntity<List<MouvementStock>> getAllMouvements() {
        List<MouvementStock> mouvements = stockService.getAllMouvements();
        return new ResponseEntity<>(mouvements, HttpStatus.OK);
    }

    @GetMapping("/retrieve/{mvtId}") //ok
    public ResponseEntity<MouvementStock> getMouvementById(@PathVariable("mvtId") Long mvtId) {
        MouvementStock mouvement = stockService.getMouvementById(mvtId);
        return new ResponseEntity<>(mouvement, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{mvtId}")
    public void deleteMouvement(@PathVariable Long mvtId) {
        stockService.deleteMouvement(mvtId);
   }
    @GetMapping("/movementsByType/{type}")
    public ResponseEntity<List<MouvementStock>> getMovementsByType(@PathVariable TypeMouvement type) {
        List<MouvementStock> movements = stockService.getMovementsByType(type);
        return new ResponseEntity<>(movements, HttpStatus.OK);
    }
    @GetMapping("/currentStock/{productId}") //Ok mais lezm association avec product
    public ResponseEntity<Long> getCurrentStock(@PathVariable Long productId) {
        Long currentStock = stockService.calculateCurrentStock(productId);
        if (currentStock != null) {
            return ResponseEntity.ok(currentStock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/averageStockValue")
    public ResponseEntity<Double> calculateAverageStockValue() {
        double averageStockValue = stockService.calculateAverageStockValue();
        return new ResponseEntity<>(averageStockValue, HttpStatus.OK);
    }
    @GetMapping("/averageStockValue/{productId}")
    public ResponseEntity<Double> calculateAverageStockValueForPeriods(@PathVariable Long productId) {
        double averageStockValue = stockService.calculateAverageStockValueForPeriods(productId);
        return new ResponseEntity<>(averageStockValue, HttpStatus.OK);
    }
    @GetMapping("/averageConsumption/{productId}") //OK
    public ResponseEntity<Double> getAverageConsumptionForProduct(@PathVariable("productId") Long productId) {
        double averageConsumption = stockService.calculateAverageConsumptionForProduct(productId);
        return new ResponseEntity<>(averageConsumption, HttpStatus.OK);
    }

}
