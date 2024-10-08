package com.team04.buy_gurus.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id; // 리뷰 ID
    private Integer rating; // 별점
    private String comment; // 리뷰 내용
    private Boolean isDeleted; // 삭제 여부
    private Long productId; // 상품 ID
    private Long userId; // 유저 ID
    private String userNickname; // 사용자 별명
}
