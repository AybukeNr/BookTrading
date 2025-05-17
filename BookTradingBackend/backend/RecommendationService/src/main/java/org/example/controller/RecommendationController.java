package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.example.constant.RestApiList.*;

@RestController
@RequestMapping(RECOMMENDATIONS)
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping(GET_RECOMMENDATIONS)
    public ResponseEntity<Set<String>> getRecommendations(@RequestBody String userId) {
        Set<String> recs = recommendationService.getRecommendations(userId);
        return new ResponseEntity<>(recs, HttpStatus.OK);
    }


    @GetMapping(GET_FILTERED_RECOMMENDATIONS)
    public ResponseEntity<Set<String>> getFilteredRecommendations(@RequestBody String userId) {
        Set<String> recs = recommendationService.getFilteredRecommendations(userId);
        return new ResponseEntity<>(recs, HttpStatus.OK);
    }

}
