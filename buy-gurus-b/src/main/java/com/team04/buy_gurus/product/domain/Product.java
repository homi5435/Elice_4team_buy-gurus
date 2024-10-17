package com.team04.buy_gurus.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {
    //상품 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    //삭제 여부
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    //상품 이름
    @Column(nullable = false)
    private String name;

    //상품 가격
    @Column(nullable = false)
    private Long price;

    //상품 설명
    @Column(nullable = false)
    private String description;

    //상품 재고
    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private String category;

    //TODO 카테고리, 판매자 정보 가져오기
//    @ManyToOne
//    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Category category;
//
//    @ManyToOne
//    @JoinColumn(name = "seller_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Seller seller;

    //판매자 아이디
    @Column(name = "seller_id")
    private Long sellerId;
}
