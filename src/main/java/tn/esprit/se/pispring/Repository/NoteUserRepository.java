package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.se.pispring.entities.Note;
import tn.esprit.se.pispring.entities.NoteUser;
import tn.esprit.se.pispring.entities.User;

import java.util.List;

public interface NoteUserRepository extends JpaRepository<NoteUser, Long> {
    boolean existsByProjectId(Long projectId);
    @Query("SELECT nu.user, COUNT(nu.user) " +
            "FROM NoteUser nu " +
            "WHERE nu.note.id = 1 " +
            "GROUP BY nu.user")
    List<Object[]> countUserOccurrencesForNote1();

    @Query("SELECT nu.user, COUNT(nu.user) " +
            "FROM NoteUser nu " +
            "WHERE nu.note.id = 3 " +
            "GROUP BY nu.user")
    List<Object[]> countUserOccurrencesForNote3();

    @Query("SELECT DISTINCT nu.user FROM NoteUser nu")
    List<User> findAllUser();

    Long countByUserIdAndNoteId(Long userId, Long noteId);

}
