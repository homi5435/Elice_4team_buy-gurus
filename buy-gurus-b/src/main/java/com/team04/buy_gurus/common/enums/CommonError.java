package com.team04.buy_gurus.common.enums;

import lombok.Getter;

@Getter
public enum CommonError {
    SELLER_NOT_FOUND("BG10010", "판매자가 존재하지 않습니다."),
    NOT_SELLER("BG10011", "판매자가 아닙니다."),


    USER_NOT_ORDERED("BG20001", "구매내역이 존재하지 않습니다."),
    ORDER_CREATE_FAILED("BG24001", "존재하지 않는 제품을 구매시도하였습니다."),
    SELLER_NOT_SOLD("BG20011", "해당 거래와 연관된 판매자가 아닙니다."),
    SELLER_NOT_ASSIGN_EDIT("BG20012", "판매자는 해당 거래의 편집 권한이 없습니다."),
    USER_NOT_ASSIGN_EDIT("BG20002", "구매 내역 편집 권한이 없습니다.")

    ;

    private final String code;
    private final String message;

    CommonError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
