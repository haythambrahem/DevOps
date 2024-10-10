package tn.esprit.se.pispring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private EPermission permissionName;
}
