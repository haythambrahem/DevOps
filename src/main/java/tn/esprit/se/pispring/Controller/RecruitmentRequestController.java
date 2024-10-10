package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.se.pispring.Service.IRecruitmentRequestService;
import tn.esprit.se.pispring.entities.RecruitmentRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recruitmentrequest")
public class RecruitmentRequestController {
    private final IRecruitmentRequestService recruitmentRequestService;

    @PostMapping("/createrequest")
    RecruitmentRequest createRecruitmentRequest(@RequestBody RecruitmentRequest recruitmentRequest)
    {
        return recruitmentRequestService.addRecruitmentrequest(recruitmentRequest);
    }

}

