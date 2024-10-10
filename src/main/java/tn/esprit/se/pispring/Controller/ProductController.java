package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.Service.ProductionService.ProductionServiceImpl;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    ProductServices productServices;
    ProductRepository productRepository;
    ProductionServiceImpl productionService;
    @PostMapping("/addProduct")
    public Product addProduct(@RequestBody Product product) {

        return productRepository.save(product);
    }


    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestBody Product product) {

        return productServices.updateProduct(product);
    }
    @PutMapping("/updateProduct/{id}")
    public Product updateRegistration(@PathVariable Long id, @RequestBody Product product) {
        product.setProductId(id);
        Product product1 = productServices.updateProduct(product);
        return product1;
    }

    @GetMapping("/getallproducts")
    List<Product> getAllProducts(@RequestParam(defaultValue = "") String searchKey) {
        return productServices.getAllProducts(searchKey);
    }

    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productServices.deleteProduct(productId);
            // Return a success response indicating the product was deleted
            return ResponseEntity.ok().body("Product successfully deleted");
        } catch (Exception e) {
            // Log the exception for debugging purposes
            System.err.println("Error deleting product: " + e.getMessage());
            // Return an error response indicating the deletion failed
            return ResponseEntity.internalServerError().body("Error deleting product: " + e.getMessage());
        }
    }

    //    @GetMapping("/getproductdetails/{productId}")
//    public Product getProductDetailsById(@PathVariable("productId")Long productId){
//        return productRepository.findById(productId).get();
//    }
    @GetMapping("/getProductById/{id}")
    public Product getProductById(@PathVariable("id") Long productId) {
        return productServices.getProductById(productId);
    }

    @GetMapping("/average-price-by-type")
    public List<Object[]> getAveragePriceByType() {
        return productServices.calculateAveragePriceByType();
    }

    @GetMapping("/top3")
    public List<Product> top3MostLikedProducts() {
        return productServices.top3MostLikedProducts();
    }


//    @PostMapping("/products/add-with-barcode")
//    public ResponseEntity<Product> addProductWithBarcode(@RequestBody Product product) {
//        Product addedProduct = productServices.addProductWithBarcode(product);
//        if (addedProduct != null) {
//            return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("/add-with-barcode/{productionId}")
    public ResponseEntity<?> addProductWithBarcodeAndAssignProduction(
            @RequestBody Product product,
            @PathVariable("productionId") Long productionId) {
        Product addedProduct = productServices.addProductWithBarcodeAndAssignProduction(product, productionId);
        if (addedProduct != null) {
            return ResponseEntity.ok(addedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    ////////////////////////////////AFFECTATION

    }
