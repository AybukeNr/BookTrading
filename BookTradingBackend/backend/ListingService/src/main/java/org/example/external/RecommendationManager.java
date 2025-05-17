package org.example.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

import static org.example.constants.RestApiList.GET_FILTERED_RECOMMENDATIONS;

@FeignClient(url = "http://localhost:8084/api/v1/recommendations",name = "recsManager")
public interface RecommendationManager {

    @GetMapping(GET_FILTERED_RECOMMENDATIONS)
    public ResponseEntity<Set<String>> getFilteredRecommendations(@RequestBody String userId);
}
