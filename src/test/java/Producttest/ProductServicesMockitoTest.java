package Producttest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.se.pispring.Repository.ProductRepository;

import tn.esprit.se.pispring.entities.Product;


import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServicesMockitoTest {
    private AutoCloseable closeable;
    @Mock
    private ProductRepository productRepository;


    @InjectMocks
    private tn.esprit.se.pispring.Service.Product.ProductServices productServices;

    @BeforeEach
    public void setup() {

        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
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
    @Test
    void testAdvancedProductAction_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                productServices.advancedProductAction(1L, "UPDATE_PRICE")
        );
    }








}