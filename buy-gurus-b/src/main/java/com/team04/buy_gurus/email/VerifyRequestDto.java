package com.team04.buy_gurus.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String code;
}
