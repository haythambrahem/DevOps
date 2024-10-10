package tn.esprit.se.pispring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @JsonIgnore // kif kif
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
    @JoinColumn(name = "productId", referencedColumnName = "productId")

    private Product product;
    private Long quantity;

}
