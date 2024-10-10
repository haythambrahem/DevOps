package tn.esprit.se.pispring.Service.Command;

import tn.esprit.se.pispring.entities.Cart;
import tn.esprit.se.pispring.entities.Command;
import tn.esprit.se.pispring.entities.CommandPayment;
import tn.esprit.se.pispring.entities.CommandStatus;

import java.util.Date;
import java.util.List;

public interface ICommandService {
    List<Command> findAll();

    Command add(Command command);

    void delete(Long commandId);

    Command update(Command command);

    Command  retrieveItem(Long idItem);

    Command assignCartToCommand(Long commandId, Long cartId);

    List<Command> findCommandsByStatus(CommandStatus status);

    List<Command> findCommandsByPaymentMethod(CommandPayment payment);

    List<Command> findCommandsBetweenDates(Date start, Date end);

    //calculer le chiffre d'affaires total pour un mois donné
    Double calculateMonthlySalesAmount(int year, int month);


  //  Command createCommandAndAssignCart(Long cartId);

    Command createCommandAndAssignCart(Long cartId, String userEmail);
}
