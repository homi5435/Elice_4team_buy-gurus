package com.team04.buy_gurus.orderitem.domain;

import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public OrderItem(Long amount, Long price, User user, Product product) {
        this.amount = amount;
        this.price = price;
        this.user = user;
        this.product = product;
    }
}
