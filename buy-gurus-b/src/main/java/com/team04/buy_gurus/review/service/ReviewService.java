package com.team04.buy_gurus.review.service;

import com.team04.buy_gurus.exception.ex_review.ErrorCode;
import com.team04.buy_gurus.exception.ex_review.ReviewException;
import com.team04.buy_gurus.exception.ex_user.ex.UserNotFoundException;
import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import com.team04.buy_gurus.order.repository.OrderInfoRepository;
import com.team04.buy_gurus.order.repository.OrderRepository;
import com.team04.buy_gurus.product.aop.ProductNotFoundException;
import com.team04.buy_gurus.product.aop.ReviewNotFoundException;
import com.team04.buy_gurus.product.repository.ProductRepository;
import com.team04.buy_gurus.review.domain.Review;
import com.team04.buy_gurus.review.dto.ReviewRequest;
import com.team04.buy_gurus.review.dto.ReviewResponse;
import com.team04.buy_gurus.review.repository.ReviewRepository;
import com.team04.buy_gurus.user.entity.User;
import com.team04.buy_gurus.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         OrderRepository orderRepository,
                         OrderInfoRepository orderInfoRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository){
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.orderInfoRepository = orderInfoRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    //리뷰 생성
    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_AUTHORIZED, "사용자를 찾을 수 없습니다."));

        // 관리자 권한 체크
        boolean isAdmin = "ADMIN".equals(String.valueOf(user.getRole()));

        if (!isAdmin) {
            // 배송 완료된 주문 조회
            List<Order> shippedOrders = orderRepository.findAllByBuyerIdAndStatus(
                    request.getUserId(),
                    Order.Status.SHIPPED
            );

            if (shippedOrders.isEmpty()) {
                throw new ReviewException(
                        ErrorCode.DELIVERY_NOT_COMPLETED,
                        "배송이 완료된 상품에 대해서만 리뷰를 작성할 수 있습니다."
                );
            }

            // 주문한 상품 중 리뷰 작성이 가능한 상품 찾기
            boolean canReview = shippedOrders.stream()
                    .flatMap(order -> order.getOrderInfoList().stream())
                    .anyMatch(orderInfo ->
                            orderInfo.getProduct().getId().equals(request.getProductId())
                                    && !orderInfo.isReviewed()
                    );

            if (!canReview) {
                throw new ReviewException(
                        ErrorCode.PRODUCT_NOT_PURCHASED,
                        "구매하고 배송이 완료된 상품에 대해서만 리뷰를 작성할 수 있습니다."
                );
            }

            // OrderInfo의 isReviewed 상태 업데이트
            shippedOrders.stream()
                    .flatMap(order -> order.getOrderInfoList().stream())
                    .filter(orderInfo -> orderInfo.getProduct().getId().equals(request.getProductId()))
                    .findFirst()
                    .ifPresent(OrderInfo::setIsReviewed);
        }

        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .product(productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new ReviewException(ErrorCode.PRODUCT_NOT_FOUND, "상품을 찾을 수 없습니다.")))
                .user(user)
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToResponseDto(savedReview);
    }

    //특정 상품의 리뷰 조회
    @Transactional
    public Page<ReviewResponse> getReviewsByProductId(Long productId, Pageable pageable){
        return reviewRepository.findByProductId(productId, pageable)
                .map(this::mapToResponseDto);
    }

    //리뷰 수정
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("리뷰를 찾을 수 없습니다."));

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setModifiedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return mapToResponseDto(updatedReview);
    }

    //리뷰 삭제
    @Transactional
    public void deleteReview(Long id) {
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
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .userNickname(review.getUser() != null ? review.getUser().getNickname() : null)
                .build();
    }
}
