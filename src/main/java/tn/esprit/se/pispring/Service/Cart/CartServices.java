package tn.esprit.se.pispring.Service.Cart;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.CartItemRepository;
import tn.esprit.se.pispring.Repository.CartRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Service.Product.ProductServices;
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


    @Autowired
    private ProductServices productService;


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
@Override
public Cart addCart(Cart cart) {
    return cartRepository.save(cart);
}
    @Override
    public void addProductToCart(Long cartId, Long productId, Long quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseGet(() -> {
                    // Si aucun panier n'existe avec l'ID donné, créez un nouveau panier
                    Cart newCart = new Cart();
                    // Initialisez les autres valeurs du panier si nécessaire
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        // Recherche d'un CartItem existant pour ce produit dans le panier
        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Si un CartItem existe déjà, mettez à jour la quantité
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity); // Augmente la quantité existante
            cartItemRepository.save(cartItem); // Sauvegarde les modifications
        } else {
            // Si aucun CartItem n'existe pour ce produit, créez-en un nouveau
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cart.getItems().add(newCartItem); // Ajoutez le nouveau CartItem au panier
            cartItemRepository.save(newCartItem); // Sauvegarde le nouveau CartItem
        }

        cart.setDateLastItem(new Date()); // Mise à jour de la date du dernier article ajouté
        cartRepository.save(cart); // Sauvegarde les modifications du panier
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

        // Find the CartItem by id
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Update the quantity
        cartItem.setQuantity(newQuantity);

        // Recalculate subtotals
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
        cartItemDTO.setProduct(cartItem.getProduct()); // Définir le produit associé
        return cartItemDTO;
    }
//    @Override
//    @Transactional
//    public boolean updateCartItemCount(Long cartId, int itemCount) {
//        Cart cart = cartRepository.findById(cartId).orElse(null);
//        if (cart != null) {
//            cart.setNumberOfItems(itemCount);
//            cartRepository.save(cart);
//            return true;
//        }
//        return false;
//    }
}



