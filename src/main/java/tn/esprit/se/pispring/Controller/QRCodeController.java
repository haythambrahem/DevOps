package tn.esprit.se.pispring.Controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QRCodeController {

    @GetMapping("/generateQR")
    public void generateQRCode(@RequestParam String message, HttpServletResponse response) throws IOException, WriterException {
        response.setContentType("image/png");

        // Set QR code parameters
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Generate QR code
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(message, BarcodeFormat.QR_CODE, 200, 200, hints);

        // Convert bit matrix to image
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", response.getOutputStream());
    }
}
