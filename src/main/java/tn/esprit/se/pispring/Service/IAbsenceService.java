package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Absence;
import tn.esprit.se.pispring.entities.Recruitment;

import java.util.List;

public interface IAbsenceService {
    Absence addOrUpdateAbsence(Absence absence);

    Absence retrieveAbsence(Long idAbsence);

    List<Absence> retrieveAllAbsences();

    void removeAbsence(Long idAbsence);
}
