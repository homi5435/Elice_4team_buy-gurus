package com.team04.buy_gurus.product.repository;

import com.team04.buy_gurus.product.domain.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 대분류 전체 검색
    @Query("SELECT p FROM Product p WHERE p.category.parent.id = :parentId")
    Page<Product> findAllByParentCategory(@Param("parentId") Long parentId, Pageable pageable);

    // 대분류에서 중분류 골라서 전체 검색
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findAllByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    // 대분류에서 중분류 선택 안하고 상품 이름으로 검색
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.category.parent.id = :parentId")
    Page<Product> findByNameAndParentCategory(@Param("name") String name, @Param("parentId") Long parentId, Pageable pageable);

    // 대분류 중분류 둘다 선택하고 상품 이름으로 검색
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.category.id = :categoryId")
    Page<Product> findByNameAndCategory(@Param("name") String name, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
    Page<Product> findByName(@Param("name") String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :productId")
    void discountQuantity(@Param("quantity") Long quantity, @Param("productId") Long productId);
}
