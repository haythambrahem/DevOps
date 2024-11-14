import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.se.pispring.Repository.ProductRepository;

import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Product;


import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServicesMockitoTest {
    private AutoCloseable closeable;
    @Mock
    private ProductRepository productRepository;


    @InjectMocks
    private ProductServices productServices;

    @BeforeEach
    public void setup() {

        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testAdvancedProductAction_UpdatePrice() {

        Product product = new Product();
        product.setProductId(1L);
        product.setPrice(100f);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);


        String response = productServices.advancedProductAction(1L, "UPDATE_PRICE");


        assertEquals("Product price updated successfully.", response);
        assertEquals(110f, product.getPrice(), 0.01);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testAdvancedProductAction_CheckStock_LowStock() {

        Product product = new Product();
        product.setProductId(1L);
        product.setStock(5L);  // Simulate low stock
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        String response = productServices.advancedProductAction(1L, "CHECK_STOCK");


        assertEquals("Product stock is low!", response);
        verify(productRepository, times(1)).findById(1L);
    }


    @Test
    public void testCalculateDiscountedPrice_WithMocksOnly() {

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


        float discountedPrice = productServices.calculateDiscountedPrices(1L, 0.1f);


        assertEquals(135.0f, discountedPrice, 0.01f);


        verify(productRepository).findById(1L);
    }






}
