package tn.esprit.se.pispring.Service.BarCode;

import org.springframework.http.ResponseEntity;


public interface IBarCode {

    ResponseEntity<byte[]> generateBarcode(String barcodeValue);
}
