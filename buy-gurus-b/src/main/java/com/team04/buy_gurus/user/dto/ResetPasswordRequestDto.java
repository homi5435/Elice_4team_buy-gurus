package com.team04.buy_gurus.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
