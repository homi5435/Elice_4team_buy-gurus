package com.team04.buy_gurus.category.dto;

import com.team04.buy_gurus.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;


    public static List<CategoryDto> toDtoList(List<Category> categories) {
        CategoryHelper helper = CategoryHelper.newInstance(
                categories,
                c -> new CategoryDto(c.getId(), c.getName(), new ArrayList<>()), // 각 CategoryDto id, name 설정
                Category::getParent,
                Category::getId,
                CategoryDto::getChildren);
        return helper.convert();
    }
}
//