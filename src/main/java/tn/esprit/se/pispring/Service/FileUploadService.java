package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.se.pispring.Repository.RecruitmentRequestRepository;
import tn.esprit.se.pispring.entities.RecruitmentRequest;


import java.io.IOException;


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

            byte[] fileContent = file.getBytes();


            recruitmentRequest.setCv(fileContent);


            recruitmentRequestRepository.save(recruitmentRequest);


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
