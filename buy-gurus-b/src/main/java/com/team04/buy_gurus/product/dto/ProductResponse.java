package com.team04.buy_gurus.product.dto;

import com.team04.buy_gurus.product.domain.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Long quantity;
    private String imageUrl;
    private String category;

    public ProductResponse(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
    }
}
