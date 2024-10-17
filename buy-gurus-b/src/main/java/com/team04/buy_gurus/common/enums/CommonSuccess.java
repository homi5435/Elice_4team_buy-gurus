package com.team04.buy_gurus.common.enums;

import lombok.Getter;

@Getter
public enum CommonSuccess {
    // success의 code는 "success" 고정입니다!
    // 메시지만 enum화 하면 되겠습니다!
    ORDER_FOUND("주문 내역 조회 성공"),
    ORDER_SAVE_SUCCESS("주문 내역 저장 성공"),
    ORDER_UPDATE_SUCCESS("주문 내역 수정 성공"),
    ORDER_DELETE_SUCCESS("주문 내역 삭제 성공")
    ;

    private final String code = "success";
    private final String message;

    CommonSuccess(String message) {
        this.message = message;
    }
}
