package com.team04.buy_gurus.user.dto;

import com.team04.buy_gurus.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    // private String imageUrl;
    private Long id;

    private String nickname;

    private String email;

    private Role role;
}
