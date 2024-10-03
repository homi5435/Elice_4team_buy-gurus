package com.team04.buy_gurus.category.application.dto;

import com.team04.buy_gurus.category.persistence.entity.Category;

import java.util.List;

public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> childrens;
}
