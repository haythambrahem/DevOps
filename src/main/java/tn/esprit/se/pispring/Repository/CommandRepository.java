package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.Command;
import tn.esprit.se.pispring.entities.CommandPayment;
import tn.esprit.se.pispring.entities.CommandStatus;

import java.util.Date;
import java.util.List;

public interface CommandRepository extends JpaRepository<Command, Long> {
    List<Command> findByCommandStatus(CommandStatus status);
    List<Command> findByCommandPayment(CommandPayment payment);
    List<Command> findAllByDateCommandBetween(Date start, Date end);
    List<Command> findByCommandStatusAndDateCommandBetween(CommandStatus status, Date startDate, Date endDate);

}