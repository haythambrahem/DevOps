package tn.esprit.se.pispring.Service.Product;

import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;

import java.util.List;

public interface IProductServices {
    List<Product> retrieveAllProducts();

    Product addProduct(Product product);

//    Product addProductWithBarcode(Product product);

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
    Product addProductWithBarcodeAndAssignProduction(Product product, Long productionId);

    Product updateProduct(Product product);



    Product getProductById(Long id);

    void deleteProduct(Long productId);

    List<Product> getAllProducts(String searchKey);

    List<Object[]> calculateAveragePriceByType();

    int numberOfLikes(Long productId);

    List<Product> top3MostLikedProducts();

    @Scheduled(fixedRate = 3600000) //1h
    void checkAndNotifyLowStock();

    Product assignProductionToProduct(Long productId, Production production);
}
