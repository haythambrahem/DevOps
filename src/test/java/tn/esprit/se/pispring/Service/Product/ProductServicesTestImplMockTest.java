package tn.esprit.se.pispring.Service.Product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.se.pispring.ProductServices;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Repository.ProductionRepository;
import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class ProductServicesTestImplMockTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BarcodeServiceImpl barcodeService;

    @Mock
    private ProductionRepository productionRepository;

    @InjectMocks
    private ProductServices productServices;

    @Test
    @Order(1)
    void testAddProductWithBarcodeAndAssignProduction_Success() throws Exception {
        log.info("Test - Success Case");

        // Arrange
        Product product = new Product();
        product.setReference("PROD123");

        Production production = new Production();
        production.setProductionId(1L);

        byte[] barcodeBytes = "sample-barcode".getBytes();
        ResponseEntity<byte[]> barcodeResponse = new ResponseEntity<>(barcodeBytes, HttpStatus.OK);

        when(barcodeService.generateBarcode(product.getReference())).thenReturn(barcodeResponse);
        when(productionRepository.findById(1L)).thenReturn(Optional.of(production));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product result = productServices.addProductWithBarcodeAndAssignProduction(product, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(Base64.getEncoder().encodeToString(barcodeBytes), result.getBarcode());
        assertEquals(production, result.getProduction());

        verify(barcodeService).generateBarcode(product.getReference());
        verify(productionRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    @Order(2)
    void testAddProductWithBarcodeAndAssignProduction_ProductionNotFound() {
        log.info("Test - Production Not Found Case");

        // Arrange
        Product product = new Product();
        product.setReference("PROD123");

        byte[] barcodeBytes = "sample-barcode".getBytes();
        ResponseEntity<byte[]> barcodeResponse = new ResponseEntity<>(barcodeBytes, HttpStatus.OK);

        when(barcodeService.generateBarcode(product.getReference())).thenReturn(barcodeResponse);
        when(productionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productServices.addProductWithBarcodeAndAssignProduction(product, 1L);
        });

        assertEquals("Production not found for id: 1", exception.getMessage());

        verify(barcodeService).generateBarcode(product.getReference());
        verify(productionRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @Order(3)
    void testAddProductWithBarcodeAndAssignProduction_BarcodeGenerationFailed() throws Exception {
        log.info("Test - Barcode Generation Failed Case");

        // Arrange
        Product product = new Product();
        product.setReference("PROD123");

        // Simulate a failure in barcode generation
        ResponseEntity<byte[]> barcodeResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(barcodeService.generateBarcode(product.getReference())).thenReturn(barcodeResponse);

        // Act
        Product result = productServices.addProductWithBarcodeAndAssignProduction(product, 1L);

        // Assert
        assertNull(result);

        verify(barcodeService).generateBarcode(product.getReference());
        verify(productionRepository, never()).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }
}
