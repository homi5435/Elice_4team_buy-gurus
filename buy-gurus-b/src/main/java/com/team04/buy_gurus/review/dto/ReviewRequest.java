package com.team04.buy_gurus.review.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private Integer rating; // 별점
    private String comment; // 리뷰 내용
    private Long productId; // 상품 ID
    private Long userId; // 사용자 ID
}
