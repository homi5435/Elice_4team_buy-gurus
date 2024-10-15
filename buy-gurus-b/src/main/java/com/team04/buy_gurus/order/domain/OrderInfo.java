package com.team04.buy_gurus.order.domain;

import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "order_info")
@SQLDelete(sql = "UPDATE order_info SET is_deleted = true WHERE id = ?")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private int quantity;

    // 주문 당시 정보
    private int price;
    private String productName;
    private String productImageUrl;

    private boolean isReviewed;
    private boolean isDeleted;

    // 상품 id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Order order;

    public OrderInfo(OrderRequest.OrderInfoRequest orderInfoRequest, Product product, Order order) {
        this.quantity = orderInfoRequest.getQuantity();
        this.price = orderInfoRequest.getPrice();
        this.product = product;
        this.order = order;
    }
}
