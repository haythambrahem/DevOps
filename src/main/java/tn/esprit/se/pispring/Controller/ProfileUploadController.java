package tn.esprit.se.pispring.Controller;


import com.itextpdf.text.Element;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import tn.esprit.se.pispring.Service.UserService;


@RestController
@RequiredArgsConstructor
public class ProfileUploadController {
    private static final String UPLOAD_DIR = "C:/Users/hayth/Downloads";
    private final UserService userService;

    @PostMapping("/uploadProfilePDF")
    public ResponseEntity<String> uploadProfilePDF(@RequestParam("profilePDF") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please select a file to upload.");
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }
    @GetMapping(value = "/download/profile", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadProfile(@RequestHeader(name = "Authorization") String token) throws IOException {
        // Generate the PDF document
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add((Element) userService.getCurrentUserInfos(token));
            // Add more profile details here as paragraphs
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Return the PDF file as a response
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }
}
