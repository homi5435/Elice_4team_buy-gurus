package com.team04.buy_gurus.product.service;

import com.team04.buy_gurus.product.aop.ProductNotFoundException;
import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.product.domain.ProductImage;
import com.team04.buy_gurus.product.dto.ProductRequest;
import com.team04.buy_gurus.product.dto.ProductResponse;
import com.team04.buy_gurus.product.repository.ProductImageRepository;
import com.team04.buy_gurus.product.repository.ProductRepository;
import com.team04.buy_gurus.utils.s3_bucket.S3BucketService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    //TODO 유저 인포, 카테고리 ID 받아오기
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final S3BucketService s3BucketService;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductImageRepository productImageRepository,
                          S3BucketService s3BucketService){
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.s3BucketService = s3BucketService;
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::mapToResponseDto);
    }

    public ProductResponse createProduct(ProductRequest request){
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .category(request.getCategory())
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

    public ProductResponse getProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        List<ProductImage> images = productImageRepository.findByProductId(product.getId());
        return mapToResponseDto(product, images);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다."));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());

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
                .category(product.getCategory())
                //.category(product.getCategory() != null ? product.getCategory().getName() : null)
                //sellerName(product.getSeller() != null ? product.getSeller().getName() : null)
                .build();
    }

    private ProductResponse mapToResponseDto(Product product) {
        List<ProductImage> images = productImageRepository.findByProductId(product.getId());
        return mapToResponseDto(product, images);
    }

    public void createTemporaryProduct() {
        // 임시 제품 정보
        ProductRequest request = new ProductRequest();
        request.setName("임시 제품");
        request.setPrice(10000L);
        request.setDescription("이것은 임시로 생성된 제품입니다.");
        request.setQuantity(10L);
        request.setCategory("임시 카테고리"); // 카테고리 ID 또는 객체로 설정 필요
        request.setImageFiles(new MultipartFile[0]); // 이미지를 추가할 경우 여기에 파일 배열을 설정
        // 제품 생성
        try {
            createProduct(request);
            log.info("임시 제품 생성 완료");
        } catch (Exception e) {
            // 로그 기록
            System.err.println("임시 제품 생성 중 오류 발생: " + e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        createTemporaryProduct(); // 애플리케이션 시작 시 임시 제품 생성
    }
}
