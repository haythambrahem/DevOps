package tn.esprit.se.pispring.utils;

import javax.mail.MessagingException;

public interface IContact {
    void sendContactEmail(String name, String email, String subject, String messageBody) throws MessagingException;
}
