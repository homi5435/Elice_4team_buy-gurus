package com.team04.buy_gurus.review.controller;

import com.team04.buy_gurus.review.dto.ReviewRequest;
import com.team04.buy_gurus.review.dto.ReviewResponse;
import com.team04.buy_gurus.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/user/product/{productId}/review")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long productId,
                                                       @RequestBody ReviewRequest request,
                                                       Principal principal) {
        // 인증된 사용자 ID 설정
        request.setUserId(Long.valueOf(principal.getName()));
        request.setProductId(productId); // 상품 ID 설정
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}/review")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<ReviewResponse> responseDtoList = reviewService.getReviewsByProductId(productId, pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @RequestBody ReviewRequest requestDto) {
        ReviewResponse responseDto = reviewService.updateReview(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
