import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.se.pispring.Repository.CartItemRepository;
import tn.esprit.se.pispring.Repository.LikeDislikeRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Repository.ProductionRepository;
import tn.esprit.se.pispring.Service.BarCode.BarcodeServiceImpl;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServicesJUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private LikeDislikeRepository likeDislikeRepository;

    @Mock
    private BarcodeServiceImpl barcodeService;

    @Mock
    private ProductionRepository productionRepository;

    @InjectMocks
    private ProductServices productServices;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

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




    @Test
    public void testAddProduct() {
        // Setup
        Product product = new Product();
        product.setTitle("Test Product");

        // Mock the save method of the productRepository
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Action
        Product savedProduct = productServices.addProduct(product);

        // Assertions
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetProductById_ProductExists() {
        // Arrange: Create a mock Product
        Product product = new Product();
        product.setProductId(1L);

        // Mock the repository to return the product when queried by ID
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act: Call the getProductById method
        Product foundProduct = productServices.getProductById(1L);

        // Assert: Ensure the product was found and the ID matches
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getProductId());
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        assertThrows(EntityNotFoundException.class, () -> productServices.getProductById(1L));
    }



    @Test
    public void testDeleteProduct_ProductNotFound() {
        assertThrows(EntityNotFoundException.class, () -> productServices.deleteProduct(1L));
    }

    @Test
    public void testNumberOfLikes() {
        Product product = new Product();
        LikeDislike like1 = new LikeDislike();
        like1.setProductRating(ProductRating.LIKE);
        LikeDislike like2 = new LikeDislike();
        like2.setProductRating(ProductRating.LIKE);
        product.setLikeDislikeProducts(Arrays.asList(like1, like2));

        // Mock the repository to return the product when queried by ID
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Call the method
        int likes = productServices.numberOfLikes(1L);

        // Verify the result
        assertEquals(2, likes);
    }

    /////only junit
    @Test
    public void testCalculateDiscountedPrice() {
        float result = productServices.calculateDiscountedPrice(100f, 0.2f);
        assertEquals(80f, result, 0.01);
    }

    ///only junit

    @Test
    public void testCalculateDiscountedPrice_InvalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> {
            productServices.calculateDiscountedPrice(100f, 1.5f);
        });
    }





}
