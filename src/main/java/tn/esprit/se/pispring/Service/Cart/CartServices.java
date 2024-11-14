package tn.esprit.se.pispring.Service.Cart;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.CartItemRepository;
import tn.esprit.se.pispring.Repository.CartRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;

import tn.esprit.se.pispring.entities.Cart;
import tn.esprit.se.pispring.entities.CartItem;
import tn.esprit.se.pispring.entities.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CartServices implements ICartServices {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final CartRepository cartRepository;






    @Override
    public void addProductToCart(Long cartId, Long productId, Long quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));


        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {

            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {

            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cart.getItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }

        cart.setDateLastItem(new Date());
        cartRepository.save(cart);
    }


    @Override
    public float calculateTotalPrice(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        float total = 0;

        for (CartItem item : cart.getItems()) {
            float itemTotal = item.getProduct().getPrice() * item.getQuantity();
            total += itemTotal;
        }

        return total;
    }

    @Override
    public void removeFromCart(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public Cart updateCartItemQuantity(Long cartId, Long cartItemId, Long newQuantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));


        cartItem.setQuantity(newQuantity);


        calculateTotalPrice(cartId);

        return cartRepository.save(cart);
    }

    @Override
    public Cart clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
        cart.setCartAmount(0F);

        return cartRepository.save(cart);
    }

    @Override
public Product getProductById(Long productId) {
    Product product = productRepository.findById(productId).get();
    return mapProductToProductDTO(product);
}
    private Product mapProductToProductDTO(Product product) {
        Product productDTO = new Product();
        productDTO.setProductId(product.getProductId());
        productDTO.setTitle(product.getTitle());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    @Override
    public List<CartItem> getCartItemsWithProducts(Long cartId) {
        Cart cart = cartRepository.findById(cartId).get();
        List<CartItem> cartItems = cart.getItems();
        List<CartItem> cartItemDTOs = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product productDTO = getProductById(cartItem.getProduct().getProductId());

            CartItem cartItemDTO = mapCartItemToCartItemDTO(cartItem);
            cartItemDTO.setProduct(productDTO);
            cartItemDTOs.add(cartItemDTO);
        }
        return cartItemDTOs;
    }


    private CartItem mapCartItemToCartItemDTO(CartItem cartItem) {
        CartItem cartItemDTO = new CartItem();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setProduct(cartItem.getProduct());
        return cartItemDTO;
    }

}



