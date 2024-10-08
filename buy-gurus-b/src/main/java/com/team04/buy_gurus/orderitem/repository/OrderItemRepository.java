package com.team04.buy_gurus.orderitem.repository;

import com.team04.buy_gurus.orderitem.domain.OrderItem;
import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByUserAndProduct(User user, Product product);

    List<OrderItem> findByUser(User user);

    @Modifying
    @Query("delete from OrderItem o where o.user.id = :user_id")
    void deleteAllByUser(Long user_id);
}
