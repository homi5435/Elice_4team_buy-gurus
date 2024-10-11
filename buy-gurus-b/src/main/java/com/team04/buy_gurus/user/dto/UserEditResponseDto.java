package com.team04.buy_gurus.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEditResponseDto {

    // private String imageUrl;

    private String nickname;

    private String email;
}
