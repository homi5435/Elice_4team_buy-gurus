package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    //리뷰 주문 정보 조회
    Optional<Order> findByUserIdAndStatus(Long userId, String status);
    Page<Order> findAllByIsDeletedFalse(Pageable pageable);
}
