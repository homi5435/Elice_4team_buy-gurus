package com.team04.buy_gurus.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AddressInfoRequest {
    @NotBlank(message = "이름은 공백이 될 수 없습니다.")
    private String name;

    @NotBlank(message = "주소는 공백이 될 수 없습니다.")
    private String address;

    @Pattern(regexp = "^(010)-?([0-9]{4})-?([0-9]{4})$", message = "전화번호는 010-XXXX-XXXX 또는 하이픈이 빠진 형태입니다.")
    private String phoneNum;
}
