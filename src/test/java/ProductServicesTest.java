import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.se.pispring.Repository.CartItemRepository;
import tn.esprit.se.pispring.Repository.LikeDislikeRepository;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Repository.ProductionRepository;
import tn.esprit.se.pispring.Service.Product.ProductServices;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ProductServicesTest {

    @InjectMocks
    private ProductServices productServices;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private LikeDislikeRepository likeDislikeRepository;

    @Mock
    private ProductionRepository productionRepository;

    @Test
    public void testCalculateAveragePriceByType() {

        List<Object[]> mockResponse = Arrays.asList(
                new Object[]{"Type1", 100.0},
                new Object[]{"Type2", 200.0}
        );
        Mockito.when(productRepository.calculateAveragePriceByType()).thenReturn(mockResponse);


        List<Object[]> result = productServices.calculateAveragePriceByType();


        assertEquals(2, result.size());
        assertEquals("Type1", result.get(0)[0]);
        assertEquals(100.0, result.get(0)[1]);
        assertEquals("Type2", result.get(1)[0]);
        assertEquals(200.0, result.get(1)[1]);
    }

    @Test
    public void testNumberOfLikes() {

        Product mockProduct = new Product();
        LikeDislike like1 = new LikeDislike();
        like1.setProductRating(ProductRating.LIKE);
        LikeDislike like2 = new LikeDislike();
        like2.setProductRating(ProductRating.LIKE);
        LikeDislike dislike = new LikeDislike();
        dislike.setProductRating(ProductRating.DISLIKE);


        mockProduct.setLikeDislikeProducts(Arrays.asList(like1, like2, dislike));


        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(mockProduct));


        int likes = productServices.numberOfLikes(1L);

        assertEquals(2, likes);
    }
}
