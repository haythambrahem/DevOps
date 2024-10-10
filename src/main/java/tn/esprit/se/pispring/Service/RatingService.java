package tn.esprit.se.pispring.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.RatingRepository;
import tn.esprit.se.pispring.entities.LRating;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService implements IRatingService {
    private final RatingRepository ratingRepository;

    @Override
    public LRating addOrUpdateRating(LRating R) {
        return ratingRepository.save(R);
    }

}
