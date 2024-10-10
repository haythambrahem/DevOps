package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long potfolio_id;
    private String potfolio_name;
    private String client_name;
    @Temporal(TemporalType.DATE)
    private Date creation_date; // condition date
    private String potfolio_manager;
    private String potfolio_description;
    private Integer  nbr_client ; // supprimer deccriment // ajout incrementer
    @Enumerated(EnumType.STRING)
    private PortfolioDomain domain;
    @OneToOne(mappedBy="portfolio")
    private Consultant consultant;
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private List<Date> meeting_dates;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="portfolio",fetch = FetchType.EAGER)
    private Set<User> Users; // kol metsir affcetation nbr client increment auto



}
