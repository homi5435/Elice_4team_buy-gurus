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
