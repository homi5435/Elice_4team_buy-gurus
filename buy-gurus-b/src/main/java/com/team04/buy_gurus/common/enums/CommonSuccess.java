package com.team04.buy_gurus.common.enums;

import lombok.Getter;

@Getter
public enum CommonSuccess {
    // success의 code는 "success" 고정입니다!
    // 메시지만 enum화 하면 되겠습니다!
    ;

    private final String code = "success";
    private final String message;

    CommonSuccess(String message) {
        this.message = message;
    }
}
