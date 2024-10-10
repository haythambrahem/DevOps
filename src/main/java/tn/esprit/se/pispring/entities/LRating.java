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
public class LRating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idrate;

    private int value;
    private String contributor;
}
