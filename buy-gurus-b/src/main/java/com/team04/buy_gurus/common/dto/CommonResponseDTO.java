package com.team04.buy_gurus.common.dto;

import com.team04.buy_gurus.common.enums.CommonError;
import com.team04.buy_gurus.common.enums.CommonSuccess;
import lombok.Getter;

@Getter
public class CommonResponseDTO<D> {
    private final String code;
    private final String message;
    private final D data;

    public CommonResponseDTO(String code, String message) {
        this(code, message, null);
    }

    public CommonResponseDTO(String code, String message, D data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponseDTO(CommonError commonError) {
        this(commonError, null);
    }

    public CommonResponseDTO(CommonError commonError, D data) {
        this.code = commonError.getCode();
        this.message = commonError.getMessage();
        this.data = data;
    }

    public CommonResponseDTO(CommonSuccess commonSuccess) {
        this(commonSuccess, null);
    }

    public CommonResponseDTO(CommonSuccess commonSuccess, D data) {
        this.code = commonSuccess.getCode();
        this.message = commonSuccess.getMessage();
        this.data = data;
    }
}
