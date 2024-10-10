package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.IAbsenceService;
import tn.esprit.se.pispring.entities.Absence;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8089")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/absence")
public class AbsenceController {
    private final IAbsenceService absenceService;

    @PostMapping("/addAbsence")
    Absence addAbsence(@RequestBody Absence absence)
    {
        return absenceService.addOrUpdateAbsence(absence);
    }
    @PutMapping("/updateAbsence")
    Absence updateAbsence(@RequestBody Absence absence)
    {
        return absenceService.addOrUpdateAbsence(absence);
    }
    @GetMapping("/get/{idAbsence}")
    Absence getAbsence(@PathVariable("idAbsence") Long idAbsence)
    {
        return absenceService.retrieveAbsence(idAbsence);
    }
    @GetMapping("/allAbsences")
    List<Absence> getAllAbsences()
    {
        return absenceService.retrieveAllAbsences();
    }
    @DeleteMapping("/delete/{idAbsence}")
    void deleteAbsence(@PathVariable("idAbsence") Long idAbsence)
    {
        absenceService.removeAbsence(idAbsence);
    }
}
