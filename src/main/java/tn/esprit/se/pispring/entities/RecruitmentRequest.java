package tn.esprit.se.pispring.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecruitmentRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long requestId;
    private String firstName;
    private String lastName;
    private String postTitle;
    private String jobLocation;
    private int numberOfOpenings;
    private String description;
    private String requirements;
    private String hiringManager;
    private String recruiter;
    // Assuming CV is stored as a byte array in the database
    private byte[] cv;
}
