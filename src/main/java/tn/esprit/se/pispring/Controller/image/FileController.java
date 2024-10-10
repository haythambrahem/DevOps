package tn.esprit.se.pispring.Controller.image;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.se.pispring.Service.imageService.FileStorageService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FileController {
    private FileStorageService fileStorageService;
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok(fileDownloadUri);
    }
}
