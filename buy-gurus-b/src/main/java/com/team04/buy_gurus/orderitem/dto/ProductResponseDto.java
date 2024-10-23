package com.team04.buy_gurus.orderitem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private Long price;
    private String imageUrl;
}
