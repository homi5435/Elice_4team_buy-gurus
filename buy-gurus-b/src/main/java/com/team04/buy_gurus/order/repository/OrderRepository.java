package com.team04.buy_gurus.order.repository;

import com.team04.buy_gurus.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
