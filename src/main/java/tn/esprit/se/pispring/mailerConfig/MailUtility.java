package tn.esprit.se.pispring.mailerConfig;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public record MailUtility(JavaMailSender mailSender) {
    public void sendMail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("haythambrahem@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
