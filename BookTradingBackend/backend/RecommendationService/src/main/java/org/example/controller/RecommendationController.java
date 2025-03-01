package org.example.controller;


import lombok.RequiredArgsConstructor;
import org.example.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.example.constant.RestApiList.*;

@RestController
@RequestMapping(RECOMMENDATIONS)
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping(GET_RECOMMENDATIONS)
    public ResponseEntity<Set<String>> getRecommendations(@RequestBody Set<String> userItems) {
        Set<String> recs = recommendationService.getRecommendations(userItems);
        return new ResponseEntity<>(recs, HttpStatus.OK);
    }


    @PostMapping(GET_FILTERED_RECOMMENDATIONS)
    public ResponseEntity<Set<String>> getFilteredRecommendations(@RequestBody Set<String> userItems) {
        Set<String> recs = recommendationService.getFilteredRecommendations(userItems);
        return new ResponseEntity<>(recs, HttpStatus.OK);
    }

}
