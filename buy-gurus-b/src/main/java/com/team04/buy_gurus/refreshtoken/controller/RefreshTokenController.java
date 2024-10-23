package com.team04.buy_gurus.refreshtoken.controller;

import com.team04.buy_gurus.refreshtoken.service.RefreshTokenService;
import com.team04.buy_gurus.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token")
    public ResponseEntity<UserResponse<Void>> tokenReissue(HttpServletRequest request, HttpServletResponse response) throws IOException {

        refreshTokenService.tokenReissue(request, response);
        return ResponseEntity.ok(new UserResponse<>("토큰 재발급 성공", null));
    }
}
