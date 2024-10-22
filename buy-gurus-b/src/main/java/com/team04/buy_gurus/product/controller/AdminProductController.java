package com.team04.buy_gurus.product.controller;

import com.team04.buy_gurus.product.dto.ProductRequest;
import com.team04.buy_gurus.product.dto.ProductResponse;
import com.team04.buy_gurus.product.service.ProductService;
import com.team04.buy_gurus.user.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestParam("quantity") Long quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("imageFiles") MultipartFile[] imageFiles,
            @AuthenticationPrincipal CustomUserDetails userDetails
    )
    {
        ProductRequest request = new ProductRequest(name, price, description, quantity, imageFiles, categoryId);
        ProductResponse response = productService.createProduct(request, userDetails.getUserId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @RequestParam("name") String name,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestParam("quantity") Long quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("imageFiles") MultipartFile[] imageFiles
    ){
        ProductRequest request = new ProductRequest(name, price, description, quantity, imageFiles, categoryId);
        ProductResponse response = productService.updateProduct(productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
