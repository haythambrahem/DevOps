package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.se.pispring.Service.FileUploadService;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/file")
public class FileUploadController {
    private final FileUploadService fileUploadService;


    @PostMapping("/uploadFile/{requestId}")
    public ResponseEntity<String> uploadFile(@PathVariable Long requestId,
                                             @RequestParam("file") MultipartFile file) {
        try {
            return fileUploadService.uploadFile(file, requestId);
        } catch (Exception ex) {
            log.error("Error uploading file", ex);
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
