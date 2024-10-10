package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Repository.NoteRepository;
import tn.esprit.se.pispring.Service.IProjectService;
import tn.esprit.se.pispring.Service.ITaskService;
import tn.esprit.se.pispring.Service.INoteService;
import tn.esprit.se.pispring.Service.NoteService;
import tn.esprit.se.pispring.entities.Note;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/Note")
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {

    private static final Logger log = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;
    INoteService iNoteService;
    @Autowired
    private IProjectService projectService;

    @Autowired
    private ITaskService taskService;
    @Autowired
    NoteRepository noteRepository;



    @PostMapping("/create")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note savedNote = noteRepository.save(note);
        return new ResponseEntity<>(savedNote, HttpStatus.CREATED);
    }

    @GetMapping("/bestEmployer")
    public ResponseEntity<String> getBestEmployer() {
        try {
            noteService.classifyProjects();
            log.info("Récupération des meilleurs employeurs avec succès");
            return ResponseEntity.ok("Classification des projets terminée avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des meilleurs employeurs : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la classification des projets");
        }
    }
    @GetMapping("/nonbestEmployer")
    public ResponseEntity<Map<Long, List<User>>> getretardProjectsAndUsers() {
        try {
            Map<Long, List<User>> projectUsersMap = noteService.getretardProjectsAndUsers();

            log.info("Récupération des projets terminés avec succès");
            return ResponseEntity.ok(projectUsersMap);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des projets terminés: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/assignUsersToNote1")
    public ResponseEntity<String> assignUsersToNote1() {
        try {
            noteService.assignUsersToNote1();
            return ResponseEntity.ok("Affectation des utilisateurs à la note d'ID 1 réussie.");
        } catch (Exception e) {
            log.error("Erreur lors de l'affectation des utilisateurs à la note d'ID 1 : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite : " + e.getMessage());
        }
    }
    @PostMapping("/assignUsersToNote3")
    public ResponseEntity<String> assignUsersToNote3() {
        try {
            noteService.assignUsersToNote3();
            return new ResponseEntity<>("Affectation des utilisateurs aux projets en retard à la note d'ID 3 réussie.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur s'est produite lors de l'affectation des utilisateurs aux projets en retard.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/Perfermance1")
    public Map<User, Long> countUserOccurrencesForNote1() {
        return noteService.countUserOccurrencesForNote1();
    }
    @GetMapping("/Perfermance3")
    public Map<User, Long> countUserOccurrencesForNote3() {
        return noteService.countUserOccurrencesForNote1();
    }
    @GetMapping("/countNoteOccurrences")
    public ResponseEntity<Map<User, Map<String, Long>>> countNoteOccurrencesForEachUser() {
        Map<User, Map<String, Long>> userNoteOccurrencesMap = noteService.countNoteOccurrencesForEachUser();
        return new ResponseEntity<>(userNoteOccurrencesMap, HttpStatus.OK);
    }
}