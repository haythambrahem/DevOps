package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Candidate;

import java.util.List;

public interface ICandidateService {
    Candidate addOrUpdateCandidate(Candidate candidate);

    void removeCandidate(Long id);

    Candidate retrieveCandidate(Long id);

    List<Candidate> retrieveAllCandidates();

    void assignRecruitmentToCandidate(Long candidateId, Long offerId);

    int countCandidatesByRecruitmentOfferId(Long offerId);
}
