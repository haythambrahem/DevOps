package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Resources {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "resourceId")
    private Long resourceId;
    private String resourceName;
    private String resourcesDescription;
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    private Float resourcesCost;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
    @ManyToMany( cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Task> tasks;

}
