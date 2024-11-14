package tn.esprit.se.pispring.Service.Cart;

import tn.esprit.se.pispring.entities.Cart;
import tn.esprit.se.pispring.entities.CartItem;
import tn.esprit.se.pispring.entities.Product;

import java.util.List;

public interface ICartServices {





    void addProductToCart(Long cartId, Long productId, Long quantity);



    float calculateTotalPrice(Long cartId);



    void removeFromCart(Long id);

    Cart updateCartItemQuantity(Long cartId, Long cartItemId, Long newQuantity);

    Cart clearCart(Long cartId);


    Product getProductById(Long productId);

    List<CartItem> getCartItemsWithProducts(Long cartId);

}
