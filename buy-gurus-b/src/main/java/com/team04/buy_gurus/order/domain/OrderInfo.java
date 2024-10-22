package com.team04.buy_gurus.order.domain;

import com.team04.buy_gurus.order.dto.BOrderRequest;
import com.team04.buy_gurus.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long price;
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

    public OrderInfo(BOrderRequest.OrderInfoRequest orderInfoRequest, Product product, Order order) {
        this.quantity = orderInfoRequest.getQuantity();
        this.productName = product.getName();
        this.productImageUrl = product.getProductImages().get(0).getImageUrl();
        this.product = product;
        this.order = order;
        this.price = product.getPrice();
    }

    public void setIsReviewed() {
        isReviewed = true;
    }
}
