package tn.esprit.se.pispring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private Long quantity;
    public ProductDTO(Long productId, String title, String description, float price, ProductType productType) {
    }

    public ProductDTO(String title, Long quantity) {
        this.title = title;
        this.quantity = quantity;
    }
}
