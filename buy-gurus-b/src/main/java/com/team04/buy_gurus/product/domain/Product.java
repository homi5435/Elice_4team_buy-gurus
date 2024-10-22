package com.team04.buy_gurus.product.domain;

import com.team04.buy_gurus.category.domain.Category;
import com.team04.buy_gurus.sellerinfo.entity.SellerInfo;
import com.team04.buy_gurus.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = true, updatable = true)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id", insertable = true, updatable = true)
    private User seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;
}
