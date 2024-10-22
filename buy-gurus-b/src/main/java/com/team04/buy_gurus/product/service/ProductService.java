package com.team04.buy_gurus.product.service;

import com.team04.buy_gurus.category.domain.Category;
import com.team04.buy_gurus.category.dto.exception.CategoryNotFoundException;
import com.team04.buy_gurus.category.repository.CategoryRepository;
import com.team04.buy_gurus.exception.ex_user.ex.UserNotFoundException;
import com.team04.buy_gurus.product.aop.ProductNotFoundException;
import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.product.domain.ProductImage;
import com.team04.buy_gurus.product.dto.ProductRequest;
import com.team04.buy_gurus.product.dto.ProductResponse;
import com.team04.buy_gurus.product.repository.ProductImageRepository;
import com.team04.buy_gurus.product.repository.ProductRepository;
import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import com.team04.buy_gurus.sellerinfo.repository.SellerInfoRepository;
import com.team04.buy_gurus.utils.s3_bucket.S3BucketService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final SellerInfoRepository sellerInfoRepository;
    private final CategoryRepository categoryRepository;
    private final S3BucketService s3BucketService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductImageRepository productImageRepository,
                          SellerInfoRepository sellerInfoRepository,
                          CategoryRepository categoryRepository,
                          S3BucketService s3BucketService){
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.sellerInfoRepository = sellerInfoRepository;
        this.categoryRepository = categoryRepository;
        this.s3BucketService = s3BucketService;
    }

    @Transactional
    public Page<ProductResponse> getAllProducts(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::mapToResponseDto);
    }

    @Transactional
    public Page<ProductResponse> searchProducts(Long parentId, Long categoryId, String name, Pageable pageable) {
        if (parentId != null && categoryId == null && name == null) {
            // 대분류 전체 검색
            return productRepository.findAllByParentCategory(parentId, pageable)
                    .map(this::mapToResponseDto);
        } else if (parentId != null && categoryId != null && name == null) {
            // 대분류에서 중분류 골라서 전체 검색
            return productRepository.findAllByCategory(categoryId, pageable)
                    .map(this::mapToResponseDto);
        } else if (parentId != null && categoryId == null) {
            // 대분류에서 중분류 선택 안하고 상품 이름으로 검색
            return productRepository.findByNameAndParentCategory(name, parentId, pageable)
                    .map(this::mapToResponseDto);
        } else if (parentId != null) {
            // 대분류 중분류 둘다 선택하고 상품 이름으로 검색
            return productRepository.findByNameAndCategory(name, categoryId, pageable)
                    .map(this::mapToResponseDto);
        } else if (name != null) {
            // 카테고리 설정 없이 상품 이름만으로 검색
            return productRepository.findByName(name, pageable)
                    .map(this::mapToResponseDto);
        } else {
            // 기본적으로 모든 상품을 검색할 수도 있음
            return productRepository.findAll(pageable)
                    .map(this::mapToResponseDto);
        }
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request, Long userId){

        SellerInfo sellerInfo = sellerInfoRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .category(category)
                .seller(sellerInfo)
                .isDeleted(false)
                .build();

        Product savedProduct = productRepository.save(product);

        if(request.getImageFiles() != null){
            List<String> fileUrls;
            try{
                fileUrls = s3BucketService.upload(request.getImageFiles());
            } catch (IOException e){
                throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
            }

            for (String fileUrl : fileUrls){
                ProductImage productImage = ProductImage.builder()
                        .imageUrl(fileUrl)
                        .product(savedProduct)
                        .build();
                productImageRepository.save(productImage);
            }
        }

        return mapToResponseDto(savedProduct);
    }

    @Transactional
    public ProductResponse getProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        List<ProductImage> images = productImageRepository.findByProductId(product.getId());
        return mapToResponseDto(product, images);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setCategory(category);

        // 기존 이미지 삭제 및 새로운 이미지 추가
        productImageRepository.deleteByProductId(id); // 이전 이미지를 삭제
        if (request.getImageFiles() != null) {
            List<String> fileUrls;
            try {
                fileUrls = s3BucketService.upload(request.getImageFiles()); // S3에 업로드
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
            }

            for (String fileUrl : fileUrls) {
                ProductImage productImage = ProductImage.builder()
                        .imageUrl(fileUrl)
                        .product(product)
                        .build();
                productImageRepository.save(productImage);
            }
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("삭제할 상품이 존재하지 않습니다."));
        product.setDeleted(true);
        productRepository.save(product);
    }

    private ProductResponse mapToResponseDto(Product product, List<ProductImage> images){
        List<String> imageUrls = images.stream()
                .map(ProductImage::getImageUrl)
                .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .imageUrls(imageUrls)
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .tradeName(product.getSeller() != null ? product.getSeller().getTradeName() : null)
                .sellerUserId(product.getSeller() != null ? product.getSeller().getUser().getId() : null)
                .build();
    }

    private ProductResponse mapToResponseDto(Product product) {
        List<ProductImage> images = productImageRepository.findByProductId(product.getId());
        return mapToResponseDto(product, images);
    }
}
