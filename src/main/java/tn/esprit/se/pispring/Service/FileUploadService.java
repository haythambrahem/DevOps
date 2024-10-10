package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.se.pispring.Repository.RecruitmentRequestRepository;
import tn.esprit.se.pispring.entities.RecruitmentRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    private final RecruitmentRequestRepository recruitmentRequestRepository;

    public ResponseEntity<String> uploadFile(MultipartFile file, Long requestId) {
        // Check if recruitment request exists
        RecruitmentRequest recruitmentRequest = recruitmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruitment request not found"));

        try {
            // Convert MultipartFile to byte array
            byte[] fileContent = file.getBytes();

            // Set the byte array to the 'cv' field of the recruitment request
            recruitmentRequest.setCv(fileContent);

            // Save the recruitment request with the updated 'cv' field
            recruitmentRequestRepository.save(recruitmentRequest);

            // Construct file download URL if needed
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(requestId.toString())
                    .toUriString();

            return ResponseEntity.ok(fileDownloadUri);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file", ex);
        }
    }


}
