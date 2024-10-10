package com.team04.buy_gurus.review.service;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.repository.OrderInfoRepository;
import com.team04.buy_gurus.order.repository.OrderRepository;
import com.team04.buy_gurus.review.domain.Review;
import com.team04.buy_gurus.review.dto.ReviewRequest;
import com.team04.buy_gurus.review.dto.ReviewResponse;
import com.team04.buy_gurus.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderInfoRepository orderInfoRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         OrderRepository orderRepository,
                         OrderInfoRepository orderInfoRepository){
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.orderInfoRepository = orderInfoRepository;
    }

    //리뷰 생성
    public ReviewResponse createReview(ReviewRequest request){

        Order order = orderRepository.findByUserIdAndStatus(
                request.getUserId(), "SHIPPED")
                .orElseThrow(() -> new RuntimeException("해당 상품을 구매하고 배송이 완료된 소비자만 리뷰를 등록할 수 있습니다."));

        OrderInfo orderInfo = orderInfoRepository.findByProductId(
                request.getProductId())
                .orElseThrow(() -> new RuntimeException("해당 상품을 구매하고 배송이 완료된 소비자만 리뷰를 등록할 수 있습니다."));

        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .productId(request.getProductId())
                .userId(request.getUserId())
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToResponseDto(savedReview);
    }

    //특정 상품의 리뷰 조회
    public Page<ReviewResponse> getReviewsByProductId(Long productId, Pageable pageable){
        return reviewRepository.findByProductId(productId, pageable)
                .map(this::mapToResponseDto);
    }

    //리뷰 수정
    public ReviewResponse updateReview(Long id, ReviewRequest request){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setModifiedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return mapToResponseDto(updatedReview);
    }

    //리뷰 삭제
    public void deleteReview(Long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제할 리뷰가 존재하지 않습니다."));
        review.setIsDeleted(true);
        reviewRepository.save(review);
    }

    // Review to ReviewResponse 변환
    private ReviewResponse mapToResponseDto(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .isDeleted(review.getIsDeleted())
                .productId(review.getProductId())
                .userId(review.getUserId())
                .userNickname(review.getUser() != null ? review.getUser().getNickname() : null)
                .build();
    }
}
