package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByIsDeletedFalse(Pageable pageable);
}
