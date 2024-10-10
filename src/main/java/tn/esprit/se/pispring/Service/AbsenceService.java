package tn.esprit.se.pispring.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.AbsenceRepository;
import tn.esprit.se.pispring.Repository.LeavRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Absence;
import tn.esprit.se.pispring.entities.Recruitment;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AbsenceService implements  IAbsenceService {

    private final UserRepository userRepository;
    private final AbsenceRepository absenceRepository;

    @Override
    public Absence addOrUpdateAbsence(Absence absence) {
        return absenceRepository.save(absence);
    }

    @Override
    public Absence retrieveAbsence(Long idAbsence) {
        return absenceRepository.findById(idAbsence).orElse(null);
    }

    @Override
    public List<Absence> retrieveAllAbsences() {
        return absenceRepository.findAll();
    }

    @Override
    public void removeAbsence(Long idAbsence) {
        absenceRepository.deleteById(idAbsence);
    }
}
