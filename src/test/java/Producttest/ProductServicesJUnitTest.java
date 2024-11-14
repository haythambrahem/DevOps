package Producttest;

import org.junit.jupiter.api.Test;
import tn.esprit.se.pispring.Service.Product.ProductServices;

import static org.junit.jupiter.api.Assertions.*;

class ProductServicesJUnitTest {

    @Test
    void testCalculateDiscountedPrice_NoDiscount() {
        float result = ProductServices.calculateDiscountedPrice(100f, 0f);
        assertEquals(100f, result, 0.01);
    }

    @Test
    void testCalculateDiscountedPrice_SmallDiscount() {
        float result = ProductServices.calculateDiscountedPrice(100f, 0.05f);
        assertEquals(95f, result, 0.01);  // 5% discount
    }

    @Test
    void testCalculateDiscountedPrice_ModerateDisacount() {
        float result = ProductServices.calculateDiscountedPrice(100f, 0.2f);
        assertEquals(80f, result, 0.01);
    }

    @Test
    void testCalculateDiscountedPrice_LargeDiscount() {
        float result = ProductServices.calculateDiscountedPrice(100f, 0.25f);
        assertEquals(70f, result, 0.01);
    }

    @Test
    void testCalculateDiscountedPrice_HalfDiscount() {
        float result = ProductServices.calculateDiscountedPrice(100f, 0.5f);
        assertEquals(50f, result, 0.01);
    }

    @Test
    void testCalculateDiscountedPrice_HighDiscount() {
        float result = ProductServices.calculateDiscountedPrice(100f, 0.75f);
        assertEquals(25f, result, 0.01);
    }

    @Test
    void testCalculateDiscountedPrice_InvalidDiscount_TooHigh() {
        assertThrows(IllegalArgumentException.class, () ->
                ProductServices.calculateDiscountedPrice(100f, 1.5f)
        );
    }

    @Test
    void testCalculateDiscountedPrice_InvalidDiscount_Negative() {
        assertThrows(IllegalArgumentException.class, () ->
                ProductServices.calculateDiscountedPrice(100f, -0.1f)
        );
    }
}