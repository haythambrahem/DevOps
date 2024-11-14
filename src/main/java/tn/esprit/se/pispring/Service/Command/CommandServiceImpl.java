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

    @Override
    public List<Command> findCommandsBetweenDates(Date start, Date end) {
        return commandRepository.findAllByDateCommandBetween(start, end);
    }


    @Override
    public Double calculateMonthlySalesAmount(int year, int month) {

        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Date startDate = Date.from(startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Command> commands = commandRepository.findByCommandStatusAndDateCommandBetween(CommandStatus.PAID, startDate, endDate);

        return commands.stream()
                .mapToDouble(command -> command.getCart().getCartAmount())
                .sum();
    }





}
