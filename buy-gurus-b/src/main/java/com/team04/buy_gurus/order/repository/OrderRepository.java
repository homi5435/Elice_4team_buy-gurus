package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.Order;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.buyer.id = :sellerId AND o.isDeleted = false")
    Page<Order> findAllByUser(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.seller.id = :sellerId")
    Page<Order> findAllBySellerInfo(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN o.buyer b JOIN o.seller s WHERE o.id = :orderId AND (b.id = :userId OR s.id = :userId)")
    Optional<Order> findByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.buyer.id = :userId AND o.status = :status")
    Optional<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    @Query("SELECT o FROM Order o " + "LEFT JOIN FETCH o.orderInfoList " +
            "WHERE o.buyer.id = :buyerId AND o.status = :status AND o.isDeleted = false")
    List<Order> findAllByBuyerIdAndStatus(@Param("buyerId") Long buyerId, @Param("status") Order.Status status);
}
