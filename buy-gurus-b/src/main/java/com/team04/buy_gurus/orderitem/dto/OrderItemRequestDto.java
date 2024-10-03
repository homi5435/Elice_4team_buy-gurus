package com.team04.buy_gurus.orderitem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {
    private Long user_id;
    private int quantity;
}
