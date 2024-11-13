

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tn.esprit.se.pispring.Repository.*;
import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServicesTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductionRepository productionRepository;

    @Mock
    private LikeDislikeRepository likeDislikeRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BarcodeServiceImpl barcodeService;

    @InjectMocks
    private ProductServices productServices;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {
        Product product = new Product();
        product.setTitle("Test Product");
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productServices.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProductById_ProductExists() {
        Product product = new Product();
        product.setProductId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productServices.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getProductId());
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productServices.getProductById(1L));
    }

    @Test
    public void testDeleteProduct_ProductExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productServices.deleteProduct(1L);

        verify(cartItemRepository, times(1)).deleteByProductId(1L);
        verify(likeDislikeRepository, times(1)).deleteByProductProductId(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productServices.deleteProduct(1L));
    }

   /* @Test
    public void testAddProductWithBarcodeAndAssignProduction_Success() {
        Product product = new Product();
        product.setReference("12345");

        Production production = new Production();
        production.setProductionId(1L);

        byte[] barcodeBytes = "barcode".getBytes();
        ResponseEntity<byte[]> barcodeResponse = new ResponseEntity<>(barcodeBytes, HttpStatus.OK);

        when(barcodeService.generateBarcode("12345")).thenReturn(barcodeResponse);
        when(productionRepository.findById(1L)).thenReturn(Optional.of(production));
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productServices.addProductWithBarcodeAndAssignProduction(product, 1L);

        assertNotNull(savedProduct);
        assertEquals(Base64.getEncoder().encodeToString(barcodeBytes), savedProduct.getBarcode());
        assertEquals(production, savedProduct.getProduction());
    }*/

    @Test
    public void testNumberOfLikes() {
        Product product = new Product();
        LikeDislike like1 = new LikeDislike();
        like1.setProductRating(ProductRating.LIKE);

        LikeDislike like2 = new LikeDislike();
        like2.setProductRating(ProductRating.LIKE);

        product.setLikeDislikeProducts(Arrays.asList(like1, like2));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        int likes = productServices.numberOfLikes(1L);

        assertEquals(2, likes);
    }

   /* @Test
    public void testTop3MostLikedProducts() {
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setLikeDislikeProducts(Arrays.asList(new LikeDislike(ProductRating.LIKE)));

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setLikeDislikeProducts(Arrays.asList(new LikeDislike(ProductRating.LIKE), new LikeDislike(ProductRating.LIKE)));

        Product product3 = new Product();
        product3.setProductId(3L);
        product3.setLikeDislikeProducts(Collections.emptyList());

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2, product3));

        List<Product> topProducts = productServices.top3MostLikedProducts();

        assertEquals(3, topProducts.size());
        assertEquals(2L, topProducts.get(0).getProductId());
    }*/

    @Test
    public void testCheckAndNotifyLowStock() {
        Product product1 = new Product();
        product1.setTitle("Product 1");
        product1.setStock(5L);

        Product product2 = new Product();
        product2.setTitle("Product 2");
        product2.setStock(15L);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        productServices.checkAndNotifyLowStock();

        verify(productRepository, times(1)).findAll();
        // Verify logging or notification (if implemented)
    }
    @Test
    public void testAdvancedProductAction_UpdatePrice() {
        // Setup
        Product product = new Product();
        product.setProductId(1L);
        product.setPrice(100f);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        // Action
        String response = productServices.advancedProductAction(1L, "UPDATE_PRICE");

        // Assertions
        assertEquals("Product price updated successfully.", response);
        assertEquals(110f, product.getPrice(), 0.01); // Price increased by 10%
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testAdvancedProductAction_CheckStock_LowStock() {
        // Setup
        Product product = new Product();
        product.setProductId(1L);
        product.setStock(5L);  // Simulate low stock
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Action
        String response = productServices.advancedProductAction(1L, "CHECK_STOCK");

        // Assertions
        assertEquals("Product stock is low!", response);
        verify(productRepository, times(1)).findById(1L);
    }

}
