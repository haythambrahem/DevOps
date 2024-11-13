package tn.esprit.se.pispring.Service.Product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.se.pispring.Repository.CartItemRepository;
import tn.esprit.se.pispring.Repository.LikeDislikeRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Repository.ProductionRepository;
import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServices implements IProductServices{
    private final CartItemRepository cartItemRepository;
    private final LikeDislikeRepository likeDislikeRepository;
   private final ProductRepository productRepository;
    private final BarcodeServiceImpl barcodeService;
private final ProductionRepository productionRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

@Override
public Product addProductWithBarcodeAndAssignProduction(Product product, Long productionId) {
    try {
        // Générer le code-barres pour la référence du produit
        ResponseEntity<byte[]> response = barcodeService.generateBarcode(product.getReference());

        if (response.getStatusCode() == HttpStatus.OK) {
            // Récupérer le code-barres sous forme d'octets
            byte[] barcodeBytes = response.getBody();

            // Associer le code-barres au produit
            product.setBarcode(Base64.getEncoder().encodeToString(barcodeBytes));

            // Récupérer la production à associer au produit
            Production production = productionRepository.findById(productionId)
                    .orElseThrow(() -> new EntityNotFoundException("Production not found for id: " + productionId));

            // Associer la production au produit
            product.setProduction(production);

            // Enregistrer le produit dans la base de données
            return productRepository.save(product);
        } else {
            log.error("Failed to generate barcode for product reference: {}", product.getReference());
            return null; // Ou lancez une exception appropriée si nécessaire
        }
    } catch (Exception e) {
        log.error("Error adding product with barcode and assigning production: {}", e.getMessage());
        return null; // Ou lancez une exception appropriée si nécessaire
    }
}

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
    @Override
    public List<Product> retrieveAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + id));
    }

  @Override
  @Transactional
  public void deleteProduct(Long productId) {
      boolean exists = productRepository.existsById(productId);
      if (!exists) {
          throw new EntityNotFoundException("Product not found for this id :: " + productId);
      }
      cartItemRepository.deleteByProductId(productId);
      likeDislikeRepository.deleteByProductProductId(productId);
      productRepository.deleteById(productId);
  }

    @Override
    public List<Product> getAllProducts(String searchKey){
        if (searchKey.equals("")) {
            return productRepository.findAll();
        }else{
            return productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchKey, searchKey);
        }
  }
////////////////fct avancee//////////////////
@Override

public List<Object[]> calculateAveragePriceByType() {
    return productRepository.calculateAveragePriceByType();
}
@Override
    public int numberOfLikes(Long productId){
        Product product=productRepository.findById(productId).get();
        int likes=0;
        for (LikeDislike e:product.getLikeDislikeProducts()){
            if(e.getProductRating().equals(ProductRating.LIKE)){
                likes++;
            }
        }
        return  likes;
    }
    @Override
    public List<Product> top3MostLikedProducts(){
        List<Product> top3MostLikedProducts= productRepository.findAll()
                .stream()
                .sorted((a,b)->this.numberOfLikes(b.getProductId())-this.numberOfLikes(a.getProductId()))
                .limit(3)
                .collect(Collectors.toList());
        return top3MostLikedProducts;
    }

    @Override
    @Scheduled(fixedRate = 3600000) //1h
    public void checkAndNotifyLowStock() {
        int lowStockThreshold = 10; // seuil de stock bas
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            if (product.getStock() < lowStockThreshold) {
                notifyLowStock(product);
            }
        }
    }

    private void notifyLowStock(Product product) {
        // Ici, implémentez la logique pour notifier du stock bas,
        // par exemple, envoyer un email, créer un événement, etc.
        log.info("Stock faible pour le produit {} - Quantité restante: {}", product.getTitle(), product.getStock());
    }

    @Override
    public Product assignProductionToProduct(Long productId, Production production) {
        // Récupérer le produit par son identifiant
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + productId));

        // Affecter la production au produit
        product.setProduction(production);

        // Enregistrer le produit mis à jour dans la base de données
        return productRepository.save(product);
    }
    @Override
    public String advancedProductAction(Long productId, String actionType) {
        // Retrieve the product by ID
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + productId));

        switch (actionType.toUpperCase()) {
            case "UPDATE_PRICE":
                // Logic to update the product price
                product.setPrice(product.getPrice() * 1.1f); // Example: increase price by 10%
                productRepository.save(product);
                return "Product price updated successfully.";

            case "CHECK_STOCK":
                // Logic to check stock and notify if low stock
                if (product.getStock() < 10) {
                    return "Product stock is low!";
                } else {
                    return "Product stock is sufficient.";
                }

            case "ASSIGN_PRODUCTION":

                // Logic to assign a production to a product
                Production production = productionRepository.findById(product.getProduction().getProductionId())
                        .orElseThrow(() -> new EntityNotFoundException("Production not found for this id"));
                product.setProduction(production);
                productRepository.save(product);
                return "Production assigned successfully.";

            default:
                return "Invalid action type provided.";
        }
    }
    public float calculateDiscountedPrice(float price, float discount) {
        if (discount < 0 || discount > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }
        return price * (1 - discount);
    }


    public float calculateDiscountedPrices(long productId, float discountRate) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            return product.getPrice() - (product.getPrice() * discountRate);
        }
        throw new NoSuchElementException("Product not found");
    }





}
