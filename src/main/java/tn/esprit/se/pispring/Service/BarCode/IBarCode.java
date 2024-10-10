package tn.esprit.se.pispring.Service.BarCode;

import org.springframework.http.ResponseEntity;
import tn.esprit.se.pispring.entities.Product;

public interface IBarCode {

    ResponseEntity<byte[]> generateBarcode(String barcodeValue);
}
