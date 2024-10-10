package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.se.pispring.Service.FileUpload;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cloud")
@CrossOrigin("*")
public class PProfileUploadController {
    @Autowired
    private final FileUpload fileUpload;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("image") MultipartFile multipartFile) {
        try {
            String imageURL = fileUpload.uploadFile(multipartFile);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("imageURL", imageURL);
            return ResponseEntity.ok(jsonResponse.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }
}
