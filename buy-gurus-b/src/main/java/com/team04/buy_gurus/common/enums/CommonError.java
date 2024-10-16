package com.team04.buy_gurus.common.enums;

import lombok.Getter;

@Getter
public enum CommonError {
    ;

    private final String code;
    private final String message;

    CommonError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
