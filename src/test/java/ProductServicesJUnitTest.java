import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.se.pispring.Repository.ProductRepository;

import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Product;

import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServicesJUnitTest {


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
        assertEquals(110f, product.getPrice(), 0.01); // Price increased by 10%
        verify(productRepository, times(1)).save(product);
    }



    @Test
    public void testAdvancedProductAction_CheckStock_LowStock() {

        Product product = new Product();
        product.setProductId(1L);
        product.setStock(5L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        String response = productServices.advancedProductAction(1L, "CHECK_STOCK");


        assertEquals("Product stock is low!", response);
        verify(productRepository, times(1)).findById(1L);
    }




    @Test
    public void testAddProduct() {

        Product product = new Product();
        product.setTitle("Test Product");


        when(productRepository.save(any(Product.class))).thenReturn(product);


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


        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        int likes = productServices.numberOfLikes(1L);


        assertEquals(2, likes);
    }


    @Test
    public void testCalculateDiscountedPrice() {
        float result = productServices.calculateDiscountedPrice(100f, 0.2f);
        assertEquals(80f, result, 0.01);
    }




    @Test
    public void testCalculateDiscountedPrice_InvalidDiscount() {
        assertThrows(IllegalArgumentException.class, () -> productServices.calculateDiscountedPrice(100f, 1.5f));
    }





}
