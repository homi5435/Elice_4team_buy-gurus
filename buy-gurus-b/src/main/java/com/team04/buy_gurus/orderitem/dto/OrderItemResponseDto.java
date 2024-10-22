package com.team04.buy_gurus.orderitem.dto;

import com.team04.buy_gurus.orderitem.domain.OrderItem;
import lombok.*;

@Data
public class OrderItemResponseDto {
    private Long id;
    private ProductResponseDto product;
    private Long amount;
    private Long price;

    public OrderItemResponseDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.product = new ProductResponseDto(orderItem.getProduct().getName(), orderItem.getProduct().getPrice());
        this.amount = orderItem.getAmount();
        this.price = orderItem.getPrice();
    }
}
