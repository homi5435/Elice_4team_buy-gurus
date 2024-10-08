package com.team04.buy_gurus.review.controller;

import com.team04.buy_gurus.review.dto.ReviewRequest;
import com.team04.buy_gurus.review.dto.ReviewResponse;
import com.team04.buy_gurus.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/user/product/{productId}")
    public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByProductId(@PathVariable Long productId, Pageable pageable) {
        Page<ReviewResponse> responseDtoList = reviewService.getReviewsByProductId(productId, pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @RequestBody ReviewRequest requestDto) {
        ReviewResponse responseDto = reviewService.updateReview(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
