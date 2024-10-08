package com.team04.buy_gurus.orderitem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class OrderItemRequestDto {
    private int quantity;
    private Long amount;


}
