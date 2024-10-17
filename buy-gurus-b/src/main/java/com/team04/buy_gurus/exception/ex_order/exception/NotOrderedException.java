package com.team04.buy_gurus.exception.ex_order.exception;

import com.team04.buy_gurus.common.enums.CommonError;
import lombok.Getter;

@Getter
public class NotOrderedException extends OrderBaseException {
    public NotOrderedException(CommonError e) {
        super(e);
    }
}
