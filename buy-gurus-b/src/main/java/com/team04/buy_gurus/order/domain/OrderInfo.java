package com.team04.buy_gurus.order.domain;

import com.team04.buy_gurus.order.dto.OrderRequest;
import com.team04.buy_gurus.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_info")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {
    public OrderInfo(OrderRequest.OrderInfoRequest orderInfoRequest) {
        this.quantity = orderInfoRequest.getQuantity();
        this.price = orderInfoRequest.getPrice();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private int quantity;

    // 주문 당시 정보
    private int price;
    private String productName;
    private String productImageUrl;

    // 상품 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    public OrderInfo(int price, String productName, String imageUrl) {
        this.price = price;
        this.productName = productName;
        this.productImageUrl = imageUrl;
    }
}
