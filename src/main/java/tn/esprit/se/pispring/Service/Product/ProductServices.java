package tn.esprit.se.pispring.Service.Product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

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
//    @Override
//    public Product addProductWithBarcode(Product product) {
//        try {
//            // Générer le code-barres pour la référence du produit
//            ResponseEntity<byte[]> response = barcodeService.generateBarcode(product.getReference());
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                // Récupérer le code-barres sous forme d'octets
//                byte[] barcodeBytes = response.getBody();
//
//                // Associer le code-barres au produit
//                product.setBarcode(Base64.getEncoder().encodeToString(barcodeBytes));
//
//                // Enregistrer le produit dans la base de données
//                return productRepository.save(product);
//            } else {
//                log.error("Failed to generate barcode for product reference: {}", product.getReference());
//                return null; // Ou lancez une exception appropriée si nécessaire
//            }
//        } catch (Exception e) {
//            log.error("Error adding product with barcode: {}", e.getMessage());
//            return null; // Ou lancez une exception appropriée si nécessaire
//        }
//    }
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
}
