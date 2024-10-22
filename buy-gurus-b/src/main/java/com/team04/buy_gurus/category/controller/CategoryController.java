package com.team04.buy_gurus.category.controller;

import com.team04.buy_gurus.category.dto.CategoryCreateRequest;
import com.team04.buy_gurus.category.dto.CategoryDto;
import com.team04.buy_gurus.category.dto.response.Response;
import com.team04.buy_gurus.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

//페이지네이션이 꼭 필요할까?
   /* @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category")
    public Response findAllCategory(){ return Response.success(categoryService.findAllCategory());}*/
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/category")
    public List<CategoryDto> findAllCategory(){ return categoryService.findAllCategory();}
}
