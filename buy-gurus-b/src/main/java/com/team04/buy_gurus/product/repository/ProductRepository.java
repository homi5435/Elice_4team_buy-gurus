package com.team04.buy_gurus.product.repository;

import com.team04.buy_gurus.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
