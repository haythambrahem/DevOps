package tn.esprit.se.pispring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.se.pispring.entities.Chat;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageDTO {

    private String senderEmail;
    private String replymessage;
    private int chatId;
}
