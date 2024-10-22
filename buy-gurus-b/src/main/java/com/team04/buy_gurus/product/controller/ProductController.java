package com.team04.buy_gurus.product.controller;

import com.team04.buy_gurus.product.dto.ProductRequest;
import com.team04.buy_gurus.product.dto.ProductResponse;
import com.team04.buy_gurus.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable){
        Page<ProductResponse> responseDtoList = productService.getAllProducts(pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam(required = false) Long parentId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name,
            Pageable pageable) {
        Page<ProductResponse> responseDtoList = productService.searchProducts(parentId, categoryId, name, pageable);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestParam("quantity") Long quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("userId") Long userId,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    )
    {
        ProductRequest request = new ProductRequest(name, price, description, quantity, imageFiles, categoryId, userId);
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
        ProductResponse response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestParam("quantity") Long quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("userId") Long userId,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    ){
        ProductRequest request = new ProductRequest(name, price, description, quantity, imageFiles, categoryId, userId);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
