package com.team04.buy_gurus.product.dto;

import com.team04.buy_gurus.category.domain.Category;
import com.team04.buy_gurus.product.domain.Product;

import com.team04.buy_gurus.product.domain.ProductImage;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private Long quantity;
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();
    private String category;

    public ProductResponse(Product product, List<ProductImage> productImages) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.category = product.getCategory() != null ? product.getCategory().getName() : null; // 카테고리 이름 처리
        this.imageUrls = new ArrayList<>(); // 빈 리스트 초기화

        for (ProductImage productImage : productImages) {
            this.imageUrls.add(productImage.getImageUrl());
        }
    }
}
