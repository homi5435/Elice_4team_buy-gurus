package com.team04.buy_gurus.review.domain;

import com.team04.buy_gurus.product.domain.Product;
import com.team04.buy_gurus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE review SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating; // 별점

    @Column(length = 255)
    private String comment; // 리뷰 내용

    private Boolean isDeleted; // 삭제 여부

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; // 수정 시간

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = true, updatable = true)
    private Product product; // 상품 ID

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = true, updatable = true)
    private User user; // 사용자 정보
}
