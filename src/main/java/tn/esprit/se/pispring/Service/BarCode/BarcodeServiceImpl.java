package tn.esprit.se.pispring.Service.BarCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class BarcodeServiceImpl implements IBarCode{

    @Override
    public ResponseEntity<byte[]> generateBarcode(String barcodeValue) {
        try {

            int width = 300;
            int height = 100;


            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    barcodeValue, BarcodeFormat.CODE_128, width, height);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            javax.imageio.ImageIO.write(bufferedImage, "png", baos);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(baos.size());

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
