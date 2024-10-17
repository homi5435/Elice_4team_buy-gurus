package com.team04.buy_gurus.exception.ex_order.exception;

import com.team04.buy_gurus.common.enums.CommonError;
import lombok.Getter;

@Getter
public class OrderBaseException extends RuntimeException {
    private String code;

    public OrderBaseException(CommonError e) {
        super(e.getMessage());
        code = e.getCode();
    }

}
