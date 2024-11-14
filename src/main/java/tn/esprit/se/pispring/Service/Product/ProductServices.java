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
import tn.esprit.se.pispring.Service.Product.IProductServices;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;



@Service
@Slf4j
@AllArgsConstructor
public class ProductServices implements IProductServices {
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

        ResponseEntity<byte[]> response = barcodeService.generateBarcode(product.getReference());

        if (response.getStatusCode() == HttpStatus.OK) {

            byte[] barcodeBytes = response.getBody();


            product.setBarcode(Base64.getEncoder().encodeToString(barcodeBytes));


            Production production = productionRepository.findById(productionId)
                    .orElseThrow(() -> new EntityNotFoundException("Production not found for id: " + productionId));


            product.setProduction(production);


            return productRepository.save(product);
        } else {
            log.error("Failed to generate barcode for product reference: {}", product.getReference());
            return null;
        }
    } catch (Exception e) {
        log.error("Error adding product with barcode and assigning production: {}", e.getMessage());
        return null;
    }
}

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NoProduct " + id));
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
        if (searchKey.isEmpty()) {
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
    public int numberOfLikes(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            int likes = 0;

            for (LikeDislike e : product.getLikeDislikeProducts()) {
                if (e.getProductRating().equals(ProductRating.LIKE)) {
                    likes++;
                }
            }
            return likes;
        } else {

            throw new NoSuchElementException("Product not found with id: " + productId);
        }
    }

    @Override
    public List<Product> top3MostLikedProducts() {
        return productRepository.findAll()
                .stream()
                .sorted((a, b) -> this.numberOfLikes(b.getProductId()) - this.numberOfLikes(a.getProductId()))
                .limit(3)
                .toList();
    }


    @Override
    @Scheduled(fixedRate = 3600000)
    public void checkAndNotifyLowStock() {
        int lowStockThreshold = 10;
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            if (product.getStock() < lowStockThreshold) {
                notifyLowStock(product);
            }
        }
    }

    private void notifyLowStock(Product product) {

        log.info("Stock faible pour le produit {} - Quantité restante: {}", product.getTitle(), product.getStock());
    }


    @Override
    public String advancedProductAction(Long productId, String actionType) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found for this id :: " + productId));

        switch (actionType.toUpperCase()) {
            case "UPDATE_PRICE":

                product.setPrice(product.getPrice() * 1.1f);
                productRepository.save(product);
                return "Product price updated successfully.";

            case "CHECK_STOCK":

                if (product.getStock() < 10) {
                    return "Product stock is low!";
                } else {
                    return "Product stock is sufficient.";
                }

            case "ASSIGN_PRODUCTION":


                Production production = productionRepository.findById(product.getProduction().getProductionId())
                        .orElseThrow(() -> new EntityNotFoundException("Production not found for this id"));
                product.setProduction(production);
                productRepository.save(product);
                return "Production assigned successfully.";

            default:
                return "Invalid action type provided.";
        }
    }
    public static float calculateDiscountedPrice(float price, float discount) {
        if (discount < 0 || discount > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }


        if (discount == 0) {
            return price;
        } else if (discount < 0.1) {

            return price * 0.95f;
        } else if (discount < 0.25) {

            return price * (1 - discount);
        } else {

            return switch ((int) (discount * 100)) {
                case 25 -> price * 0.70f;
                case 50 -> price * 0.50f;
                case 75 -> price * 0.25f;
                default -> price * (1 - discount);
            };

        }
    }









}
