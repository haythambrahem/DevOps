package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Repository.CommandRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.Service.Command.CommandServiceImpl;
import tn.esprit.se.pispring.entities.Cart;
import tn.esprit.se.pispring.entities.Command;
import tn.esprit.se.pispring.entities.CommandPayment;
import tn.esprit.se.pispring.entities.CommandStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/command")
@CrossOrigin(origins = "http://localhost:4200")
public class CommandController {
    CommandServiceImpl commandService ;
    private final CommandRepository commandRepository;
    private final UserRepository userRepository;

    @GetMapping("/commandes")
    public List<Command> retrieveCommandList(){
        return commandService.findAll();
    }

    @GetMapping("/retrive_commandes/{commandId}")
    public Command retrieveCommand(@PathVariable("commandId") Long commandId){
        return commandService.retrieveItem(commandId);
    }

    @PostMapping("/addCommande")
    public Command addCommand(@RequestBody Command command){
        return commandService.add(command);
    }
    ///////////////////////////

    @PutMapping("/updateCommande")
    public Command updateCommand(@RequestBody Command command){
        return commandService.update(command);
    }
    //OK
    @DeleteMapping("/deleteCommande/{commandId}")
    public void deleteCommand(@PathVariable("commandId") Long commandId){
        commandService.delete(commandId);
    }
    @PutMapping("/{commandId}/assignCart/{cartId}") // ca marche pas
    public ResponseEntity<Command> assignCartToCommand(@PathVariable Long commandId, @PathVariable Long cartId) {
        Command updatedCommand = commandService.assignCartToCommand(commandId, cartId);
        return ResponseEntity.ok(updatedCommand);
    }

    @GetMapping("/sales/{date}")
    public ResponseEntity<Double> getMonthlySalesAmount(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        Double salesAmount = commandService.calculateMonthlySalesAmount(year, month);
        return ResponseEntity.ok(salesAmount);
    }
    //OK
    @GetMapping("/commands/between-dates/{start}/{end}")
    public ResponseEntity<List<Command>> findCommandsBetweenDates(
            @PathVariable("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start,
            @PathVariable("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end) {
        List<Command> commands = commandService.findCommandsBetweenDates(start, end);
        return ResponseEntity.ok(commands);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<Command>> findCommandsByStatus(@PathVariable CommandStatus status) {
        List<Command> commands = commandService.findCommandsByStatus(status);
        return ResponseEntity.ok(commands);
    }

    @GetMapping("/by-payment/{payment}")
    public ResponseEntity<List<Command>> findCommandsByPaymentMethod(@PathVariable CommandPayment payment) {
        List<Command> commands = commandService.findCommandsByPaymentMethod(payment);
        return ResponseEntity.ok(commands);
    }
//    @PostMapping("/commands/{cartId}/{userEmail}")
//    public ResponseEntity<Command> createCommandAndAssignCart(@PathVariable Long cartId, @PathVariable String userEmail) {
//        Command command = commandService.createCommandAndAssignCart(cartId, userEmail);
//        return new ResponseEntity<>(command, HttpStatus.CREATED);
//    }
}
