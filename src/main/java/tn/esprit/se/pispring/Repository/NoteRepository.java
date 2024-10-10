package tn.esprit.se.pispring.Repository;

import org.apache.commons.codec.language.bm.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
