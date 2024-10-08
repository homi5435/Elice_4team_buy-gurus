package com.team04.buy_gurus.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ProductRequest {
    @NotBlank(message = "상품 이름은 공백이 될 수 없습니다.")
    private String name;

    @Positive(message = "상품 가격은 1 이상 이어야 합니다.")
    private Long price;

    @NotBlank(message = "상품 설명은 공백이 될 수 없습니다.")
    private String description;

    @PositiveOrZero(message = "상품 재고는 0 또는 양수인 값 이어야 합니다.")
    private Long quantity;

    @NotBlank(message = "이미지를 반드시 포함하여야 합니다.")
    private MultipartFile[] imageFiles;

    @NotBlank(message = "카테고리는 반드시 선택하여야 합니다.")
    private String category;
}
