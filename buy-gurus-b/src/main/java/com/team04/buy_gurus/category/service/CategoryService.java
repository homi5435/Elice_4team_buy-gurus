package com.team04.buy_gurus.category.service;

import com.team04.buy_gurus.category.dto.CategoryCreateRequest;
import com.team04.buy_gurus.category.dto.CategoryDto;
import com.team04.buy_gurus.category.dto.exception.CategoryNotFoundException;
import com.team04.buy_gurus.category.domain.Category;
import com.team04.buy_gurus.category.dto.response.CategoryFirstCreatedResponse;
import com.team04.buy_gurus.category.dto.response.CategorySubCreatedResponse;
import com.team04.buy_gurus.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    @Transactional
    public List<CategoryDto> findAllCategory(){
        List<Category> categories = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdASC();
        return CategoryDto.toDtoList(categories);
    }

    //생성
    @Transactional
    public CategoryFirstCreatedResponse createCategory(String name){
        Category category = new Category(name, null);
        Category categoryEntity = categoryRepository.save(category);
        return CategoryFirstCreatedResponse.builder().name(categoryEntity.getName()).id(categoryEntity.getId()).build();
    }
    /*@Transactional
    public void createCategory(String name){
        Category category = new Category(name, null);
        categoryRepository.save(category);
    }*/

    //삭제
    @Transactional
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.deleteById(category.getId());
    }

    //수정
    @Transactional
    public CategorySubCreatedResponse updateCategory(final CategoryCreateRequest request){
        Category parent = categoryRepository.findById(request.getParentId()) // request 의 parentId를 가지고 카테고리 찾기
                .orElseThrow(CategoryNotFoundException::new);
        Category categoryEntity = categoryRepository.save(new Category(request.getName(), parent));
        return CategorySubCreatedResponse.builder()
                .name(categoryEntity.getName())
                .parentId(categoryEntity.getParent().getId())
                .id(categoryEntity.getId())
                .build();

    }

}
