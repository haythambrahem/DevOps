package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import tn.esprit.se.pispring.Service.IRecruitmentRequestService;
import tn.esprit.se.pispring.entities.RecruitmentRequest;


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

