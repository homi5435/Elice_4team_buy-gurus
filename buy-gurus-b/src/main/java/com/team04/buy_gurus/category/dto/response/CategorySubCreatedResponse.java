package com.team04.buy_gurus.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CategorySubCreatedResponse {

    private String name;
    private Long id;
    private Long parentId;
}
