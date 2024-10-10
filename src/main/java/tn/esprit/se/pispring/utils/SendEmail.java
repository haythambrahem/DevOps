package tn.esprit.se.pispring.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
@Service
@AllArgsConstructor
@Slf4j
public class SendEmail {

    private JavaMailSender emailSender;

    public void sendEmailWithAttachment(ByteArrayInputStream pdfBytes, String address, String subject,String body) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        // Enable multipart mode
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(address);
        helper.setSubject(subject);
        helper.setText(body);
        // Attach PDF
        helper.addAttachment(subject+".pdf", new ByteArrayResource(pdfBytes.readAllBytes()));
        emailSender.send(message);
    }


}

