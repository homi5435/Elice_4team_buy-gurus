package com.team04.buy_gurus.exception.ex_order.exception;

import com.team04.buy_gurus.common.enums.CommonError;
import lombok.Getter;

@Getter
public class NotSellerException extends OrderBaseException {
    public NotSellerException(CommonError e) {
        super(e);
    }
}
