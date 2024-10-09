package com.team04.buy_gurus.category.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "카테고리명을 입력하세요.")
    @Size(min = 2, max = 15, message = "길이 제한은 2~15자 이내 입니다.")
    private String name;

    private Long parentId;
}
