package tn.esprit.se.pispring.Controller;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import org.hibernate.type.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



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
//    @PostMapping("/generateBarcode")
//    public ResponseEntity<byte[]> generateBarcode(@RequestBody String barcodeValue) {
//        try {
//            // Set barcode width and height
//            int width = 300;
//            int height = 100;
//
//            // Encode the barcode
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(
//                    barcodeValue, BarcodeFormat.CODE_128, width, height);
//
//            // Convert bitMatrix to BufferedImage
//            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//            // Convert BufferedImage to byte array
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            javax.imageio.ImageIO.write(bufferedImage, "png", baos);
//
//            // Set response headers
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_PNG);
//            headers.setContentLength(baos.size());
//
//            // Return image bytes with appropriate headers
//            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
//        } catch (WriterException | IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
