package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
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
public class Cart {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long cartId;
    @Temporal(TemporalType.DATE)
    private Date dateLastItem;
    private Float cartAmount;
    private Integer numberOfItems;


    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @OneToOne
    private Command command;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return items;
    }

}
