package com.team04.buy_gurus.review.repository;

import com.team04.buy_gurus.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //상품 ID로 리뷰 조회
    Page<Review> findByProductId(Long productId, Pageable pageable);
}
