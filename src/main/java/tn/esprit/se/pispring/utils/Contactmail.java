package tn.esprit.se.pispring.utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.spi.InitialContextFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
@Service
@Slf4j
@AllArgsConstructor
public class Contactmail implements IContact {
    private JavaMailSender emailSender;
    public void sendContactEmail(String name, String email, String subject, String messageBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo("imenbennourddin@gmail.com"); // Remplacez par votre adresse e-mail
        helper.setSubject("Nouveau message de contact: " + subject);

        String emailBody = "Nom: " + name + "\n" +
                "Email: " + email + "\n" +
                "Sujet: " + subject + "\n" +
                "Message: " + messageBody;

        helper.setText(emailBody);

        emailSender.send(message);
    }
}
