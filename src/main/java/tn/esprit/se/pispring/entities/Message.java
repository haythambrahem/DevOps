package tn.esprit.se.pispring.entities;

import lombok.*;

import javax.persistence.*;

import java.util.Date;
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String senderEmail;
    private String time = new Date(System.currentTimeMillis()).toString();
    private String replymessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat;

}
