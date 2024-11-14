package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Repository.CartRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Service.Cart.CartServices;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Cart;
import tn.esprit.se.pispring.entities.CartItem;
import tn.esprit.se.pispring.entities.Product;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:4200")

public class CartController {

    @Autowired
    private CartServices cartService;
    private ProductServices productServices;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;


@PostMapping("/addCart")
public Cart addCart(@RequestBody Cart cart) {

    return cartRepository.save(cart);
}

    @PostMapping("/cartItem/{cartId}/{productId}/{quantity}")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Long quantity) {
        cartService.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/calculate-subtotals/{cartId}")  //OK
    public ResponseEntity<Float> calculateSubtotals(@PathVariable("cartId") Long cartId) {
        float total = cartService.calculateTotalPrice(cartId);
        return ResponseEntity.ok(total);
    }

    @DeleteMapping("/removeItem/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok().build();
    }



    @PutMapping("/update-item-quantity/{cartId}/{cartItemId}/{newQuantity}")   //OKKK
    public ResponseEntity<?> updateItemQuantity(@PathVariable("cartId") Long cartId, @PathVariable("cartItemId") Long  cartItemId, @PathVariable("newQuantity") Long newQuantity) {
        Cart cart = cartService.updateCartItemQuantity(cartId, cartItemId, newQuantity);
        return ResponseEntity.ok().body("Quantity updated successfully");
    }




    @DeleteMapping ("/clear-cart/{cartId}")
    public ResponseEntity<Cart> clearCart(@PathVariable("cartId") Long cartId) {
        Cart cart = cartService.clearCart(cartId);
        return ResponseEntity.ok(cart);
    }



    @GetMapping("/getCartItemsWithProducts/{cartId}")  //OKKK
    public List<CartItem> getCartItemsWithProducts(@PathVariable Long cartId) {
        return cartService.getCartItemsWithProducts(cartId);
    }
    @GetMapping("/products/{productId}")
    public Product getProductById(@PathVariable Long productId) {
        return productServices.getProductById(productId);
    }


}
