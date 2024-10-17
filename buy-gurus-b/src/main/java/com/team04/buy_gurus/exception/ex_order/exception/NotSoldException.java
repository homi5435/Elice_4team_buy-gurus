package com.team04.buy_gurus.exception.ex_order.exception;

import com.team04.buy_gurus.common.enums.CommonError;

public class NotSoldException extends OrderBaseException {
    public NotSoldException(CommonError e) {
        super(e);
    }
}
