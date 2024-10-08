package com.team04.buy_gurus.orderitem.dto;

import com.team04.buy_gurus.orderitem.domain.OrderItem;
import com.team04.buy_gurus.product.domain.Product;
import lombok.*;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Product product;
    private Long amount;
    private Long price;

    public OrderItemResponseDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.product = orderItem.getProduct();
        this.amount = orderItem.getAmount();
        this.price = orderItem.getPrice();
    }
}
