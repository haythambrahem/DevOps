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
import tn.esprit.se.pispring.entities.ProductType;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServicesMockitoTest {

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

    ///// only with mockito
    @Test
    public void testCalculateDiscountedPrice_WithMocksOnly() {
        // Arrange
        Product mockProduct = Product.builder()
                .productId(1L)
                .reference("Ref123")
                .title("Product Name")
                .image("imageUrl")
                .description("Description")
                .stock(100L)
                .price(150.0f)
                .TVA(20L)
                .productType(Product.ProductType.ELECTRONICS)
                .createdAt(new Date())
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // Act: Apply a discount (10%)
        float discountedPrice = productServices.calculateDiscountedPrices(1L, 0.1f); // Use float for the discount rate

        // Assert
        assertEquals(135.0f, discountedPrice, 0.01f); // Compare using float values

        // Verify the interaction with the mock
        verify(productRepository).findById(1L);
    }






}
