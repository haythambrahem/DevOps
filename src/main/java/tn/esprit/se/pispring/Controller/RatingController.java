package tn.esprit.se.pispring.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.IRatingService;
import tn.esprit.se.pispring.entities.LRating;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/rate")
public class RatingController {
    private final IRatingService ratingService;

    @PostMapping("/ratings")
    LRating addRating(@RequestBody LRating rating)
    {
        return ratingService.addOrUpdateRating(rating);
    }
}

