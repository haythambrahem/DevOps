package Producttest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.se.pispring.Repository.*;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServicesTest {
    private AutoCloseable closeable;
    @Mock
    private ProductRepository productRepository;



    @Mock
    private LikeDislikeRepository likeDislikeRepository;

    @Mock
    private CartItemRepository cartItemRepository;



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
    void testAddProduct() {
        Product product = new Product();
        product.setTitle("Test Product");
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productServices.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getTitle());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetProductById_ProductExists() {
        Product product = new Product();
        product.setProductId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productServices.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getProductId());
    }

    @Test
    void testGetProductById_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productServices.getProductById(1L));
    }

    @Test
    void testDeleteProduct_ProductExists() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productServices.deleteProduct(1L);

        verify(cartItemRepository, times(1)).deleteByProductId(1L);
        verify(likeDislikeRepository, times(1)).deleteByProductProductId(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productServices.deleteProduct(1L));
    }



    @Test
    void testNumberOfLikes() {
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



    @Test
    void testCheckAndNotifyLowStock() {
        Product product1 = new Product();
        product1.setTitle("Product 1");
        product1.setStock(5L);

        Product product2 = new Product();
        product2.setTitle("Product 2");
        product2.setStock(15L);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        productServices.checkAndNotifyLowStock();

        verify(productRepository, times(1)).findAll();

    }
    @Test
    void testAdvancedProductAction_UpdatePrice() {

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
    void testAdvancedProductAction_CheckStock_LowStock() {

        Product product = new Product();
        product.setProductId(1L);
        product.setStock(5L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        String response = productServices.advancedProductAction(1L, "CHECK_STOCK");


        assertEquals("Product stock is low!", response);
        verify(productRepository, times(1)).findById(1L);
    }

}