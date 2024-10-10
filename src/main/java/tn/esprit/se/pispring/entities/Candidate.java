package tn.esprit.se.pispring.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCandidate;

    private String firstNameCand;
    private String lastNameCand;
    private String emailCand;
    private String skillsCand;
    private int experienceCand;
    private String education;
    private String postTitleC;
    @Lob
    private byte[] cv; // Byte array to store the file content

    private String cvFileName; // Name of the CV file
    @ManyToOne
    @JoinColumn(name = "offerId", referencedColumnName = "offerId")
    private Recruitment recruitment;

}
