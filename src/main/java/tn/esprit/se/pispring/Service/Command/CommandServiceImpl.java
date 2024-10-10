package tn.esprit.se.pispring.Service.Command;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.CartRepository;
import tn.esprit.se.pispring.Repository.CommandRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CommandServiceImpl implements ICommandService{
    private final UserRepository userRepository;
    private final CommandRepository commandRepository;
    private final CartRepository cartRepository;

    @Override
    public List<Command> findAll() {
        return commandRepository.findAll();
    }
    @Override
    public Command add(Command command) {
        return commandRepository.save(command);
    }

    @Override
    public void delete(Long commandId) {
        commandRepository.deleteById(commandId);

    }
    @Override
    public Command update(Command command) {
        return commandRepository.save(command);
    }

    @Override
    public Command  retrieveItem(Long idItem) {
        return commandRepository.findById(idItem).get();
    }
    @Override
    public Command assignCartToCommand(Long commandId, Long cartId) {
        Command command = commandRepository.findById(commandId)
                .orElseThrow(() -> new EntityNotFoundException("Command not found with id " + commandId));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id " + cartId));

        command.setCart(cart);
        return commandRepository.save(command);
    }
    @Override
    public List<Command> findCommandsByStatus(CommandStatus status) {
        return commandRepository.findByCommandStatus(status);
    }

    @Override
    public List<Command> findCommandsByPaymentMethod(CommandPayment payment) {
        return commandRepository.findByCommandPayment(payment);
    }

    @Override //ok
    public List<Command> findCommandsBetweenDates(Date start, Date end) {
        return commandRepository.findAllByDateCommandBetween(start, end);
    }

    //calculer le chiffre d'affaires total pour un mois donné
    @Override
    public Double calculateMonthlySalesAmount(int year, int month) {
        // Définir le début et la fin du mois
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        // Convertir LocalDate en Date
        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        // Récupérer les commandes payées dans l'intervalle de temps
        List<Command> commands = commandRepository.findByCommandStatusAndDateCommandBetween(CommandStatus.PAID, startDate, endDate);
        // Calculer le chiffre d'affaires total
        return commands.stream()
                .mapToDouble(command -> command.getCart().getCartAmount())
                .sum();
    }


    @Override

    public Command createCommandAndAssignCart(Long cartId, String userEmail) {
        // Récupérer le panier à partir de l'identifiant du panier
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));

        // Récupérer l'utilisateur à partir de son email
        User user = userRepository.findByEmail(userEmail);

        // Créer une nouvelle commande
        Command command = new Command();
        command.setDateCommand(new Date()); // Définir la date de la commande
        // Autres attributs de la commande

        // Associer l'utilisateur à la commande
        command.setUser(user);

        // Associer le panier à la commande
        cart.setCommand(command);

        // Enregistrer la commande en base de données
        return commandRepository.save(command);
    }


}
