package com.team04.buy_gurus.orderitem.repository;

import com.team04.buy_gurus.orderitem.domain.OrderItem;
import com.team04.buy_gurus.orderitem.dto.OrderItemViewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByUserAndProduct(User user, Product product);

    List<OrderItemViewDto> findByUser(User user);

    @Query("delete from OrderItem o where o.user_id = :user_id")
    void deleteAllByUserId(Long user_id);
}
