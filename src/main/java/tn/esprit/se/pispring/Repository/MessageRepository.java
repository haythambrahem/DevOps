package tn.esprit.se.pispring.Repository;

import tn.esprit.se.pispring.DTO.MessageDTO;
import tn.esprit.se.pispring.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}