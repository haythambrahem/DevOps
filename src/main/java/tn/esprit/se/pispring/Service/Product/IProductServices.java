package tn.esprit.se.pispring.Service.Product;

import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.se.pispring.entities.Product;


import java.util.List;

public interface IProductServices {


    Product addProduct(Product product);


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


     String advancedProductAction(Long productId, String actionType);
}
