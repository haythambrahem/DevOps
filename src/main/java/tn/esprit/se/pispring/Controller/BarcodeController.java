package tn.esprit.se.pispring.Controller;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;





@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

    @Autowired
    private BarcodeServiceImpl barcodeService;

    @PostMapping("/generateBarcode")
    public ResponseEntity<byte[]> generateBarcode(@RequestBody String barcodeValue) {
        return barcodeService.generateBarcode(barcodeValue);
    }


}
