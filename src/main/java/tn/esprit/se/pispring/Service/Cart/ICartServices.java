package tn.esprit.se.pispring.Service.Cart;

import tn.esprit.se.pispring.entities.Cart;
import tn.esprit.se.pispring.entities.CartItem;
import tn.esprit.se.pispring.entities.Product;

import java.util.List;

public interface ICartServices {

//    Cart addToCart(Long cartId, Long productId, Long quantity);

    //    @Override  //temchi
    //    public void addProductToCart(Long cartId, Long productId, Long quantity) {
    //    Cart cart = cartRepository.findById(cartId)
    //            .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));
    //    Product product = productRepository.findById(productId)
    //            .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
    //    CartItem cartItem = new CartItem();
    //    cartItem.setCart(cart);
    //    cartItem.setProduct(product);
    //    cartItem.setQuantity(quantity);
    //    cart.setDateLastItem(new Date());
    //    cartRepository.save(cart);
    //    cartItemRepository.save(cartItem);
    //}
    Cart addCart(Cart cart);

    //    @Override
    //    public Cart addToCart(Long cartId, Long productId, Long quantity) {
    //        Cart cart = cartRepository.findById(cartId)
    //                .orElseThrow(() -> new RuntimeException("Cart not found"));
    //
    //        Product product = productService.getProductById(productId);
    //
    //        CartItem cartItem = new CartItem();
    //        cartItem.setCart(cart);
    //        cartItem.setProduct(product);
    //        cartItem.setQuantity(quantity);
    //
    //        cart.getItems().add(cartItem);
    //
    //        cartRepository.save(cart);
    //
    //        return cart;
    //    }
    void addProductToCart(Long cartId, Long productId, Long quantity);

    //Cart calculateSubtotals(Long cartId);

    float calculateTotalPrice(Long cartId);

    //Cart removeFromCart(Long cartId, Long cartItemId);

    void removeFromCart(Long id);

    Cart updateCartItemQuantity(Long cartId, Long cartItemId, Long newQuantity);

    Cart clearCart(Long cartId);

    //@Override
    //    public List<CartItem> getCartItemsWithProducts(Long cartId) {
    //        Optional<Cart> optionalCart = cartRepository.findById(cartId);
    //
    //        if (optionalCart.isPresent()) {
    //            Cart cart = optionalCart.get();
    //            List<CartItem> cartItems = cart.getCartItems();
    //
    //            cartItems.forEach(cartItem -> cartItem.getProduct().getTitle());
    //
    //            return cartItems;
    //        } else {
    //
    //            return Collections.emptyList();
    //        }
    //    }
    Product getProductById(Long productId);

    List<CartItem> getCartItemsWithProducts(Long cartId);

//    @Transactional
//    boolean updateCartItemCount(Long cartId, int itemCount);

//    List<CartItem> getCartItemsWithProducts(Long cartId);
}
