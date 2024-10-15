package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    Optional<OrderInfo> findByProductId(Long productId);

    @Query("SELECT oi FROM OrderInfo oi JOIN oi.order o WHERE oi.product.id = :productId AND oi.isReviewed = false AND oi.isDeleted = false")
    List<OrderInfo> findOrderInfoByProductId(Long productId);
}
