package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.RecruitmentRequestRepository;
import tn.esprit.se.pispring.entities.RecruitmentRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruitmentRequestService implements IRecruitmentRequestService {
    private final RecruitmentRequestRepository recruitmentRequestRepository;


    @Override
    public RecruitmentRequest addRecruitmentrequest(RecruitmentRequest recruitmentRequest) {
        return recruitmentRequestRepository.save(recruitmentRequest);
    }
}

