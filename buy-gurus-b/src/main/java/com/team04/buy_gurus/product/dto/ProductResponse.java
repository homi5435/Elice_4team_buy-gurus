package com.team04.buy_gurus.product.dto;

import com.team04.buy_gurus.product.domain.Product;

import com.team04.buy_gurus.product.domain.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Long quantity;
    private List<String> imageUrls;
    private String category;

    public ProductResponse(Product product, ProductImage productImage){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.imageUrls = Collections.singletonList(productImage.getImageUrl());
        this.category = product.getCategory();
    }
}
