package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.utils.IContact;

import javax.mail.MessagingException;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/contact")
public class ContactContoller {
IContact iContact;
    @PostMapping("/send")
    public ResponseEntity<String> sendContactEmail(@RequestParam String name,
                                                   @RequestParam String email,
                                                   @RequestParam String subject,
                                                   @RequestParam String messageBody) {
        try {
            iContact.sendContactEmail(name, email, subject, messageBody);
            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException e) {
            // Log the exception
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }


}
