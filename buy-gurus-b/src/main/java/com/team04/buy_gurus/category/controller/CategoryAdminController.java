package com.team04.buy_gurus.category.controller;

import com.team04.buy_gurus.category.dto.CategoryCreateRequest;
import com.team04.buy_gurus.category.dto.response.Response;
import com.team04.buy_gurus.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryService categoryService;

    @DeleteMapping("/category/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteCategory(final @PathVariable Long id){
        categoryService.deleteCategory(id);
        return Response.success();
    }
    //최초의 대분류 카테고리를 생성하는 로직
    @PostMapping("/FirstCategory")
    public Response createFirstCategory(@Valid @RequestBody final CategoryCreateRequest request){
        categoryService.createCategory(request.getName());
        return Response.success();
    }


    @PostMapping("/category")
    public Response createCategory(@Valid @RequestBody final CategoryCreateRequest request){
        categoryService.updateCategory(request);
        return Response.success();
    }
}