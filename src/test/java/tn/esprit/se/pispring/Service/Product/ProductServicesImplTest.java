//package tn.esprit.se.pispring.Service.Product;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import tn.esprit.se.pispring.ProductServices;
//import tn.esprit.se.pispring.Repository.ProductRepository;
//import tn.esprit.se.pispring.Repository.ProductionRepository;
//import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;
//import tn.esprit.se.pispring.entities.Product;
//import tn.esprit.se.pispring.entities.Production;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.Base64;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Slf4j
//public class ProductServicesImplTest {
//    @Autowired
//    private ProductServices productServices;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductionRepository productionRepository;
//
//    @Autowired
//    private BarcodeServiceImpl barcodeService;
//
//    @Test
//    @Order(1)
//    void testAddProductWithBarcodeAndAssignProduction_Success() throws Exception {
//        log.info("JUnit Test - Success Case");
//
//        // Arrange
//        Product product = new Product();
//        product.setReference("PROD123");
//        product.setBarcode(Base64.getEncoder().encodeToString("sample-barcode".getBytes())); // Directly set expected barcode
//
//        Production production = new Production();
//        production.setProductionId(1L);
//        productionRepository.save(production); // Ensure production is saved first
//
//        // Act
//        Product result = productServices.addProductWithBarcodeAndAssignProduction(product, production.getProductionId());
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(product.getBarcode(), result.getBarcode());
//        assertEquals(production.getProductionId(), result.getProduction().getProductionId());
//    }
//
//    @Test
//    @Order(2)
//    void testAddProductWithBarcodeAndAssignProduction_ProductionNotFound() {
//        log.info("JUnit Test - Production Not Found Case");
//
//        // Arrange
//        Product product = new Product();
//        product.setReference("PROD123");
//
//        // Act & Assert
//        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
//            productServices.addProductWithBarcodeAndAssignProduction(product, 99L);
//        });
//
//        assertEquals("Production not found for id: 99", exception.getMessage());
//    }
//
//    @Test
//    @Order(3)
//    void testAddProductWithBarcodeAndAssignProduction_BarcodeGenerationFailed() {
//        log.info("JUnit Test - Barcode Generation Failed Case");
//
//        // Arrange
//        Product product = new Product();
//        product.setReference("PROD123");
//
//        // Simulating barcode generation failure
//        String invalidBarcode = null; // Simulating a failure by using null
//
//        // Here we need to save the product with a simulated failure
//        product.setBarcode(invalidBarcode);
//        productionRepository.save(new Production()); // Ensure a production exists
//
//        // Act
//        Product result = productServices.addProductWithBarcodeAndAssignProduction(product, 1L);
//
//        // Assert
//        assertNull(result); // Assuming null is returned when barcode generation fails
//    }
//}
