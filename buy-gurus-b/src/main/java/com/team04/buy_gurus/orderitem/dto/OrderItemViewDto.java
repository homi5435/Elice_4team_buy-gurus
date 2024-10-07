package com.team04.buy_gurus.orderitem.dto;

import com.team04.buy_gurus.product.domain.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemViewDto {
    private Long id;
    private Product product;
    private int amount;
    private int price;
}
