package com.team04.buy_gurus.user.controller;

import com.team04.buy_gurus.jwt.service.JwtService;
import com.team04.buy_gurus.user.CustomUserDetails;
import com.team04.buy_gurus.user.dto.*;
import com.team04.buy_gurus.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse<Void>> createUser(@RequestBody @Valid SignupRequestDto request) {

            userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserResponse<>("회원가입 성공", null));
    }

    @GetMapping("/userMe")
    public ResponseEntity<UserResponse<UserInfoResponseDto>> readUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

            UserInfoResponseDto response = userService.loadUserInfo(customUserDetails.getUserId());
            return ResponseEntity.ok(new UserResponse<>("유저 정보 조회 성공", response));
    }

    @PatchMapping("/userMe")
    public ResponseEntity<UserResponse<UserEditResponseDto>> updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody @Valid UserEditRequestDto request) {

            UserEditResponseDto response = userService.editUserInfo(customUserDetails.getUserId(), request);
            return ResponseEntity.ok(new UserResponse<>("회원 정보 수정 성공", response));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<UserResponse<Void>> updatePassword(@RequestBody @Valid ResetPasswordRequestDto request) {

        userService.resetPassword(request);
        return ResponseEntity.ok(new UserResponse<>("비밀번호 재설정 성공", null));
    }

    @DeleteMapping("/userMe")
    public ResponseEntity<UserResponse<Void>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletResponse response) {

        userService.withdrawal(customUserDetails.getUserId());
        jwtService.removeAccessTokenToCookie(response);
        jwtService.removeRefreshTokenToCookie(response);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new UserResponse<>("회원 탈퇴 성공", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponse<Void>> logout(HttpServletResponse response) {

        jwtService.removeAccessTokenToCookie(response);
        jwtService.removeRefreshTokenToCookie(response);

        return ResponseEntity.ok(new UserResponse<>("로그아웃 성공", null));
    }
}
