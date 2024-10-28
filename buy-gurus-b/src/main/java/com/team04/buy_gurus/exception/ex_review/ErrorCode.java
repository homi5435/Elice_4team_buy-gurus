package com.team04.buy_gurus.exception.ex_review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    REVIEW_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "R001", "리뷰 작성 권한이 없습니다."),
    PRODUCT_NOT_PURCHASED(HttpStatus.BAD_REQUEST, "R002", "구매하지 않은 상품에 대해 리뷰를 작성할 수 없습니다."),
    DELIVERY_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "R003", "배송이 완료된 상품에 대해서만 리뷰를 작성할 수 있습니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "R004", "상품을 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}