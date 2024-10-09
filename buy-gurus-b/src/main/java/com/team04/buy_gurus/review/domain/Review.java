package com.team04.buy_gurus.review.domain;

import com.team04.buy_gurus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "리뷰")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private java.time.LocalDateTime createdAt; // 생성 시간

    @Column(name = "modified_at")
    private java.time.LocalDateTime modifiedAt; // 수정 시간

    @Column(name = "product_id")
    private Long productId; // 상품 ID

    @Column(name = "user_id")
    private Long userId; // 사용자 ID

//    @ManyToOne
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    private User user; // 사용자 정보
}
