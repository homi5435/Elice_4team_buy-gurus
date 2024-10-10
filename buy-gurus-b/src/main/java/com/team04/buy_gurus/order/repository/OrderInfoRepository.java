package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.Order;
import com.team04.buy_gurus.order.domain.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    Optional<OrderInfo> findByProductId(Long productId);
}
