package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.id = :sellerId")
    Page<Order> findAllByUser(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.sellerInfo si WHERE si.id = :sellerId")
    Page<Order> findAllBySellerInfo(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.user u JOIN o.sellerInfo s WHERE o.id = :orderId AND (u.id = :userId OR s.id = :userId)")
    Optional<Order> findByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
}
