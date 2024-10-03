package com.team04.buy_gurus.product.dto;

import com.team04.buy_gurus.product.domain.Product;
import lombok.Data;

@Data
public class ProductResponse {
    private Long productId;
    private String name;
    private Long price;
    private String description;
    private Long quantity;
    private String imageUrl;
    private String category;

    public ProductResponse(Product product){
        this.productId = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory();
    }
}
