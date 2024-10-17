package com.team04.buy_gurus.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserEditRequestDto {

    // private String imageUrl;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

}
