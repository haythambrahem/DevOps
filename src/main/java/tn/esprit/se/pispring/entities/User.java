package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String firstName ;
    private String lastName ;
    private String email;
    private String password;
    private Integer telephone;
    private Integer salaire;
    private Boolean connected = false;
    private boolean deleted = false;
    private boolean enabled= true;
    private String profilePhoto;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns  = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

//    @ManyToMany(fetch = EAGER)
//    @JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns  = @JoinColumn(name = "permission_id"))
//    private List<Permission> permissions = new ArrayList<>();




    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Recruitment> Recruitments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Performance> Performances;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Payroll> Payrolls;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Prime> Primes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Contribution> Contributions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Leav> Leaves;

    //@ManyToMany(mappedBy="users", cascade = CascadeType.ALL)
   // private Set<Portfolio> portfolios;

    @ManyToMany(mappedBy="users", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Task> tasks;
    @ManyToMany(mappedBy="users", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Note> notes;



    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    @JsonIgnore
    private Set<NoteUser> NoteUsers;
    //@OneToOne(mappedBy="portfolio")
    //private Consultant consultant;

    @ManyToOne
    Portfolio portfolio;

    @OneToOne
    private CustomerTracking customertracking;

//DHOUUUUUUUUUUUUUUUUUUHA
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Set<Command> Commands;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Review> reviews;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    List<LikeDislike> likeDislikeProductList;

    public void setPermissions(List<Permission> permissions) {
    }

    public void setProfilePhotoUrl(String fileName) {
    }
}
