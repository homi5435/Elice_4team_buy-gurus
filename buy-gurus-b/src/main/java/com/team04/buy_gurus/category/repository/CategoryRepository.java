package com.team04.buy_gurus.category.repository;

import com.team04.buy_gurus.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c LEFT JOIN c.parent p ORDER BY p.id ASC NULLS FIRST , c.id ASC")
    List<Category> findAllOrderByParentIdAscNullsFirstCategoryIdASC();

}
