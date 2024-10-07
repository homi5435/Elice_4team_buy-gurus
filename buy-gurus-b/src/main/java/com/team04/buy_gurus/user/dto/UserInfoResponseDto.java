package com.team04.buy_gurus.user.dto;

import com.team04.buy_gurus.user.entity.Role;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {

    private String imageUrl;

    private String nickname;

    private String email;

    private Role role;
}
